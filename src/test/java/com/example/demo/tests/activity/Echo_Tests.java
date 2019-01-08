package com.example.demo.tests.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


import java.util.Date;
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


import com.example.demo.activity.ActivityService;
import com.example.demo.activity.ActivityTO;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidRoleException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Echo_Tests {

	@LocalServerPort
	private int port;
	private String url;

	
	private ElementEntity demoEntity;

	
	
	RestTemplate rest = new RestTemplate();

	@Autowired
	private ActivityService service;
	
	
	@Autowired
	private ElementServiceJpa elementService;
	
	@Autowired
	private UserService userService;
	

	private UserEntity demo_user_player;
	private UserEntity demo_user_manager;
	
	
	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/playground/activites";
		//System.err.println(this.url);
		
		this.demo_user_manager = new UserEntity("demoManager@gmail.com", "playground_lazar", "mr.manajer", null, "Manager");
		this.demo_user_player = new UserEntity("demoPlayer@gmail.com", "playground_lazar", "mr.palayer", null, "Player");
		
		
		this.demoEntity = new ElementEntity("playground_lazar", "1", 0, 0, "postBoard", new Date(), null, "postBoard", null, null, null);
	}
	
	@Before
	public void setup() {
		ElementServiceJpa.setIDToZero(); // reset the ID to 0 after each test
		
		try {
			this.userService.registerNewUser(demo_user_manager);
			this.userService.registerNewUser(demo_user_player);
		} catch (Exception e) {
			System.err.println("ElementTest setup exception on registering users, exception is:");
			System.err.println(e.getMessage());
		}		
	}
	
	@After
	public void teardown() {
		service.cleanUp();
		this.elementService.cleanup();
		this.userService.cleanup();
	}


	@Test
	public void SuccessEchoActivityByPlayer() throws ElementAlreadyExistException, InvalidRoleException {
		
		this.elementService.addNewElement(demoEntity, this.demo_user_manager.getEmail());
		
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("Attribute", "Test");
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", demo_user_player.getPlayground());
		params.add("email", demo_user_player.getEmail());
		
		ActivityTO activityTo = new ActivityTO();
		activityTo.setType("");
		activityTo.setId("1");
		activityTo.setElementId(demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		activityTo.setAttributes(map);
		
		Object result =rest.postForObject( url+"/{userPlayground}/{email}", activityTo, ActivityTO.class
				,demo_user_player.getPlayground(), demo_user_player.getEmail());

		ActivityTO actual = ActivityTO.class.cast(result);

		assertThat(actual.getId(),equalTo("1"));
		assertThat(actual.getAttributes().get("Attribute"), equalTo("Test"));
	}
	
	
	@Test(expected=InvalidRoleException.class)
	public void FailedEchoActivityByManager() throws ElementAlreadyExistException, InvalidRoleException {
		
		this.elementService.addNewElement(demoEntity, this.demo_user_manager.getEmail());
		
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("Attribute", "Test");
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", demo_user_manager.getPlayground());
		params.add("email", demo_user_manager.getEmail());
		
		ActivityTO activityTo = new ActivityTO();
		activityTo.setType("");
		activityTo.setId("1");
		activityTo.setElementId(this.demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		activityTo.setAttributes(map);
		
		try {
			rest.postForObject( url+"/{userPlayground}/{email}", activityTo, ActivityTO.class, 
					demo_user_manager.getPlayground(), demo_user_manager.getEmail());
		}
		catch (Exception e) {
			throw new InvalidRoleException("Invalid role of user");
		}
		
	}
	
}
