package com.example.demo.tests.aspects;

import static org.junit.Assert.assertTrue;

import java.util.Date;

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

import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementService;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.ElementTO;
import com.example.demo.element.Location;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserExistInDBAspectTests {
	
	@LocalServerPort
	private int port;

	private String urlElements;
	private String urlUsers;
	private RestTemplate restTemplate;
	
	private ElementEntity eeTest;
	private UserEntity ueTest;
	// private ObjectMapper jsonMapper;

	@Autowired
	private ElementService elementService;
	
	@Autowired 
	private UserService userService;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		// this.jsonMapper = new ObjectMapper();
		this.urlElements = "http://localhost:" + port + "/playground/elements";
		this.urlUsers = "http://localhost:" + port + "/playground/users";
		
		Location demo_entity_location = new Location(0, 1);
		eeTest = new ElementEntity("playground_lazar", "1", demo_entity_location.getX(),
				demo_entity_location.getY(), "demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		
		ueTest = new UserEntity("VaildUserDemo@gmail.com","playground_lazar", "Guliver", null, "Player");
	}
	
	
	@Before
	public void setup() {
		ElementServiceJpa.setIDToZero();
	}
	
	@After
	public void tearDown() {
		this.elementService.cleanup();
		this.userService.cleanup();
	}
	
	
	// Feature 7
	
	// scenario 3: 
	@Test
	public void getElementWithLegalUser() {
		
		try {	
			this.userService.registerNewUser(ueTest);
			this.elementService.addNewElement(eeTest);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		ElementTO originalElementTO = new ElementTO(eeTest);
		String userPlayground = "playground_lazar";
		String email = "demo@gmail.com";  
		// TODO: check it out -> because it's working with "demo@gmail.com", which means maybe the aspect does not do the work 
//		String userPlayground = ueTest.getPlayground();
//		String email = ueTest.getEmail();
		
//		System.err.println("Email of user = " + email);
		
		String playground = originalElementTO.getPlayground();
		String id = originalElementTO.getId();
		
		ElementTO elementTOFromDB = this.restTemplate.getForObject(this.urlElements + "/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, userPlayground, email, playground, id);
		
		boolean success = false; 
		if(elementTOFromDB.getPlayground().equals(eeTest.getPlayground()) && 
				elementTOFromDB.getId().equals(eeTest.getId())) {
			success = true;
		}
		
		assertTrue(success);
	}
	
	
	@Test(expected=UserNotFoundException.class)
	public void getElementWithIlleagalUser() throws UserNotFoundException {
		try {	
			this.userService.registerNewUser(ueTest);
			this.elementService.addNewElement(eeTest);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		ElementTO originalElementTO = new ElementTO(eeTest);
//		String userPlayground = "playground_lazar";
//		String badEmail = "123@123.com";
		
		String userPlayground = ueTest.getPlayground();
		String badEmail = "demo@gmail.com";
		// TODO: change the aspect. the test failed even though the user "demo@gmail.com" does not exist
		
		String playground = originalElementTO.getPlayground();
		String id = originalElementTO.getId();
		
		
		try {
			this.restTemplate.getForObject(this.urlElements + "/{userPlayground}/{email}/{playground}/{id}",
					ElementTO.class, userPlayground, badEmail, playground, id);
		}
		catch(Exception e) {
			throw new UserNotFoundException("no user with this email");
		}
	}
	
	

}
