package eu.mrico.creole;

import eu.mrico.creole.impl.CreoleParserFactoryImpl;

public abstract class CreoleParserFactory {

    public static final String FACTORY_CLASS = "eu.mrico.creole.CreoleParserFactory";
    private static final String DEFAULT_PARSER_FACTORY = CreoleParserFactoryImpl.class.getName();

    public abstract CreoleParser newParser();

    public static CreoleParserFactory newInstance() {
        final String factoryClass = System.getProperty(FACTORY_CLASS, DEFAULT_PARSER_FACTORY);
        try {
            Class<?> clazz = Class.forName(factoryClass);
            return (CreoleParserFactory) clazz.newInstance();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
