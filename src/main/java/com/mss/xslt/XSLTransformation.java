package com.mss.xslt;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTransformation {

	public void xslTransform(String string) throws TransformerFactoryConfigurationError, TransformerException{
		Source xmlInput = new StreamSource(new File("decoded.xml"));
		Source xsl = new StreamSource(new File(string));
		Result xmlOutput = new StreamResult(new File("output.xml"));
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xsl);
		transformer.transform(xmlInput, xmlOutput);
		
	}
	
}
