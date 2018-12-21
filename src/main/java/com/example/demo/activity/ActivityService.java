package com.example.demo.activity;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;

public interface ActivityService {
	
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException;
	public void removeActivity(String activityID); 
	
}
