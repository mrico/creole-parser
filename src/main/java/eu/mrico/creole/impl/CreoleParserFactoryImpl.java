package eu.mrico.creole.impl;

import eu.mrico.creole.CreoleParser;
import eu.mrico.creole.CreoleParserFactory;

public class CreoleParserFactoryImpl extends CreoleParserFactory {

	@Override
	public CreoleParser newParser() {
		return new CreoleParserImpl();
	}

}
