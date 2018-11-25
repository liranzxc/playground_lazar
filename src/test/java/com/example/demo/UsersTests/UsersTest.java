package com.example.demo.UsersTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.Application;
import com.example.demo.classes.EntityClasses.UserEntity;
import com.example.demo.classes.ToClasses.ActivityTO;
import com.example.demo.classes.ToClasses.UserTO;
import com.example.demo.classes.exceptions.InvalidCodeException;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.contollers.UsersController;
import com.example.demo.services.UserServiceDummy;

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
	private UserServiceDummy userServices;
	
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
	
	// 2. Test user registration
	@Test
	public void TestNewUserForm() {
		//When I POST /playground/users
		UserTO testUser = new UserTO("name", "mail@something.com", "avatar.url", types.Player.getType());
		UserTO user = this.rest.postForObject(this.url + "/", testUser ,UserTO.class);
		
	}
	
	// 3.a Test user confirmation
	@Test
	public void TestUserConfirmationByCode() {
		this.code = "123";
		UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, "playground_lazar", "address@mail.end", code);

	}

	// 3.b Test Code exception
	@Test(expected=InvalidCodeException.class)
	public void TestInvalidCodeThrowsException() throws InvalidCodeException {
		
		this.code = "222";
		try
		{
			UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
					UserTO.class, "playground_lazar", "address@mail.end", code);
		}
		catch (Exception e) {
			// TODO: handle exception
			
			throw new InvalidCodeException();
		}
	
	}
	
	// 4. Test user log in successfully 
	@Test
	public void TestUserLoginSuccessfully() throws Exception {

		UserTO user = new UserTO("address@mail.end", "playground_lazar", "tal", "anAvatar");
		userServices.registerNewUser(user.ToEntity());
		
		System.err.println(userServices.getAllUsers());
		
		UserEntity actual = this.rest.getForObject(this.url + "/login/{playground}/{email}",
				UserEntity.class, user.getPlayground(),user.getEmail());
		
		System.err.println(actual);
		assertEquals(user.getEmail(), actual.getEmail());
		
		

		
	}
	
	// 5. Test update user
	@Test
	public void TestUpdateUserFromDB() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "play");
		map.put("email", "lirannh@gmail.com");

		UserTO userto = new UserTO("liranzxc", "lirannh@gmail.com", "DOG", types.Manager.getType());

		rest.put(url + "/{playground}/{email}", userto, map);
		
		// put method return void , so how we can do Assert? 

	}

}
