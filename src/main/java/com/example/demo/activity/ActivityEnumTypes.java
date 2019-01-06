package com.example.demo.activity;

public class ActivityEnumTypes {
	public enum Activities{
		
		BoardPost("BoardPost"), BoardRead("BoardRead"), CookOmelette("CookOmelette");
		// declaring private variable for getting values
		private String action;

		// getter method
		public String getActivityName() {
			return this.action;
		}

		// enum constructor - cannot be public or protected
		private Activities(String action) {
			this.action = action;
		}

	};
}