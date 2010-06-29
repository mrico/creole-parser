creole-parser
=============

Creole-parser is a simple java library to parse CREOLE wiki markup and converts
it to XHTML.


Requirements
************

This library requires Java 6 or higher.
To build creole-parser you must have Apache Maven installed. (http://maven.apache.org)

The creole-parser itself has no additional jar dependencies!


Getting started
***************

Grap the latest sources from here and deploy the jar into your local maven repository: ::

	git clone git@github.com:mrico/creole-parser.git
	mvn install


Just use it (standalone): ::

	java -jar creole-parser-${version}.jar < sample.wiki > sample.html


Usage
*****

Convert some wiki markup to xhtml: ::

    Document doc = Creole.parse("** Some wiki markup **");
    new XHtmlWriter().write(doc, System.out);


Customize XHtmlWriter to use css classes: ::

	Document doc = Creole.parse("** Some wiki markup **");
	
	XHtmlWriter writer = new XHtmlWriter();
	writer.addCssClass("p", "section");
    
	writer.write(doc, System.out);


Customize XHtmlWriter to decorate specific elements: ::

	Document doc = Creole.parse("** Some wiki markup **");

	XHtmlWriter writer = new XHtmlWriter();
	writer.setDecorator(Paragraph.class, new DivElementDecorator("section");

	writer.write(doc, System.out);

You can easily create your own decorators by implementing XHtmlElementDecorator.
