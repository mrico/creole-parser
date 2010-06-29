package eu.mrico.creole;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import eu.mrico.creole.ast.Document;
import eu.mrico.creole.plugins.CurrentDateTimePlugin;
import java.util.HashMap;
import java.util.Map;
import eu.mrico.creole.xhtml.XHtmlWriter;

public class Creole {

    private static Map<String, CreolePluginHandler> PLUGINS = new HashMap<String, CreolePluginHandler>();
    static {
        addPlugin(new CurrentDateTimePlugin());
    }

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

    public static void addPlugin(CreolePluginHandler plugin) {
        PLUGINS.put(plugin.getName(), plugin);
    }

    public static void removePlugin(String pluginName) {
        PLUGINS.remove(pluginName);
    }

    public static CreolePluginHandler getPlugin(String name) {
        return PLUGINS.get(name);
    }

    public static void main(String args[]) throws CreoleException {
        Document document = Creole.parse(System.in);
        new XHtmlWriter().write(document, System.out);
    }
}
