package eu.mrico.creole.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Stack;

class CharacterReader {

    private Reader reader;
    private Stack<Character> stack = new Stack<Character>();

    public CharacterReader(InputStream in) {
        this.reader = new InputStreamReader(in);
    }

    public CharacterReader(Reader reader) {
        this.reader = reader;
    }

    public String peek(int c) throws IOException {
        StringBuffer sb = new StringBuffer();

        if(c <= stack.size()) {
            for(int i = stack.size() - 1; i >= stack.size() - c; i--)
                sb.append(stack.get(i));

            return sb.toString();
        }

        for (int i = 0; i < c; i++) {
            Character cc = next(true);
            if (cc == null) {
                break;
            }

            sb.append(cc);
        }

        for (int i = sb.length() - 1; i >= 0; i--) {
            stack.push(sb.charAt(i));
        }

        return sb.toString();
    }

    public Character peek() throws IOException {
        Character c = next(true);
        if (c == null) {
            return null;
        }

        stack.push(c);

        return c;
    }

    public void skip(int c) throws IOException {
        for (int i = 0; i < c; i++) {
            next();
        }
    }

    public void push(char c) throws IOException {
        stack.push(c);
    }

    public Character next() throws IOException {
        return next(true);
    }

    public Character next(boolean withStack) throws IOException {
        if (withStack && !stack.isEmpty()) {
            return stack.pop();
        }

        int c = reader.read();
        if (c == -1) {
            return null;
        }
        if (c == '\r') {
            return next();
        }

        return new Character((char) c);
    }
}
