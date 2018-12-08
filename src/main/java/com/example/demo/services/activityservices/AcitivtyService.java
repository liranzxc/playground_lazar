package com.example.demo.services.activityservices;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.classes.entities.ActivityEntity;
import com.example.demo.classes.exceptions.ActivityAlreadyExistException;
import com.example.demo.repository.IActivityRepository;

public class AcitivtyService implements IActivityService {

	@Autowired
	private IActivityRepository dataBase;
	
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
