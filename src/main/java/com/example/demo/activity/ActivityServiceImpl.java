package com.example.demo.activity;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.plugins.ActivityHandler;
import com.example.demo.activity.plugins.PlaygroundPlugin;
import com.example.demo.aop.UserPermission;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.TypesEnumUser.Types;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ActivityServiceImpl implements ActivityService {

	private String playground = "${playground.name}";
	
	private ActivityRepository dataBase;
	private ApplicationContext spring;
	private ObjectMapper jackson;
	private static int Id = 0;
	
	@Autowired
	public ActivityServiceImpl(ActivityRepository dataBase, ApplicationContext spring) {
		this.dataBase = dataBase;
		this.spring = spring;
		this.jackson = new ObjectMapper();
	}
	
	private ActivityHandler handler;
	
	@Autowired
	public void setElementVerifyier(ActivityHandler handler) {
		this.handler = handler;
	}
	

	
	@Transactional
	@Override
	// TODO: 
//	@UserPermission(permissions= {Types.Player})
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException, ElementNotFoundException, InvalidRoleException, UserNotFoundException, InvalidElementForActivityException, InvalidEmailException {
		entity.setKey(ActivityEntity.generateKey(playground, ""+Id++));
		String key = entity.getKey();
		if(!this.dataBase.existsByKey(key)) {
			this.handler.handle(entity);
			if (!entity.getType().isEmpty()) {
				try {
					String type = entity.getType();
					String className = "com.example.demo.activity.plugins." + type + "Plugin";

					Class<?> theClass = Class.forName(className);
					PlaygroundPlugin plugin = (PlaygroundPlugin) this.spring.getBean(theClass);
					Object activity = plugin.invokeOperation(entity);
					
					Map<String, Object> rvMap = this.jackson.readValue(
							this.jackson.writeValueAsString(activity),
							Map.class);
					
					entity.getAttributes().putAll(rvMap);
				} catch (Exception e) {
					throw new RuntimeException(); //cause the user to get http 500 error
				}
			}
			this.dataBase.save(entity);
		}
		else {
			throw new ActivityAlreadyExistException("Activity already exists");
		}
		
	}


	@Override
	public List<ActivityEntity> getAllActivitiesByTypes(String type, Pageable pageable) {
		List<ActivityEntity> list = dataBase.findByType(type, pageable);
		return list;
	}



	@Override
	public void cleanUp() {
		this.dataBase.deleteAll();
	}

}
