package com.example.demo.activity.plugins;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementService;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Component
public class ActivityHandler {
	
	private ElementService elementService;
	private UserService userService;
	
	public ActivityHandler() {
		
	}
	
	@Autowired
	public void setUserService(UserService service) {
		this.userService = service;
	}
	
	@Autowired
	public void setElementService(ElementService service) {
		this.elementService = service;
	}
	
	public void handle(ActivityEntity activity) throws ElementNotFoundException, InvalidRoleException, UserNotFoundException, InvalidElementForActivityException, InvalidEmailException {
		String elementPlayground = activity.getElementPlayground();
		String elementID = activity.getElementId();
		String playerPlayground = activity.getPlayerPlayground();
		String playerEmail = activity.getPlayerEmail();
		
		ElementEntity element = this.elementService.getElement(elementPlayground, elementID, playerEmail);
		
		Map<String,Object> elementMapping = element.getAttributes();
		if(elementMapping == null || elementMapping.isEmpty()) {
			throw new InvalidElementForActivityException("cant use activity on this element, no activies available for this element");		
		}
		
		String activityType = activity.getType();
		
		for(String attribute: elementMapping.keySet()) {
			if(attribute.equals(activityType)) {	
				if(elementMapping.get(attribute) instanceof Long) {
					Long totalPoints = (Long)elementMapping.get(attribute);
					UserEntity user = this.userService.getUser(playerEmail, playerPlayground);
					totalPoints += user.getPoints();
					totalPoints += extractPointsFromAttributes(activity);
					user.setPoints(totalPoints);
					this.userService.updateUserInfo(user);	
					System.err.println("user new total points is: " + totalPoints);
				
					return;
				}
			}
		}
		
		throw new InvalidElementForActivityException("cant use activity on this element");		
	}
	
	
	private Long extractPointsFromAttributes(ActivityEntity activity) {
		Map<String, Object> attributes = activity.getAttributes();
		if(attributes.containsKey("points")) {
			Object pointsValue = attributes.get("points");
			try {
				if(pointsValue instanceof Integer) {
					return new Long((Integer)pointsValue);
				}
				else if(pointsValue instanceof String) {
					return new Long(Long.parseLong((String)pointsValue));
				}
			}catch(Exception e) {
				// do nothing
			}
		}
		
		return 0L;
	}
	
	
	
}
