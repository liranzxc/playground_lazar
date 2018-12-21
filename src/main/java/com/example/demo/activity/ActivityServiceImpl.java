package com.example.demo.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;

@Service
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository dataBase;
	
	@Autowired
	public void setDataBase(ActivityRepository dataBase) {
		this.dataBase = dataBase;
	}
	
	
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
