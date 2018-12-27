package com.example.demo.activity;

public class ActivityEnumTypes {
		public enum Activities {

			//Any new activity name must be added here!
			BoardPost("BoardPost"), BoardRead("BoardRead");
			
			private String actName;
			
			private Activities(String action) {
				this.actName = action;
			}
			
			
			public String getActivityName() {
				return this.actName;
			}
		}

}
