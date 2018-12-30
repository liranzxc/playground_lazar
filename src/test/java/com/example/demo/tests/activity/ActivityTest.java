package com.example.demo.tests.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.activity.ActivityEnumTypes.Activities;
import com.example.demo.activity.ActivityService;
import com.example.demo.activity.ActivityTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActivityTest {

	@LocalServerPort
	private int port;
	private String url;

	
	RestTemplate rest = new RestTemplate();

	@Autowired
	private ActivityService service;
	
	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/playground/activites";
		//System.err.println(this.url);
	}
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void teardown() {
		service.cleanUp();
	}

<<<<<<< HEAD
=======
	// Feature 11
//	@Test
//	public void Test_Send_Activity_To_Do_Something_and_return_Some_Object() {
//		
//		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
//		params.add("userPlayground", "playground_lazar");
//		params.add("email", "demo@gmail.com");
//		
//		ActivityTO activtyTo = new ActivityTO();
//		
//		activtyTo.setType("transport"); // add type
//		
//		activtyTo.setId("1");
//		Object result =rest.postForObject( url+"/{userPlayground}/{email}", activtyTo, ActivityTO.class,params);
//
//		ActivityTO actual = ActivityTO.class.cast(result);
//
//		assertThat(actual.getId(),equalTo("1"));
//
//	}
>>>>>>> 5114fe0a8215c8c383b23ddc1ba11e5deca9999f

	@Test
	public void EchoActivity() {
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("Attribute", "Test");
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		
		ActivityTO activityTo = new ActivityTO();
		activityTo.setType("");
		activityTo.setId("1");
		activityTo.setAttributes(map);
		Object result =rest.postForObject( url+"/{userPlayground}/{email}", activityTo, ActivityTO.class,params);

		ActivityTO actual = ActivityTO.class.cast(result);

		assertThat(actual.getId(),equalTo("1"));
		assertThat(actual.getAttributes().get("Attribute"), equalTo("Test"));

	}
<<<<<<< HEAD
=======

>>>>>>> 5114fe0a8215c8c383b23ddc1ba11e5deca9999f
	
	
	@Test
	public void AddBoardPostActivity() {
		//Given
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", "Tal");
		map.put("message", "This is a test");
		ActivityTO activity = new ActivityTO("playground_lazar", "playground_lazar", "1", 
				Activities.BoardPost.getActivityName(), "playground_lazar", "asdfsd", map);
		
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		//When
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, 
				ActivityTO.class, params );
		System.err.println(result.getAttributes());
	}
	
	@Test
	public void ReadTwoMessages() {
		//Given
		//first message
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", "Tal");
		map.put("message", "This is a test");
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.BoardPost.getActivityName() , 
				"playground_lazar", "asdfsd", map);
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class, params );
		
		//second message 
		Map <String,Object> map1 = new HashMap<String,Object>();
		map1.put("poster", "Human");
		map1.put("message", "Generic message");
		ActivityTO activity1 = new ActivityTO("playground_lazar", "playground_lazar", "1", Activities.BoardPost.getActivityName() , 
				"playground_lazar", "asdfsd", map1);
		MultiValueMap<String, String> params1= new LinkedMultiValueMap<>();
		params1.add("userPlayground", "playground_lazar");
		params1.add("email", "demo@gmail.com");
		ActivityTO result1 =rest.postForObject( url+"/{userPlayground}/{email}", activity1, ActivityTO.class, params1 );
		
		
		//When
		//read message
		Map <String,Object> map2 = new HashMap<String,Object>();
		map2.put("page", 0);
		map2.put("size", 5);
		ActivityTO activity2 = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.BoardRead.getActivityName() , "playground_lazar", 
				"asdfsd", map2);
		MultiValueMap<String, String> params2= new LinkedMultiValueMap<>();
		params2.add("userPlayground", "playground_lazar");
		params2.add("email", "demo@gmail.com");
		ActivityTO result2 =rest.postForObject( url+"/{userPlayground}/{email}", activity2, ActivityTO.class, params2 );
		//Then: should see the messages on console.
		System.out.println(result2.getAttributes());
		
	}
	
	@Test(expected = Exception.class) //status <> 2xx
	public void ThrowWhenTypeIsNotExist() {
		//Given
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("attribute1", "Tal");
		map.put("attribute2", "This is a test");
		ActivityTO activity = new ActivityTO("playground_lazar", "playground_lazar", "1", "FinishTheProjectForUs" , 
				"playground_lazar", "asdfsd", map);
		
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		//When
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class, params );
		//Then ^ThrowsException^
	}
	
	@Test(expected = Exception.class) //status <> 2xx
	public void ThrowWhenAttributesAreInvalid() {
		//Given
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("attribute1", "Tal");
		map.put("attribute2", "This is a test");
		ActivityTO activity = new ActivityTO("playground_lazar", "playground_lazar", "1", Activities.BoardPost.toString() /*must be a valid type*/ , 
				"playground_lazar", "asdfsd", map);
		
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		//When
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class, params );
		//Then ^ThrowsException^
	}
	
	@Test
	public void ReadBoardWhenEmpty() {
		//Given
		//When
		Map <String,Object> map2 = new HashMap<String,Object>();
		map2.put("page", 0);
		ActivityTO activity2 = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.BoardRead.getActivityName() , 
				"playground_lazar", "asdfsd", map2);
		MultiValueMap<String, String> params2= new LinkedMultiValueMap<>();
		params2.add("userPlayground", "playground_lazar");
		params2.add("email", "demo@gmail.com");
		Object result =rest.postForObject( url+"/{userPlayground}/{email}", activity2, ActivityTO.class, params2 );
		//Then Console prints an empty list.
	}
	
	@Test
	public void TestCookOmelete() {
		//Given
		//When
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", "medium");
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.CookOmelette.getActivityName() , 
				"playground_lazar", "asdfsd", map);
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class, params );
		
	}
	
	@Test
	public void TestOmeletteEggSizes() {
		//Given
		//When
		Map <String,Object> smallMap = new HashMap<String,Object>();
		Map <String,Object> mediumMap = new HashMap<String,Object>();
		Map <String,Object> largeMap = new HashMap<String,Object>();
		Map <String,Object> xlargeMap = new HashMap<String,Object>();
		smallMap.put("eggSize", "small");
		mediumMap.put("eggSize", "medium");
		largeMap.put("eggSize", "large");
		xlargeMap.put("eggSize", "extraLarge");
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.CookOmelette.getActivityName() , 
				"playground_lazar", "asdfsd", smallMap);
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class, params );
		System.err.println(result.getAttributes());
		
		ActivityTO activity2 = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.CookOmelette.getActivityName() , 
				"playground_lazar", "asdfsd", mediumMap);
		ActivityTO result2 =rest.postForObject( url+"/{userPlayground}/{email}", activity2, ActivityTO.class, params );
		System.err.println(result2.getAttributes());
		
		ActivityTO activity3 = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.CookOmelette.getActivityName() , 
				"playground_lazar", "asdfsd", largeMap);
		ActivityTO result3 =rest.postForObject( url+"/{userPlayground}/{email}", activity3, ActivityTO.class, params );
		System.err.println(result3.getAttributes());
		
		ActivityTO activity4 = new ActivityTO("playground_lazar",  "playground_lazar", "1", Activities.CookOmelette.getActivityName() , 
				"playground_lazar", "asdfsd", xlargeMap);
		ActivityTO result4 =rest.postForObject( url+"/{userPlayground}/{email}", activity4, ActivityTO.class, params );
		System.err.println(result4.getAttributes());
	}
	
}
