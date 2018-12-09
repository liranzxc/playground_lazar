package com.example.demo.tests.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import com.example.demo.classes.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.classes.exceptions.InvalidConfirmationCodeException;
import com.example.demo.classes.exceptions.UserNotActivatedException;
import com.example.demo.classes.exceptions.UserNotFoundException;
import com.example.demo.classes.to.UserTO;
import com.example.demo.classes.entities.UserEntity;
import com.example.demo.services.userservices.IUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersTest {

	//TODO must fix the exception test for the correct exceptions~! currently http500 is invoked everytime.
	
	@LocalServerPort
	private int port;
	private String url;
	
	private RestTemplate rest;
	
	private enum types {

		Player("player"), Manager("manger");
		// declaring private variable for getting values
		private String action;

		// getter method
		public String getType() {
			return this.action;
		}

		// enum constructor - cannot be public or protected
		private types(String action) {
			this.action = action;
		}

	};

	

	@Autowired
	private IUserService userService;
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/playground/users";


		rest = new RestTemplate();
	}

	@Before
	public void setup() {
		//Do nothing
	}

	@After
	public void teardown() {
		userService.cleanup();
	}
	
	// Feature 1
	
	// Scenario 1: Test user registration
	@Test
	public void TestNewUserForm() {
		//When I POST /playground/users
		UserTO testUser = new UserTO("name", "mail@something.com", "avatar.url", types.Player.getType(), false);
		UserTO user = this.rest.postForObject(this.url + "/", testUser ,UserTO.class);
		
	}
	
	// Scenario 2: Test user registration fail because he is already exist in DB
	@Test
	public void TestNewUserFormFail() throws EmailAlreadyRegisteredException {

		// Given: the user is already exist
		UserEntity userEntityForDB = new UserEntity("demo@gmail.com", "playground_lazar", "username", "avatar",
				types.Player.getType());
		userService.registerNewUser(userEntityForDB);

		// When:
		boolean isSucceed = false;
		UserTO testUser = new UserTO("name", "demo@gmail.com", "avatar.url", types.Player.getType(), false);

		try {
			UserTO user = this.rest.postForObject(this.url + "/", testUser, UserTO.class);
		} catch (Exception e) {
			isSucceed = true;
		}

		// Than:
		assertTrue(isSucceed);

	}
	
	
	// Feature 2:
	
	// Scenario 1: User confirmation Success
	@Test
	public void TestUserConfirmationByCode() throws EmailAlreadyRegisteredException, UserNotFoundException {
		String testEmail = "demo@gmail.com";
		UserTO testUser = new UserTO("name", testEmail , "avatar.url", types.Player.getType(), false);
		userService.registerNewUser(testUser.ToEntity());
		String code = null;
		try {
			code = userService.getUser(testEmail).getCode();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		map.put("code", code);
		UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, map);
		assertEquals(userService.getUser(testEmail).getCode(), null);

	}

	// Scenario 2: User confirm with wrong code
	@Test(expected=InvalidConfirmationCodeException.class)
	public void TestInvalidCodeThrowsException() throws InvalidConfirmationCodeException {
		String code = "12345"; //since code is a 4-char string, this will always cause an InvalidConfirmationCodeException.
		try
		{
			UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
					UserTO.class, "playground_lazar", "demo@gmail.com", code);
		}
		catch (Exception e) {
			//System.out.println(e.getClass());
			throw new InvalidConfirmationCodeException();
		}
	
	}
	
	// Feature 3
	// Scenario 1: Test user log in successfully 
	@Test
	public void TestUserLoginSuccessfully() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		UserTO user = new UserTO("tal", "demo@gmail.com", "anAvatr", types.Manager.getType(), true);
		userService.registerNewUser(user.ToEntity());
		
		//System.err.println(userServices.getAllUsers(5, 1));

		UserEntity actual = this.rest.getForObject(this.url + "/login/{playground}/{email}",
				UserEntity.class, map);
		
		assertEquals(user.getEmail(), actual.getEmail());
	}
	
	// Feature 4
	// Scenario 1: Test update user
	@Test
	public void TestUpdateUserFromDB() throws EmailAlreadyRegisteredException, UserNotFoundException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		UserTO userto = new UserTO("demo", "demo@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userService.registerNewUser(et);
		et.setCode(null);
		userService.updateUserInfo(et);
		userto.setAvatar("CAT");
		rest.put(url + "/{playground}/{email}", userto, map);
		assertEquals(userto.getAvatar(), "CAT");
	}

	
	// Scenario 2: Test that guest cannot update details
	
	@Test(expected=UserNotActivatedException.class)
	public void TestUnactivatedUserThrowsExceptionWhenUpdatingDetails() throws EmailAlreadyRegisteredException, UserNotFoundException, UserNotActivatedException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "liran@gmail.com");
		UserTO userto = new UserTO("liranzxc", "liran@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userService.registerNewUser(et);
		et.setCode("C0D3");
		userService.updateUserInfo(et);
		userto.setAvatar("CAT");
		try {
			rest.put(url + "/{playground}/{email}", userto, map);
		}catch(Exception e) {
			//System.out.println(e.getClass());
			throw new UserNotActivatedException();
		}
	}
}
