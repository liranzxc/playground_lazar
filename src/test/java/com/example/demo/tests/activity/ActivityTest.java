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
public class ActivityTest {

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

	// Feature 11


	//s1
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
	

	
	//s2
	@Test
	public void AddBoardPostActivity() throws ElementAlreadyExistException, InvalidRoleException, UserNotFoundException {
		//Given
		this.elementService.addNewElement(this.board, this.demo_user_manager.getEmail());
		Long oldPoints = this.userService.getUser(this.demo_user_player.getEmail(), this.demo_user_player.getPlayground()).getPoints();
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
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activityTo, 
				ActivityTO.class, demo_user_player.getPlayground(), demo_user_player.getEmail());
		
		Long newPoints = this.userService.getUser(this.demo_user_player.getEmail(), this.demo_user_player.getPlayground()).getPoints();

		System.err.println(result.getAttributes());
		
		System.err.println("Old points: " + oldPoints);
		System.err.println("New points: " + newPoints);
		assertTrue(newPoints != oldPoints);
	}
	
	//s3
	@Test
	public void ReadTwoMessages() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.board, this.demo_user_manager.getEmail());

		//first message
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", "Tal");
		map.put("message", "This is a test");
		ActivityTO activityTo = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.BoardPost.getActivityName() , 
				demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
		
		activityTo.setElementId(this.demoEntity.getId());
		activityTo.setElementPlayground(demoEntity.getPlayground());
		

		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activityTo, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
		
		//second message 
		Map <String,Object> map1 = new HashMap<String,Object>();
		map1.put("poster", "Human");
		map1.put("message", "Generic message");
		ActivityTO activityTo1 = new ActivityTO("playground_lazar", "playground_lazar", "1", ActivityTypes.BoardPost.getActivityName() , 
				demo_user_player.getPlayground(), demo_user_player.getEmail(), map1);
		
		
		activityTo1.setElementId(this.demoEntity.getId());
		activityTo1.setElementPlayground(demoEntity.getPlayground());
		
		
		ActivityTO result1 =rest.postForObject( url+"/{userPlayground}/{email}", activityTo1, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail() );
		
		
		//When
		//read message
		Map <String,Object> map2 = new HashMap<String,Object>();
		map2.put("page", 0);
		map2.put("size", 5);
		ActivityTO activity2 = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.BoardRead.getActivityName() 
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map2);
		
		
		activity2.setElementId(this.demoEntity.getId());
		activity2.setElementPlayground(demoEntity.getPlayground());
		
		
		ActivityTO result2 =rest.postForObject( url+"/{userPlayground}/{email}", activity2, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail() );
		//Then: should see the messages on console.
		System.out.println(result2.getAttributes());
	}
	
	// s4
	@Test(expected = InvalidActivityTypeException.class) //status <> 2xx
	public void ThrowWhenTypeIsNotExist() 
			throws ElementAlreadyExistException, InvalidRoleException, InvalidActivityTypeException {
		//Given
		this.elementService.addNewElement(demoEntity, this.demo_user_manager.getEmail());

		Map <String,Object> map = new HashMap<String,Object>();
		map.put("attribute1", "Tal");
		map.put("attribute2", "This is a test");
		ActivityTO activity = new ActivityTO("playground_lazar", "playground_lazar", "1", "FinishTheProjectForUs" 
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
		
		
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
		
		//When
		try {
			rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
					, demo_user_player.getPlayground(), demo_user_player.getEmail());
		}catch (Exception e) {
			throw new InvalidActivityTypeException();
		}
		//Then ^ThrowsException^
	}
	
	// s5
	@Test(expected = Exception.class) //status <> 2xx
	public void ThrowWhenAttributesAreInvalid() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(demoEntity, this.demo_user_manager.getEmail());

		Map <String,Object> map = new HashMap<String,Object>();
		map.put("attribute1", "Tal");
		map.put("attribute2", "This is a test");
		ActivityTO activity = new ActivityTO("playground_lazar", "playground_lazar", "1", ActivityTypes.BoardPost.toString() /*must be a valid type*/ 
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
		
		
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
		
		
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", "playground_lazar");
		params.add("email", "demo@gmail.com");
		//When
		rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
		//Then ^ThrowsException^
	}
	
	@Test
	public void ReadBoardWhenEmpty() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.board, this.demo_user_manager.getEmail());

		//When
		Map <String,Object> map2 = new HashMap<String,Object>();
		map2.put("page", 0);
		ActivityTO activity2 = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.BoardRead.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map2);
		
		activity2.setElementId(this.demoEntity.getId());
		activity2.setElementPlayground(demoEntity.getPlayground());
		
		
		rest.postForObject( url+"/{userPlayground}/{email}", activity2, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
		//Then Console prints an empty list.
		// TODO: check if it's really empty
	}
	
}
