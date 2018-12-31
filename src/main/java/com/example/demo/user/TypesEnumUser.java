package com.example.demo.user;

public class TypesEnumUser {
		
	public enum Types {

		Player("Player"), Manager("Manager");
		// declaring private variable for getting values
		private String action;

		// getter method
		public String getType() {
			return this.action;
		}

		// enum constructor - cannot be public or protected
		private Types(String action) {
			this.action = action;
		}

	};

}
