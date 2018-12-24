package com.example.demo.tests.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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

import com.example.demo.user.TypesEnumUser.types;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.UserTO;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidConfirmationCodeException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotActivatedException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersTest {

	//TODO must fix the exception test for the correct exceptions~! currently http500 is invoked everytime.
	
	@LocalServerPort
	private int port;
	private String url;
	
	private RestTemplate rest;
	

	@Autowired
	private UserService userService;
	
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
	
	
	///////////////
	// Feature 1 //
	///////////////
	
	// Scenario 1: Test user registration success
	@Test
	public void TestNewUserForm() {
		//When I POST /playground/users
		UserTO testUser = new UserTO("name", "mail@something.com", "avatar.url", types.Player.getType(), false);
		UserTO user = this.rest.postForObject(this.url + "/", testUser ,UserTO.class);
		
	}
	
	
	// Scenario 2: Create a new User form when the user (which means his email) exists in the playground.
	@Test
	public void TestNewUserFormFail() throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException {

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
	
	// Scenario 3: Invalid Role on user creation throws Exception
	@Test(expected=Exception.class) // = status <> 2xx
	public void InvalidRoleThrowsException() {
		//Given...
		
		//When
		UserTO testUser = new UserTO("name", "demo@gmail.com", "avatar.url", "Servant" , false);
		
		UserTO user = this.rest.postForObject(this.url + "/", testUser, UserTO.class);
		//Then: ^Exception^

	}
	
	
	///////////////
	// Feature 2 //
	///////////////
	
	// Scenario 1: User confirmation Success
	@Test
	public void TestUserConfirmationByCode() throws EmailAlreadyRegisteredException, UserNotFoundException, InvalidEmailException, InvalidRoleException {
		//Given
		String testEmail = "demo@gmail.com";
		UserTO testUser = new UserTO("name", testEmail , "avatar.url", types.Player.getType(), false);
		userService.registerNewUser(testUser.ToEntity());
		String code = null;
		try {
			code = userService.getUser(testEmail,"playground_lazar").getCode();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		map.put("code", code);
		
		//When
		UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, map);
		//Then
		assertEquals(userService.getUser(testEmail,"playground_lazar").getCode(), null);
	}

	// Scenario 2: User confirm with wrong code
	@Test(expected=InvalidConfirmationCodeException.class)
	public void TestInvalidCodeThrowsException() throws InvalidConfirmationCodeException {
		//Given
		String testEmail = "demo@gmail.com";
		UserTO testUser = new UserTO("name", testEmail , "avatar.url", types.Player.getType(), false);
		try {
			userService.registerNewUser(testUser.ToEntity());
		} catch (EmailAlreadyRegisteredException | InvalidEmailException | InvalidRoleException e1) {
			e1.printStackTrace();
		}
		String code = "12345"; //since code is a 4-char string, this will always cause an InvalidConfirmationCodeException.
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		map.put("code", code);
		//When
		try
		{
			UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
					UserTO.class, "playground_lazar", "demo@gmail.com", code);
		}
		//Then
		catch (Exception e) {
			//System.out.println(e.getClass());
			throw new InvalidConfirmationCodeException();
		}
	
	}
	
	// Scenario 3: User tried to confirm when the database is empty.
	@Test(expected=Exception.class) //should be UserNotFoundException.class
	public void TestConfirmationWithoutUserCauseException() {
		//Given
		
		//When
		UserTO user = this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}",UserTO.class, "playground_lazar", "demo@gmail.com", "1234");
		//Then: ^Exception^
	}
	
	
	///////////////
	// Feature 3 //
	///////////////
	
	// Scenario 1: Test user log in successfully 
	@Test
	public void TestUserLoginSuccessfully() throws Exception {
		//Given
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		UserTO user = new UserTO("tal", "demo@gmail.com", "anAvatr", types.Manager.getType(), true);
		userService.registerNewUser(user.ToEntity());
		
		//System.err.println(userServices.getAllUsers(5, 1));

		//When
		UserEntity actual = this.rest.getForObject(this.url + "/login/{playground}/{email}",
				UserEntity.class, map);
		
		//Then
		assertEquals(user.getEmail(), actual.getEmail());
		assertEquals(UserEntity.class, actual.getClass());
	}
	
	//Scenario 2: User tries to login with invalid playground.
	@Test(expected=Exception.class)
	public void TestUserTriesToLoginWithInvalidPlayground() throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException {
		//Given
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_tomy");
		map.put("email", "demo@gmail.com");
		UserTO user = new UserTO("tal", "demo@gmail.com", "anAvatr", types.Manager.getType(), true);
		userService.registerNewUser(user.ToEntity());
		//When
		UserEntity actual = this.rest.getForObject(this.url + "/login/{playground}/{email}",
				UserEntity.class, map);
		//Then ^Exception^
	}
	
	//Scenario 3: User tries to login with invalid email
	@Test(expected=Exception.class) // should be InvalidEmailException
	public void TestUserLoginWithInvalidEmail() throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException {
		//Given
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "gogo@gmail.com");
		UserTO user = new UserTO("tal", "demo@gmail.com", "anAvatr", types.Manager.getType(), true);
		userService.registerNewUser(user.ToEntity());
		//When
		UserEntity actual = this.rest.getForObject(this.url + "/login/{playground}/{email}",
				UserEntity.class, map);
		//Then ^Exception^
	}
	
	///////////////
	// Feature 4 //
	///////////////
	
	// Scenario 1: Test update user
	@Test
	public void TestUpdateUserFromDB() throws EmailAlreadyRegisteredException, UserNotFoundException, InvalidEmailException, InvalidRoleException {
		//Given
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "demo@gmail.com");
		UserTO userto = new UserTO("demo", "demo@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userService.registerNewUser(et);
		et.setCode(null);
		userService.updateUserInfo(et);
		userto.setAvatar("CAT");
		//When
		rest.put(url + "/{playground}/{email}", userto, map);
		//Then
		assertEquals(userto.getAvatar(), "CAT");
	}

		
	// Scenario 2: Test that guest cannot update details
	
	@Test(expected=UserNotActivatedException.class)
	public void TestUnactivatedUserThrowsExceptionWhenUpdatingDetails() throws EmailAlreadyRegisteredException, UserNotFoundException, UserNotActivatedException, InvalidEmailException, InvalidRoleException {
		//Given
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "liran@gmail.com");
		UserTO userto = new UserTO("liranzxc", "liran@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userService.registerNewUser(et);
		et.setCode("C0D3");
		userService.updateUserInfo(et);
		userto.setAvatar("CAT");
		//When
		try {
			rest.put(url + "/{playground}/{email}", userto, map);
		}catch(Exception e) {
		//Then
			//System.out.println(e.getClass());
			throw new UserNotActivatedException();
		}
	}
	
	//Scenario 3: Update a user when the user is not exist in databse 
	@Test(expected=Exception.class)
	public void TestUnexistedUserThrowsException() throws EmailAlreadyRegisteredException, UserNotFoundException, InvalidEmailException, InvalidRoleException {
		//Given
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "playground_lazar");
		map.put("email", "gogo@gmail.com");
		UserTO userto = new UserTO("demo", "demo@gmail.com", "DOG", types.Manager.getType(), true);
		UserEntity et = userto.ToEntity();
		userService.registerNewUser(et);
		et.setCode(null);
		userService.updateUserInfo(et);
		userto.setAvatar("CAT");
		//When
		rest.put(url + "/{playground}/{email}", userto, map);
		//Then ^Exception^

	}

}
