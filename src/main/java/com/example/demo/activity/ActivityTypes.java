package com.example.demo.activity;


public enum ActivityTypes{
		
		BoardPost("BoardPost"), BoardRead("BoardRead"), CookOmelette("CookOmelette");
		// declaring private variable for getting values
		private String action;

		// getter method
		public String getActivityName() {
			return this.action;
		}

		// enum constructor - cannot be public or protected
		private ActivityTypes(String action) {
			this.action = action;
		}

	}
