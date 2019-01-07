package com.example.demo.activity;

import org.springframework.data.domain.Pageable;
import java.util.List;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.exceptions.InvalidActivityAtributeException;
import com.example.demo.activity.exceptions.InvalidActivityTypeException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

public interface ActivityService {
	
	public void addNewActivity(ActivityEntity entity, String email) throws ActivityAlreadyExistException, InvalidRoleException, InvalidActivityTypeException, InvalidActivityAtributeException, UserNotFoundException, InvalidEmailException;
	public List<ActivityEntity> getAllActivitiesByTypes(String type, Pageable pageable);
	public void cleanUp();
}
