package com.example.demo.activity;

public class ActivityEnumTypes {
	public enum Activities{
		
		BoardPost("BoardPost", new Long(10)), 
		BoardRead("BoardRead", new Long(1)), 
		CookOmelette("CookOmelette", new Long(100)),
		ENTER_TO_REFRIGERATOR("EnterRefrigerator", new Long(50));
		// declaring private variable for getting values
		private String action;
		private Long points;


		// enum constructor - cannot be public or protected
		private Activities(String action, Long points) {
			this.action = action;
			this.points = points;
		}

		public String getName() {
			return this.action;
		}

		public Long getPoints() {
			return points;
		}

	};
}