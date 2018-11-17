package com.example.demo.UsersTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.classes.EntityClasses.UserEntity;
import com.example.demo.classes.ToClasses.ActivityTO;
import com.example.demo.classes.ToClasses.UserTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersTest {

	@LocalServerPort
	private int port;
	private String url;

	private enum types {

		Player("player"), Manager("manger");
		// declaring private variable for getting values
		private String action;

		// getter method
		public String getAction() {
			return this.action;
		}

		// enum constructor - cannot be public or protected
		private types(String action) {
			this.action = action;
		}

	};

	RestTemplate rest = new RestTemplate();

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/playground/users";
		System.err.println(this.url);
	}

	// 4. test update user
	@Test
	public void TestUpdateUserFromDB() {

		url = url + "/{playground}/{email}";

		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", "play");
		map.put("email", "lirannh@gmail.com");

		UserTo userto = new UserTo("liranzxc", "lirannh@gmail.com", "DOG", types.Manager.getAction());

		rest.put(url, userto, map);

	}

}
