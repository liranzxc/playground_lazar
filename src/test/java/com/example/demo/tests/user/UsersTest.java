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
import com.example.demo.classes.to.UserTO;
import com.example.demo.classes.entities.UserEntity;
import com.example.demo.services.userservices.IUserService;
import com.example.demo.services.userservices.UserServiceDummy;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersTest {

	@LocalServerPort
	private int port;
	private String code;
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
		this.code ="123";

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
	public void TestUserConfirmationByCode() throws EmailAlreadyRegisteredException {
		this.code = "Code";
		UserTO testUser = new UserTO("name", "mail@something.com", "avatar.url", types.Player.getType(), false);
		userServices.registerNewUser(testUser.ToEntity());
		
		UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, "playground_lazar", "address@mail.end", code);

	}

	// 2.b Test Code exception
	@Test(expected=InvalidConfirmationCodeException.class)
	public void TestInvalidCodeThrowsException() throws InvalidConfirmationCodeException {
		
		this.code = "222";
		try
		{
			UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
					UserTO.class, "playground_lazar", "address@mail.end", code);
		}
		catch (Exception e) {
			throw new InvalidConfirmationCodeException();
		}
	
	}
	
	// 3. Test user log in successfully 
	@Test
	public void TestUserLoginSuccessfully() throws Exception {
		System.out.println("test login");
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
	
	// 4. Test update user
	@Test
	public void TestUpdateUserFromDB() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "play");
		map.put("email", "lirannh@gmail.com");

		UserTO userto = new UserTO("liranzxc", "lirannh@gmail.com", "DOG", types.Manager.getType(), false);

		rest.put(url + "/{playground}/{email}", userto, map);
		
		// put method return void , so how we can do Assert? 

	}

}
