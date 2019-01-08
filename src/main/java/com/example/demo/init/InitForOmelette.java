package com.example.demo.init;

import java.util.Date;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementService;
import com.example.demo.element.custom.Pot;
import com.example.demo.user.TypesEnumUser;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;

//@Component
public class InitForOmelette {
		private UserService userService;
		private ElementService elementService;
		
		@Autowired
		public void setUserService(UserService service) {
			this.userService = service;
		}
		
		@Autowired
		public void setUserService(ElementService service) {
			this.elementService = service;
		}
		
		@PostConstruct
		public void init() {
			System.err.println("Inits for omelette creation...");
			
			try {
				//Create a player user + verify
				UserEntity user = new UserEntity("newplayer@gmail.com", "playground_lazar", "Tom the cat", null,
						TypesEnumUser.Types.Player.getType());
				userService.registerNewUser(user);
				user.setCode(null);
				userService.updateUserInfo(user);
				
				//Create Manager user + verify
				UserEntity manager = new UserEntity("demoManager44@gmail.com", "playground_lazar", "mr.manajer", null,
						TypesEnumUser.Types.Manager.getType());
				userService.registerNewUser(manager);
				manager.setCode(null);
				userService.updateUserInfo(manager);
				
				//Create Pot with id 1
				ElementEntity et = new ElementEntity(manager.getPlayground(), "1" , 0 ,  0 , "Uncle Tom's Pot" , new Date() , null , "Pot",
						null , manager.getPlayground(), manager.getEmail());
				Pot pot = new Pot(et);
				elementService.addNewElement(pot, manager.getEmail());
				System.err.println("Done initing!");

			}catch(Exception e) {
				System.err.println("No need to init because the database is already inited");

		}
		
	}

}
