package eu.mrico.creole.impl;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.CreoleParser;
import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Cell;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Element;
import eu.mrico.creole.ast.Heading;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.Image;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Plugin;
import eu.mrico.creole.ast.Preformatted;
import eu.mrico.creole.ast.Row;
import eu.mrico.creole.ast.Table;
import eu.mrico.creole.ast.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 *
 * @author Marco Rico-Gomez
 */
class CreoleParserImpl implements CreoleParser {

    private BufferedReader reader;
    private String currentLine;
    private Element parent;

    @Override
    public Document parse(InputStream in) throws CreoleException {
        return parse(new InputStreamReader(in));
    }

    @Override
    public Document parse(Reader stream) throws CreoleException {
        this.reader = new BufferedReader(stream);

        Document document = new Document();
        this.parent = document;
        String line = null;

        boolean newParagraph = true;

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    // new block
                    this.parent = new Paragraph();
                    document.add(parent);
                    newParagraph = true;

                } else if (line.startsWith("=")) {
                    document.add(parseHeading(line));

                    this.parent = new Paragraph();
                    document.add(parent);
                    newParagraph = true;

                } else if (line.matches("^\\*[^\\*].*")) {
                    // unordered list
                    parent.add(parseList(line, false));

                } else if (line.startsWith("#")) {
                    // ordered list
                    parent.add(parseList(line, true));

                } else if (line.startsWith("|")) {
                    // table
                    parent.add(parseTable(line));

                } else if (line.equals("{{{")) {
                    parent.add(readPreformatted());

                } else if (line.matches("\\{\\{[^\\{].*")) {
                    // TODO: find better way to recognize image pattern
                    // image
                    parent.add(readImage(line));

                } else if (line.startsWith("----")) {
                    document.add(new HorizontalRule());
                
                } else {
                    if (!newParagraph) {
                        parent.add(new Text(" "));
                    }

                    this.parent = parseString(parent, line);
                    newParagraph = false;
                }
            }

            document.clean();

            return document;

        } catch (IOException e) {
            throw new CreoleException(e);
        }
    }

    private Heading parseHeading(String s) {
        if (!s.startsWith("=")) {
            throw new IllegalArgumentException();
        }

        int level;
        for (level = 0; s.charAt(level) == '='; level++);

        s = s.substring(level);
        s = s.replaceAll("=+$", "");
        s = s.trim();

        return new Heading(level, s);
    }

    private Element parseString(Element ctx, String s) throws IOException {
        CharacterReader cReader = new CharacterReader(new StringReader((s)));
        Character c = null;

        boolean escaped = false;

        while ((c = cReader.next()) != null) {
            Character nextChar = cReader.peek();
            if (nextChar == null) {
                nextChar = '\n';
            }

            if (!escaped && c == '*' && nextChar == '*') {
                cReader.skip(1);
                if (ctx instanceof Bold) {
                    ctx = ctx.getParent(); // close bold
                } else {
                    Bold bold = new Bold();
                    ctx.add(bold);
                    ctx = bold;
                }

            } else if (!escaped && c == '/' && nextChar == '/') {
                cReader.skip(1);
                if (ctx instanceof Italic) {
                    ctx = ctx.getParent(); // close italic
                } else {
                    Italic italic = new Italic();
                    ctx.add(italic);
                    ctx = italic;
                }
            } else if (!escaped && c == '{' && "{{".equals(cReader.peek(2))) {
                cReader.skip(2);
                ctx.add(readInlineNoWiki(cReader));

            } else if (!escaped && c == '[' && nextChar == '[') {
                cReader.skip(1);
                ctx.add(readLink(cReader));

            } else if (!escaped && c == '\\' && nextChar == '\\') {
                cReader.skip(1);
                ctx.add(new LineBreak());

            } else if (!escaped && c == '<' && nextChar == '<') {
                cReader.skip(1);
                ctx.add(readPlugin(cReader));
                
            } else if (c == 'h') {
                String tmp = "h" + cReader.peek(6);
                if ("http://".equals(tmp)) {
                    parseRawLink(ctx, cReader, escaped);
                } else {
                    ctx.add(new Text(c));
                }

            } else if (!escaped && c == '~' && (nextChar != ' ' && nextChar != '\n')) {
                escaped = true;

            } else {
                ctx.add(new Text(c));
                escaped = false;
            }
        }

        return ctx;
    }

    private void parseRawLink(Element ctx, CharacterReader cReader, boolean escaped) throws IOException {
        StringBuffer sb = new StringBuffer("h");
        Character c = null;
        while ((c = cReader.next()) != null && c != ' ') {
            sb.append(c);
        }

        boolean appendLc = false;
        char lc = sb.charAt(sb.length() - 1);

        if (lc == ',' || lc == '.' || lc == '?' || lc == '!'
                || lc == ':' || lc == ';' || lc == '"' || lc == '\'') {

            appendLc = true;
            sb.deleteCharAt(sb.length() - 1);
        }

        String s = sb.toString();

        ctx.add(escaped ? new Text(s) : new Link(s));

        if (appendLc) {
            ctx.add(new Text(lc));
        }

        if (c != null) {
            ctx.add(new Text(c));
        }
    }

    private Link readLink(CharacterReader cReader) throws IOException {

        String label = null;
        String target = null;

        StringBuffer sb = new StringBuffer();

        Character c = null;
        while ((c = cReader.next()) != null) {

            Character nextChar = cReader.peek();
            if (nextChar == null) {
                nextChar = '\0';
            }

            if (c == '|') {
                target = sb.toString();
                sb = new StringBuffer();

            } else if (c == ']' && nextChar == ']') {
                cReader.skip(1);
                break;

            } else {
                sb.append(c);
            }
        }

        if (target == null) {
            target = sb.toString();
        }

        if (label == null && sb.length() > 0) {
            label = sb.toString();
        } else {
            label = target;
        }

        return new Link(label, target);
    }

    private Image readImage(String line) throws IOException {

        String alt = null;
        String src = null;

        CharacterReader cReader = new CharacterReader(new StringReader(line));
        cReader.skip(2); // skip '{{'

        StringBuffer sb = new StringBuffer();

        Character c = null;
        while ((c = cReader.next()) != null) {

            Character nextChar = cReader.peek();
            if (nextChar == null) {
                nextChar = '\0';
            }

            if (c == '|') {
                src = sb.toString();
                sb = new StringBuffer();

            } else if (c == '}' && nextChar == '}') {
                cReader.skip(1);
                break;

            } else {
                sb.append(c);
            }
        }

        if (src == null) {
            src = sb.toString();
        }

        if (alt == null && sb.length() > 0) {
            alt = sb.toString();
        } else {
            alt = src;
        }

        return new Image(src, alt);
    }

    private Plugin readPlugin(CharacterReader cReader) throws IOException {
        String name = null;
                
        StringBuffer sb = new StringBuffer();

        Character c = null;
        while ((c = cReader.next()) != null) {

            Character nextChar = cReader.peek();
            if (nextChar == null) {
                nextChar = '\0';
            }
           
            if (c == '>' && nextChar == '>') {
                cReader.skip(1);
                break;

            } else {
                sb.append(c);
            }
        }

        name = sb.toString();
        name = name.trim();

        return new Plugin(name);
    }

    private Preformatted readPreformatted() throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("}}}")) {
                break;
            }

            sb.append(line).append('\n');
        }

        return new Preformatted(sb.toString());
    }

    private Preformatted readInlineNoWiki(CharacterReader cReader) throws IOException {
        StringBuffer sb = new StringBuffer();

        Character c = null;
        while ((c = cReader.next()) != null) {
            if (c == '}') {
                int closingBraces;
                String tmp = cReader.peek(10);
                for (closingBraces = 1; closingBraces <= tmp.length() && tmp.charAt(closingBraces - 1) == '}'; closingBraces++);

                int extraBraces = closingBraces >= 3 ? closingBraces - 3 : closingBraces;
                for (int i = 0; i < extraBraces; i++) {
                    sb.append('}');
                }

                cReader.skip(closingBraces - 1);

                if (closingBraces >= 3) {
                    break;
                }

            } else {
                sb.append(c);
            }
        }

        return new Preformatted(true, sb.toString().trim());
    }

    private Table parseTable(String s) throws IOException {
        Table table = new Table();

        // parse first row
        table.add(parseRow(s));

        while ((s = reader.readLine()) != null) {
            s = s.trim();
            if (s.length() == 0) {
                break;
            } else if (!s.startsWith("|")) {
                break;
            }

            table.add(parseRow(s));
        }

        return table;
    }

    private Row parseRow(String s) throws IOException {
        Row row = new Row();
        String columns[] = s.split("\\|");

        for (int i = 1; i < columns.length; i++) {
            String col = columns[i].trim();
            boolean headline = col.startsWith("=");
            if (headline) {
                col = col.substring(1);
            }

            col = col.trim();

            Cell cell = new Cell();
            cell.setHeading(headline);

            parseString(cell, col);
            row.add(cell);
        }

        return row;
    }

    private List parseList(String s, boolean ordered) throws IOException {
        List list = new List(ordered ? List.ORDERED : List.UNORDERED);

        final Character listChar = ordered ? '#' : '*';

        final int level = countChars(s, listChar);

        ListItem item = parseListItem(s, listChar);
        list.add(item);

        String line = null;
        while ((line = readLine()) != null) {
            line = line.trim();

            int starCount = countChars(line, listChar);

            if (starCount > level) {
                item.add(parseList(line, ordered));

                line = currentLine;
                starCount = countChars(line, listChar);
            }

            if (line.length() == 0) {
                break;
            } else if (!line.startsWith(listChar.toString())) {
                break;
            }

            if (starCount == level) {
                item = parseListItem(line, listChar);
                list.add(item);

            } else if (starCount > level) {
                item.add(parseList(line, ordered));

            } else if (starCount < level) {
                break;
            }
        }

        return list;
    }

    private ListItem parseListItem(String s, char listChar) throws IOException {
        s = s.replaceAll("^\\" + listChar + "+ *", "");

        ListItem item = new ListItem();
        parseString(item, s);

        return item;
    }

    private int countChars(String s, char c) {
        int i;
        for (i = 0; i < s.length() && s.charAt(i) == c; i++);

        return i;
    }

    private String readLine() throws IOException {
        this.currentLine = reader.readLine();
        return currentLine;
    }
}
