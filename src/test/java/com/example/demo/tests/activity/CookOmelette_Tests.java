package com.example.demo.tests.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.GregorianCalendar;
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
import com.example.demo.activity.ActivityTypes;
import com.example.demo.activity.exceptions.InvalidEggSizeException;
import com.example.demo.activity.plugins.accessories.Omelette.EggSize;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.custom.ElementTypes;
import com.example.demo.element.custom.Pot;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidRoleException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CookOmelette_Tests {

	//private final long DAY_IN_MS = 24 * 60 * 60 * 1000; 
	@LocalServerPort
	private int port;
	private String url;

	
	private ElementEntity demoEntity;
	private ElementEntity pot;
	private ElementEntity expiredPot;
	private ElementEntity board;
	
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
		this.pot = new ElementEntity("playground_lazar", "1", 0, 0, "uncle bob pot", new Date(), null, ElementTypes.Pot.toString(), null, null, null);
		this.expiredPot = new ElementEntity("playground_lazar", "1", 0, 0, "uncle bob old pot", new Date(), new Date(0), ElementTypes.Pot.toString(), null, null, null);
		this.board = new ElementEntity("playground_lazar", "1", 0, 0, "uncle bob board", new Date(), null, ElementTypes.Board.toString(), null, null, null);
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
	
	// s1
	
	
	
	@Test(expected=ElementNotFoundException.class)
	public void FailedCookOmeletteWithExpiredPot() throws ElementAlreadyExistException, InvalidRoleException, ElementNotFoundException {
		

		this.elementService.addNewElement(this.expiredPot, this.demo_user_manager.getEmail());
		
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", "medium");
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", demo_user_player.getPlayground());
		params.add("email", demo_user_player.getEmail());
		
		ActivityTO activityTo = new ActivityTO();
		activityTo.setType(ActivityTypes.CookOmelette.getActivityName());
		activityTo.setId("1");
		activityTo.setElementId(expiredPot.getId());
		activityTo.setElementPlayground(expiredPot.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		activityTo.setAttributes(map);
		
		try {
			rest.postForObject( url+"/{userPlayground}/{email}", activityTo, ActivityTO.class
					,demo_user_player.getPlayground(), demo_user_player.getEmail());
		}
		catch (Exception e) {
			throw new ElementNotFoundException();
		}
		
	}
	
	
	@Test(expected=InvalidElementForActivityException.class)
	public void FailedCookOmeletteWithOtherElement() throws ElementAlreadyExistException, InvalidRoleException, InvalidElementForActivityException {
		
		this.elementService.addNewElement(this.demoEntity, this.demo_user_manager.getEmail());
		
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", EggSize.Medium);
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("userPlayground", demo_user_player.getPlayground());
		params.add("email", demo_user_player.getEmail());
		
		ActivityTO activityTo = new ActivityTO();
		activityTo.setType(ActivityTypes.CookOmelette.getActivityName());
		activityTo.setId("1");
		activityTo.setElementId(expiredPot.getId());
		activityTo.setElementPlayground(expiredPot.getPlayground());
		activityTo.setPlayerEmail(demo_user_player.getEmail());
		activityTo.setPlayerPlayground(demo_user_player.getPlayground());
		activityTo.setAttributes(map);
		
		try {
			rest.postForObject( url+"/{userPlayground}/{email}", activityTo, ActivityTO.class
					,demo_user_player.getPlayground(), demo_user_player.getEmail());
			
		}
		catch (Exception e) {
			throw new InvalidElementForActivityException();
		}
		
	}
	
	@Test
	public void CookOmeletteSuccessWithEggsizeSmall() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.pot, this.demo_user_manager.getEmail());
		//When
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", EggSize.Small);
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.CookOmelette.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
				
				
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
				
				
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
	}
	
	@Test
	public void CookOmeletteSuccessWithEggsizeMedium() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.pot, this.demo_user_manager.getEmail());
		//When
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", EggSize.Medium);
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.CookOmelette.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
				
				
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
				
				
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
				
	}
	
	@Test
	public void CookOmeletteSuccessWithEggsizeLarge() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.pot, this.demo_user_manager.getEmail());
		//When
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", EggSize.Large);
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.CookOmelette.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
				
				
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
				
				
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
				
	}
	
	@Test
	public void CookOmeletteSuccessWithEggsizeXtraLarge() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.pot, this.demo_user_manager.getEmail());
		//When
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", EggSize.ExtraLarge);
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.CookOmelette.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
				
				
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
				
				
		ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
				, demo_user_player.getPlayground(), demo_user_player.getEmail());
				
	}
	
	
	@Test(expected=InvalidEggSizeException.class)
	public void FailedCookOmeletteWithInvalidSize() throws ElementAlreadyExistException, InvalidRoleException {
		//Given
		this.elementService.addNewElement(this.pot, this.demo_user_manager.getEmail());
		//When
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", "extraSmall");
		ActivityTO activity = new ActivityTO("playground_lazar",  "playground_lazar", "1", ActivityTypes.CookOmelette.getActivityName()
				, demo_user_player.getPlayground(), demo_user_player.getEmail(), map);
				
				
		activity.setElementId(this.demoEntity.getId());
		activity.setElementPlayground(demoEntity.getPlayground());
				
		try {
			ActivityTO result =rest.postForObject( url+"/{userPlayground}/{email}", activity, ActivityTO.class
					, demo_user_player.getPlayground(), demo_user_player.getEmail());
		}
		//Then
		catch (Exception e) {
			throw new InvalidEggSizeException();
		}
	
	}
	
}

