package com.example.demo.activity;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.plugins.PlaygroundPlugin;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository dataBase;
	private ApplicationContext spring;
	private ObjectMapper jackson;
	
	@Autowired
	public void setDataBase(ActivityRepository dataBase) {
		this.dataBase = dataBase;
	}
	
	
	@Override
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException {
		String id = entity.getId();
		if(!this.dataBase.existsById(id)) {
			if (!entity.getType().isEmpty()) {
				try {
					String type = entity.getType();
					String className = "com.example.demo.activity.plugins." + type + "Plugin";
					Class<?> theClass = Class.forName(className);
					PlaygroundPlugin plugin = (PlaygroundPlugin) this.spring.getBean(theClass);
					Object activity = plugin.invokeOperation(entity);
					
					@SuppressWarnings("unchecked")
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
	public void removeActivity(String activityID) {
		if(this.dataBase.existsById(activityID)) {
			this.dataBase.deleteById(activityID);
		}
	}

}
