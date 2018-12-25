package com.example.demo.activity;

import org.springframework.data.domain.Pageable;
import java.util.List;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;

public interface ActivityService {
	
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException;
	public List<ActivityEntity> getAllActivitiesByTypes(String type, Pageable pageable);
	public void cleanUp();
}
