package com.mss.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoMain {
	
	@Autowired
	private JmsMessageSender jmsMessageSender;
	
	@RequestMapping(value="/welcome")
	public String welcome() throws IOException{
		final String con = new String(Files.readAllBytes(Paths.get("input.xml")));
		jmsMessageSender.send(con);	
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
        e.printStackTrace();
		}
		return "success";
	}
	
}