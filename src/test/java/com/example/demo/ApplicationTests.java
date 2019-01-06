package com.example.demo;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

//// Exmaple code !!!!!!!!!!!!!!!!!!!!!


/// ************************
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
	
	@LocalServerPort
	private int port;
	private String url;
	
	@PostConstruct
	public void init() {
		
		this.url = "http://localhost:"+port+"/messages";
		System.err.println(this.url);
	}

	@Test
	public void testGetSpecifiyMessage() throws Exception {
		
		// invoke GET this.url + /"demo"
		
		
		
		// Verify message have details
	}

}
