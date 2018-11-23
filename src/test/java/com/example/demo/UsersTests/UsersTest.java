package com.example.demo.UsersTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import com.example.demo.contollers.UsersController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersTest {

	@LocalServerPort
	private int port;
	private String code;
	private String url;
	
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

	RestTemplate rest = new RestTemplate();

	@Autowired
	private UsersController userController = new UsersController();
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/playground/users";
		System.err.println(this.url);
		this.code = userController.getTEST_CODE();
	}

	// 1. Test user registration
	@Test
	public void TestNewUserForm() {
		//When I POST /playground/users
		UserTO testUser = new UserTO("name", "mail@something.com", "avatar.url", types.Player.getType());
		UserEntity user = userController.registerFromForm(testUser);
//		Then the response is: 
//		{
//			"email": any string,
//			"playground": any string,
//			"username": any string,
//			"avatar": any string,
//			"role": any string,
//			"points":"0"
//		}

	}
	
	// 2. Test user confirmation
	@Test
	public void TestUserConfirmationByCode() {
//		When I GET /playground/users/confirm with:
//		{ 
//			"playground":"playground_lazar",
//			"email": any string,
//			"code": any reasonable code from digits 
//		}
//		with headers:
//			Content-Type: application/json
		url = url + "/confirm/{playground}/{email}/{code}";
		Map<String, String> map = new HashMap<String,String>();
		map.put("playground", "playground_lazar");
		map.put("email", "talisraeli.t@gmail.com" );
		map.put("code",  code); //currently delivering the RIGHT code.
		
//		Then the response is: 
//		{
//			"email": any string,		
//			"playground":"playground_lazar",
//			"username": any string,
//			"avatar": any string,
//			"role": any string,
//			"points":"0"
//		}
		UserTO user = rest.getForObject(url, UserTO.class, map);
		assertThat(user.getPlayground(),equalTo("playground_lazar"));

	}
	
	// 3. Test user log in successfully 
	@Test
	public void TestUserLoginSuccessfully() {
//		When I GET /playground/users/login with:
//		{ 
//			"playground":"playground_lazar",
//			"email": any string
//		}
//		with headers:
//			Content-Type: application/json
		url = url + "/login/{playground}/{email}";
		Map<String, String> map = new HashMap<String,String>();
		map.put("playground", "playground_lazar");
		map.put("email", "tal@gmail.com");
		
//		Then the response is: 
//		{
//			"email":any string,
//			"playground":"playground_lazar",
//			"username": any string,
//			"avatar": any string,
//			"role": any string,
//			"points": any number equals or higher than 0
//		}
		UserTO user = rest.getForObject(url,  UserTO.class, map);

	}
	
	// 4. Test update user
	@Test
	public void TestUpdateUserFromDB() {

		url = url + "/{playground}/{email}";

		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "play");
		map.put("email", "lirannh@gmail.com");

		UserTO userto = new UserTO("liranzxc", "lirannh@gmail.com", "DOG", types.Manager.getType());

		rest.put(url, userto, map);

	}

}
