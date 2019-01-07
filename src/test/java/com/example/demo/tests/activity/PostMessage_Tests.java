package com.example.demo.tests.activity;



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
import org.springframework.web.client.RestTemplate;

import com.example.demo.activity.ActivityService;
import com.example.demo.activity.ActivityTO;
import com.example.demo.activity.ActivityEnumTypes.Activities;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostMessage_Tests {

	@LocalServerPort
	private int port;
	private String url;

	
	private ElementEntity demoEntity;
	
	RestTemplate rest = new RestTemplate();

	@Autowired
	private ActivityService activitySevice;
	
	
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
		activitySevice.cleanUp();
		this.elementService.cleanup();
		this.userService.cleanup();
	}
	
	
	@Test
	public void AddBoardPostActivity() throws ElementAlreadyExistException, InvalidRoleException, UserNotFoundException {
		String poster = "Tal";
		String message = "This is a test";
		long playerPointsBeforePost = demo_user_player.getPoints();
		
		//Given
		this.elementService.addNewElement(demoEntity, this.demo_user_manager.getEmail());

		Map <String,Object> map = new HashMap<String,Object>();
		map.put("poster", poster);
		map.put("message", message);
		ActivityTO activity = new ActivityTO("playground_lazar", demoEntity.getPlayground()
				, demoEntity.getId(), Activities.BoardPost.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
		
		//activity.setElementId(this.demoEntity.getId());
		//activity.setElementPlayground(demoEntity.getPlayground());
		
		//When
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, 
				ActivityTO.class, demo_user_player.getPlayground(), demo_user_player.getEmail());
		
		
		//assertThat((String)result.getAttributes().get("message"), "This is a test");
		assertTrue(((String)result.getAttributes().get("message")) 
				.equals(message));
		assertTrue(((String)result.getAttributes().get("poster")) 
				.equals(poster));
		
		long playerPointsAfterPost = userService.getUser(demo_user_player.getEmail(), demo_user_player.getPlayground()).getPoints();
		System.err.println(playerPointsAfterPost);
		assertTrue(playerPointsAfterPost == playerPointsBeforePost + 10);
	}
	
	
	
	
}
