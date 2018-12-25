package com.example.demo.tests.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.activity.ActivityTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActivityTest {

	@LocalServerPort
	private int port;
	private String url;

	
	
	RestTemplate rest = new RestTemplate();

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/playground/activites";
		//System.err.println(this.url);
	}

	// Feature 11
	@Test
	public void Test_Send_Activity_To_Do_Something_and_return_Some_Object() {
		
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		
		ActivityTO activtyTo = new ActivityTO();
		
		activtyTo.setType("transport"); // add type
		
		activtyTo.setId("1");
		Object result =rest.postForObject( url+"/{userPlayground}/{email}", activtyTo, ActivityTO.class,params);

		ActivityTO actual = ActivityTO.class.cast(result);

		assertThat(actual.getId(),equalTo("1"));

	}
	
	
	@Test
	public void AddActivity() {
		//Given
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("op", "tal");
		map.put("message", "this is a test");
		ActivityTO activity = new ActivityTO("playground_lazar", "0", "playground_lazar", "1", "BoardPost" , "playground_lazar", "asdfsd", map);
		
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		//When
		Object result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class, params );
	}
}
