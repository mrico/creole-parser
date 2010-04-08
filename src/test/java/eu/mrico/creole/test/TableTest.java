package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.ASTDebugger;
import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Cell;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.Image;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Row;
import eu.mrico.creole.ast.Table;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Tables
 */
public class TableTest {
			
    @Test
    public void simpleTable() {

            final String wiki = "|=Heading Col 1 |=Heading Col 2         |\n" +
                                                    "|Cell 1.1       |[[Cell 1.2]]               |\n" +
                                                    "|//Cell 2.1//       |**Cell 2.2**               |\n";

            Document is = Creole.parse(wiki);

            Table table = (Table) new Table()
                    .add(new Row()
                            .add(new Cell().addAll(Text.asArray("Heading Col 1")))
                            .add(new Cell().addAll(Text.asArray("Heading Col 2"))))
                    .add(new Row()
                            .add(new Cell().addAll(Text.asArray("Cell 1.1")))
                            .add(new Cell().add(new Link("Cell 1.2", "Cell 1.2"))))
                    .add(new Row()
                            .add(new Cell().add(new Italic().addAll(Text.asArray("Cell 2.1"))))
                            .add(new Cell().add(new Bold().addAll(Text.asArray("Cell 2.2"))))
                    );

            Document expected = (Document) new Document()
                    .add(table);

            assertEquals(expected, is);
    }

    @Test
    public void tableWihtoutTrailingSlahes() {

            final String wiki = "|=Heading Col 1 |=Heading Col 2         \n" +
                                                    "|Cell 1.1       |Cell 1.2\n" +
                                                    "|Cell 2.1       |Cell 2.2\n";

            Document is = Creole.parse(wiki);

            Table table = (Table) new Table()
                    .add(new Row()
                            .add(new Cell().addAll(Text.asArray("Heading Col 1")))
                            .add(new Cell().addAll(Text.asArray("Heading Col 2"))))
                    .add(new Row()
                            .add(new Cell().addAll(Text.asArray("Cell 1.1")))
                            .add(new Cell().addAll(Text.asArray("Cell 1.2"))))
                    .add(new Row()
                            .add(new Cell().addAll(Text.asArray("Cell 2.1")))
                            .add(new Cell().addAll(Text.asArray("Cell 2.2")))
                    );

            Document expected = (Document) new Document()
                    .add(table);

            assertEquals(expected, is);
    }
}
