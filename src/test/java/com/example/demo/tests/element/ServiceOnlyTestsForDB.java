package com.example.demo.tests.element;



import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementService;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.Location;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServiceOnlyTestsForDB {
	
	private int numOfEntities = 20;
	private ElementEntity[] demo_entities;
	private ElementEntity demo_entity;

	/*
	 * ====================================== READ ME
	 * =====================================================
	 * 
	 * In this test class i do: - GET
	 * /{userPlayground}/{email}/search/{attributeName}/{value} I/O: --------- |
	 * ElementTO ====================================== READ ME
	 * =====================================================
	 */

	@LocalServerPort
	private int port;

	private String url;

	private RestTemplate restTemplate;
	// private ObjectMapper jsonMapper;

	@Autowired
	private ElementService elementService;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		// this.jsonMapper = new ObjectMapper();
		this.url = "http://localhost:" + port + "/playground/elements";

		Location demo_entity_location = new Location(0, 1);
		this.demo_entity = new ElementEntity("playground_lazar", "0", demo_entity_location.getX(),
				demo_entity_location.getY(), "demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		/*
		 * Create numOfDemoEntities element entities more in array for more tests. we
		 * used sleep method for getting different time-stamps.
		 */
		Location demo_entities_locaiton = new Location();
		demo_entities = new ElementEntity[this.numOfEntities];
		for (int i = 0; i < this.numOfEntities; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.demo_entities[i] = new ElementEntity("playground_lazar", (i + 1) + "", demo_entities_locaiton.getX(),
					demo_entities_locaiton.getY(), "demo", new Date(), null, "demo type", null, "Aviv",
					"demo@gmail.com");
		}
	}

	@Before
	public void setup() throws InterruptedException {
		ElementServiceJpa.setIDToZero();
	}

	@After
	public void teardown() {
		this.elementService.cleanup();
	}

	@Test
	public void findByNameForPlayer() throws ElementAlreadyExistException, InvalidAttributeNameException, InterruptedException, InvalidPageSizeRequestException, InvalidPageRequestException {
		Date date1 = new Date(2050, 1, 1);
		Date date2 = new Date(1000, 1, 1);
		
		Location demo_entity_location = new Location(0, 1);
		ElementEntity demo_entity = new ElementEntity("playground_lazar", "0", demo_entity_location.getX(),
				demo_entity_location.getY(), "demo", new Date(), date1, "demo type", null, "Aviv", "demo@gmail.com");
		
		ElementEntity demo_entity2 = new ElementEntity("playground_lazar", "2", demo_entity_location.getX(),
				demo_entity_location.getY(), "demo", new Date(), date2, "demo type", null, "Shay", "demo@gmail.com");
		
				
		this.elementService.addNewElement(demo_entity);
		this.elementService.addNewElement(demo_entity2);
		
		
		
		
		String attributeToFind = "name";
		String attributeValue = "demo";
		
		
		System.err.println("=========================== find by name ================================");
		List<ElementEntity> list = this.elementService.getAllElementsByAttributeAndValuePlayer(attributeToFind, attributeValue, PageRequest.of(0, 10));
		for (ElementEntity elementEntity : list) {
			System.err.println(elementEntity);
		}
		list.clear();
		if(list.isEmpty()) {
			System.err.println("clearing list sucess");
			
		}
		
		System.err.println("=========================== find by type ================================");
		list = this.elementService.getAllElementsByAttributeAndValuePlayer("type", "demo type",  PageRequest.of(0, 10));
		for (ElementEntity elementEntity : list) {
			System.err.println(elementEntity);
		}
		
	}
	
}
