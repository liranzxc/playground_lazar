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
import com.example.demo.activity.plugins.ActivityHandler;
import com.example.demo.activity.plugins.PlaygroundPlugin;
import com.example.demo.aop.EmailValue;
import com.example.demo.aop.ToLog;
import com.example.demo.aop.UserPermission;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementRepository;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.TypesEnumUser.Types;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository dataBase;
	private ApplicationContext spring;
	private ObjectMapper jackson;
	private UserService userService;
	private static int Id = 0;
	
	private ActivityHandler handler;
	
	@Autowired
	public void setActivityHandler(ActivityHandler handler) {
		this.handler = handler;
	}
	
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
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	

	
	@Override
	@Transactional
	@UserPermission(permissions= {Types.Player})
	@ToLog
	public void addNewActivity(ActivityEntity activityEntity, @EmailValue String email) 
			throws ActivityAlreadyExistException, InvalidRoleException, InvalidActivityTypeException, 
			InvalidActivityAtributeException, UserNotFoundException, InvalidEmailException, ElementNotFoundException, InvalidElementForActivityException {
		
		activityEntity.setKey(ActivityEntity.generateKey("playground_lazar", ""+Id++));
		String key = activityEntity.getKey();
		if(!this.dataBase.existsByKey(key)) {
			if (!activityEntity.getType().isEmpty()) {
				this.handler.handle(activityEntity);
				Class<?> theClass;
				try {
					String type = activityEntity.getType();
					String className = "com.example.demo.activity.plugins." + type + "Plugin";

					theClass = Class.forName(className);
					
				} catch (Exception e) {
					throw new InvalidActivityTypeException(activityEntity.getType() + " is an invalid activity type"); //cause the user to get http 500 error
				}
				
				Object activity;
				try {
					PlaygroundPlugin plugin = (PlaygroundPlugin) this.spring.getBean(theClass);
					activity = plugin.invokeOperation(activityEntity);
					
					Map<String, Object> rvMap = this.jackson.readValue(
							this.jackson.writeValueAsString(activity),
							Map.class);
					activityEntity.getAttributes().putAll(rvMap);
				} catch (Exception e) {
					throw new InvalidActivityAtributeException("Invalid attributes");
				}
				
				
			}
			this.dataBase.save(activityEntity);
			
			// TODO: add activity points to user account - make it better
			System.err.println("Start to add activity points to user account");
			UserEntity userAccount = userService.getUser(activityEntity.getPlayerEmail(), activityEntity.getPlayerPlayground());
			userAccount.setPoints(userAccount.getPoints() + 10);
			userService.updateUserInfo(userAccount);
			
			System.err.println("Inside activity controller: num of points of user is: " 
			+ userService.getUser
			(activityEntity.getPlayerEmail(), activityEntity.getPlayerPlayground())
			.getPoints());
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
