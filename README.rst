creole-parser
=============

Creole-parser is a simple java library to parse CREOLE wiki markup and converts
it to XHTML.


Requirements
************

This library requires Java 6 or higher.

Usage
*****

Convert some wiki markup to xhtml: ::

    Document doc = Creole.parse("** Some wiki markup **");
    new XHtmlWriter().write(doc, System.out);

