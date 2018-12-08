package com.example.demo.services.activityservices;

import com.example.demo.classes.entities.ActivityEntity;
import com.example.demo.classes.exceptions.ActivityAlreadyExistException;

public interface IActivityService {
	
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException;
	public void removeActivity(String activityID); 
	
}
