package com.example.demo.user;

public class TypesEnumUser {
		
	public enum Types {

		Player("Player"),
		Manager("Manager");
		
		private String action;

		public String getType() {
			return this.action;
		}

		
		private Types(String action) {
			this.action = action;
		}

	};

}
