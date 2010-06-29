package eu.mrico.creole;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import eu.mrico.creole.ast.Document;
import eu.mrico.creole.xhtml.XHtmlWriter;

public class Creole {

    public static Document parse(String s) {
        try {
            CreoleParser parser = CreoleParserFactory.newInstance().newParser();
            return parser.parse(new StringReader(s));

        } catch (CreoleException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document parse(InputStream in) {
        try {
            CreoleParser parser = CreoleParserFactory.newInstance().newParser();
            return parser.parse(new InputStreamReader(in));

        } catch (CreoleException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) throws CreoleException {
        Document document = Creole.parse(System.in);
        new XHtmlWriter().write(document, System.out);
    }
}
