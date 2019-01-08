package com.example.demo.tests.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

import com.example.demo.activity.ActivityTypes;
import com.example.demo.activity.ActivityService;
import com.example.demo.activity.ActivityTO;
import com.example.demo.activity.ActivityTypes;
import com.example.demo.activity.exceptions.InvalidActivityAtributeException;
import com.example.demo.activity.exceptions.InvalidActivityTypeException;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.custom.ElementTypes;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostMessage_Tests  {

	@LocalServerPort
	private int port;
	private String url;

	
	private ElementEntity demoEntity;
	private ElementEntity board;
	private ElementEntity pot;
	
	
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
		this.board = new ElementEntity("playground_lazar", "1", 0, 0, "uncle bob board", new Date(), null, ElementTypes.Board.toString(), null, null, null);
		this.pot = new ElementEntity("playground_lazar", "1", 0, 0, "uncle bob pot", new Date(), null, ElementTypes.Pot.toString(), null, null, null);

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
	public void SuccessfulPostMessageOnBoard() throws ElementAlreadyExistException, InvalidRoleException, UserNotFoundException {
		//Given
		this.elementService.addNewElement(this.board, this.demo_user_manager.getEmail());
		Long oldPoints = this.userService.getUser(this.demo_user_player.getEmail(), this.demo_user_player.getPlayground()).getPoints();
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", "Tal");
		map.put("message", "This is a test");
		ActivityTO activityTo = new ActivityTO("playground_lazar", "playground_lazar", "1", 
				ActivityTypes.BoardPost.getActivityName(), "playground_lazar", demo_user_player.getEmail(), map);
		
		activityTo.setElementId(this.demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		
		//When
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activityTo, 
				ActivityTO.class, demo_user_player.getPlayground(), demo_user_player.getEmail());
		
		Long newPoints = this.userService.getUser(this.demo_user_player.getEmail(), this.demo_user_player.getPlayground()).getPoints();

		System.err.println(result.getAttributes());
		
		System.err.println("Old points: " + oldPoints);
		System.err.println("New points: " + newPoints);
		assertTrue(newPoints != oldPoints);
	}
	
	
	@Test(expected=InvalidActivityAtributeException.class)
	public void FailedPostMessageOnSomethingThatNotBoard() throws ElementAlreadyExistException, InvalidRoleException, UserNotFoundException, InvalidActivityAtributeException {
		//Given
		this.elementService.addNewElement(this.pot, this.demo_user_manager.getEmail());
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", "Tal");
		map.put("message", "This is a test");
		ActivityTO activityTo = new ActivityTO("playground_lazar", "playground_lazar", "1", 
				ActivityTypes.BoardPost.getActivityName(), "playground_lazar", "asdfsd", map);
		
		activityTo.setElementId(this.demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		
		//When
		try {
			rest.postForObject( url+"/{userPlayground}/{email}", activityTo, 
					ActivityTO.class, demo_user_player.getPlayground(), demo_user_player.getEmail());
		} catch (Exception e) {
			throw new InvalidActivityAtributeException();
		}
	}
	
	
	@Test
	public void FailedPostMessageWithInvalidType() throws ElementAlreadyExistException, InvalidRoleException, UserNotFoundException {
		//Given
		
		this.elementService.addNewElement(this.board, this.demo_user_manager.getEmail());
		Long oldPoints = this.userService.getUser(this.demo_user_player.getEmail(), this.demo_user_player.getPlayground()).getPoints();
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", "Tal");
		map.put("message", "This is a test");
		String wrongType = ActivityTypes.BoardRead.getActivityName();
		
		ActivityTO activityTo = new ActivityTO("playground_lazar", "playground_lazar", "1", 
				wrongType, "playground_lazar", demo_user_player.getEmail(), map);
		
		activityTo.setElementId(this.demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		
		//When
		boolean success = false;
		try {
			rest.postForObject( url+"/{userPlayground}/{email}", activityTo, 
					ActivityTO.class, demo_user_player.getPlayground(), demo_user_player.getEmail());
		}
		catch (Throwable e) {
			success = true;
		}
		
		assertTrue(success);
	}
	
	
	@Test(expected=Exception.class)
	public void FailedPostMessageWithInvalidAttributes() throws ElementAlreadyExistException, InvalidRoleException, UserNotFoundException {
		//Given
		this.elementService.addNewElement(this.board, this.demo_user_manager.getEmail());
		Map <String,Object> map = new HashMap<String,Object>();
		
		String wrongAttribute = "name";
		map.put(wrongAttribute, "Tal");
		map.put("message", "This is a test");
		ActivityTO activityTo = new ActivityTO("playground_lazar", "playground_lazar", "1", 
				ActivityTypes.BoardPost.getActivityName(), "playground_lazar", demo_user_player.getEmail(), map);
		
		activityTo.setElementId(this.demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		
		//When
		
		rest.postForObject( url+"/{userPlayground}/{email}", activityTo, 
				ActivityTO.class, demo_user_player.getPlayground(), demo_user_player.getEmail());
		
	
		
	}
	
}
