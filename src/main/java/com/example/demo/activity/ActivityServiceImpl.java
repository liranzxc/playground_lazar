package com.example.demo.activity;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.plugins.PlaygroundPlugin;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ActivityServiceImpl implements ActivityService {

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


	
	@Override
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException {
		entity.setKey(ActivityEntity.generateKey("playground_lazar", ""+Id++));
		String key = entity.getKey();
		if(!this.dataBase.existsByKey(key)) {
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
