package com.example.demo.ElementTests;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.example.demo.classes.ToClasses.ElementTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTestAviv {

	
	@LocalServerPort
	private int port;
	private String url;

	RestTemplate rest = new RestTemplate();

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/playground/elements";
		System.err.println(this.url);
	}
		
	
	@Test
	public void GoToSite()
	{
		
	}
}
