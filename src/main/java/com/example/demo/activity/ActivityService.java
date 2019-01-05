package com.example.demo.activity;

import org.springframework.data.domain.Pageable;
import java.util.List;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

public interface ActivityService {
	
	public void addNewActivity(ActivityEntity entity) throws ActivityAlreadyExistException, ElementNotFoundException, InvalidRoleException, UserNotFoundException, InvalidElementForActivityException, InvalidEmailException;
	public List<ActivityEntity> getAllActivitiesByTypes(String type, Pageable pageable);
	public void cleanUp();
}
