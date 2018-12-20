package com.example.demo.activity;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;

public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityRepository dataBase;
	
	@Override
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException {
		String id = entity.getId();
		if(!this.dataBase.existsById(id)) {
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
