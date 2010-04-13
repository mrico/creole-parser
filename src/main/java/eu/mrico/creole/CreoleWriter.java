package eu.mrico.creole;

import java.io.OutputStream;

import eu.mrico.creole.ast.Document;

public interface CreoleWriter {

    void write(Document document, OutputStream out) throws CreoleException;
}
