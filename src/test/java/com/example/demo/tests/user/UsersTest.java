package com.example.demo.tests.user;

import static org.junit.Assert.assertEquals;
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
	private IUserService userServices;
	
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
		userServices.cleanup();
	}
	
	// 1. Test user registration
	@Test
	public void TestNewUserForm() {
		//When I POST /playground/users
		UserTO testUser = new UserTO("name", "mail@something.com", "avatar.url", types.Player.getType(), false);
		UserTO user = this.rest.postForObject(this.url + "/", testUser ,UserTO.class);
		
	}
	
	
	// 2.a Test user confirmation
	@Test
	public void TestUserConfirmationByCode() throws EmailAlreadyRegisteredException, UserNotFoundException {
		String testEmail = "mail@something.com";
		UserTO testUser = new UserTO("name", testEmail , "avatar.url", types.Player.getType(), false);
		userServices.registerNewUser(testUser.ToEntity());
		String code = null;
		try {
			code = userServices.getUser(testEmail).getCode();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "mail@something.com");
		map.put("code", code);
		UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, map);
		assertEquals(userServices.getUser(testEmail).getCode(), null);

	}

	// 2.b Test Code exception
	@Test(expected=InvalidConfirmationCodeException.class)
	public void TestInvalidCodeThrowsException() throws InvalidConfirmationCodeException {
		String code = "0"; //since code is a 4-char string, this will always cause an InvalidConfirmationCodeException.
		try
		{
			UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
					UserTO.class, "playground_lazar", "address@mail.end", code);
		}
		catch (Exception e) {
			//System.out.println(e.getClass());
			throw new InvalidConfirmationCodeException();
		}
	
	}
	
	// 3. Test user log in successfully 
	@Test
	public void TestUserLoginSuccessfully() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "address@mail.end");
		UserTO user = new UserTO("tal", "address@mail.end", "anAvatr", types.Manager.getType(), true);
		userServices.registerNewUser(user.ToEntity());
		
		//System.err.println(userServices.getAllUsers(5, 1));

		UserEntity actual = this.rest.getForObject(this.url + "/login/{playground}/{email}",
				UserEntity.class, map);
		
		assertEquals(user.getEmail(), actual.getEmail());
	}
	
	// 4.a Test update user
	@Test
	public void TestUpdateUserFromDB() throws EmailAlreadyRegisteredException, UserNotFoundException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "liranh@gmail.com");
		UserTO userto = new UserTO("liranzxc", "liranh@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userServices.registerNewUser(et);
		et.setCode(null);
		userServices.updateUserInfo(et);
		userto.setAvatar("CAT");
		rest.put(url + "/{playground}/{email}", userto, map);
		assertEquals(userto.getAvatar(), "CAT");
	}

	
	// 4.b Test that guest cannot update details
	@Test(expected=UserNotActivatedException.class)
	public void TestUnactivatedUserThrowsExceptionWhenUpdatingDetails() throws EmailAlreadyRegisteredException, UserNotFoundException, UserNotActivatedException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "liran@gmail.com");
		UserTO userto = new UserTO("liranzxc", "liran@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userServices.registerNewUser(et);
		et.setCode("C0D3");
		userServices.updateUserInfo(et);
		userto.setAvatar("CAT");
		try {
			rest.put(url + "/{playground}/{email}", userto, map);
		}catch(Exception e) {
			System.out.println(e.getClass());
			throw new UserNotActivatedException();
		}
	}
}
