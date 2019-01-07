package com.example.demo.activity;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.exceptions.InvalidActivityAtributeException;
import com.example.demo.activity.exceptions.InvalidActivityTypeException;
import com.example.demo.activity.plugins.PlaygroundPlugin;
import com.example.demo.aop.EmailValue;
import com.example.demo.aop.ToLog;
import com.example.demo.aop.UserPermission;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementRepository;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository dataBase;
	private ApplicationContext spring;
	private ObjectMapper jackson;
	private static int Id = 0;
	
//	@Autowired
//	public ActivityServiceImpl(ActivityRepository dataBase, ApplicationContext spring) {
//		this.dataBase = dataBase;
//		this.spring = spring;
//		this.jackson = new ObjectMapper();
//	}
	
	@Autowired
	public void setDataBase(ActivityRepository dataBase) {
		this.dataBase = dataBase;
	}
	
	@Autowired
	public void setApplicationContext(ApplicationContext spring) {
		this.spring = spring;
	}
	
	@Autowired
	public void setObjectMapper(ObjectMapper jackson) {
		this.jackson = jackson;
	}
	

	
	@Override
	@Transactional
	@UserPermission
	@ToLog
	public void addNewActivity(ActivityEntity activityEntity, @EmailValue String email) 
			throws ActivityAlreadyExistException, InvalidRoleException, InvalidActivityTypeException, InvalidActivityAtributeException {
		// Check if email is OK by role
		// Aspect @UserPermission inject the role to 'email' var
		String role = email;
		if(!role.equals("Player")) {
			System.out.println("addNewActivity - the user is not player!");
			throw new InvalidRoleException("only player can do activities!");
		}
		
		System.err.println("add new ativity - after user email validation");
		
		activityEntity.setKey(ActivityEntity.generateKey("playground_lazar", ""+Id++));
		String key = activityEntity.getKey();
		if(!this.dataBase.existsByKey(key)) {
			if (!activityEntity.getType().isEmpty()) {
				
				Class<?> theClass;
				try {
					String type = activityEntity.getType();
					String className = "com.example.demo.activity.plugins." + type + "Plugin";

					theClass = Class.forName(className);
					
					
					
				} catch (Exception e) {
					throw new InvalidActivityTypeException(activityEntity.getType() + " is an invalid activity type"); //cause the user to get http 500 error
				}
				
				try {
					PlaygroundPlugin plugin = (PlaygroundPlugin) this.spring.getBean(theClass);
					Object activity = plugin.invokeOperation(activityEntity);
					
					Map<String, Object> rvMap = this.jackson.readValue(
							this.jackson.writeValueAsString(activity),
							Map.class);
					activityEntity.getAttributes().putAll(rvMap);
				} catch (Exception e) {
					throw new InvalidActivityAtributeException("Invalid attributes");
				}
			}
			this.dataBase.save(activityEntity);
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
