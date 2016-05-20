package com.mss.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class JmsMessageListener implements SessionAwareMessageListener {

	@Autowired
	private JmsMessageSender jmsMessageSender;

	String decoderClass = null;
	String destinationQueue = null;
	String xslTransformClass = null;
	String xsltPath = null;
	String output = null;

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		System.out.println("Receive: " + ((TextMessage) message).getText());
		String test = ((TextMessage) message).getText();

		StringToFile file = new StringToFile();
		file.stringToFile(test);

		retrievingHeaderValues();

		decodeInputFile();

		transformInputFile();

		try {
			output = new String(Files.readAllBytes(Paths.get("output.xml")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(destinationQueue+"-----------");
		Queue queue = new ActiveMQQueue(destinationQueue);

		jmsMessageSender.send(queue, output);

	}

	public void retrievingHeaderValues() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document document = documentBuilder.parse(new File("input.xml"));
			Node node = document.getDocumentElement();

			NodeList nodeList = node.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node childNode = nodeList.item(i);

				NodeList nodeList2 = childNode.getChildNodes();

				for (int j = 0; j < nodeList2.getLength(); j++) {

					Node childNodesOfHeader = nodeList2.item(j);

					if (childNodesOfHeader.getNodeName() == "decoderClass")
						decoderClass = childNodesOfHeader.getTextContent();

					if (childNodesOfHeader.getNodeName() == "destinationQueue"){
						destinationQueue = childNodesOfHeader.getTextContent();
						System.out.println(destinationQueue);
					}

					if (childNodesOfHeader.getNodeName() == "xslTransformClass")
						xslTransformClass = childNodesOfHeader.getTextContent();

					if (childNodesOfHeader.getNodeName() == "xsltPath") {
						xsltPath = childNodesOfHeader.getTextContent();
						System.out.println("XSLT path : " + xsltPath);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void decodeInputFile() {
		try {
			Class cls = Class.forName(decoderClass);
			Object obj = cls.newInstance();

			Class[] param = new Class[1];
			param[0] = String.class;

			Method method = cls.getDeclaredMethod("decoder", param);
			method.invoke(obj, new String("input.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void transformInputFile() {
		try {
			Class xslt = Class.forName(xslTransformClass);
			Object obj1 = xslt.newInstance();

			Class[] param1 = new Class[1];
			param1[0] = String.class;

			Method method1 = xslt.getDeclaredMethod("xslTransform", param1);
			method1.invoke(obj1, new String(xsltPath));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}