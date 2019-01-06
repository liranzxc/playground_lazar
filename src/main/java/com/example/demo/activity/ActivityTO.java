package com.example.demo.activity;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ActivityTO {
		private static int ID = 0;
		private String id;
		private String playground;
		private String elementPlayground;
		private String elementId;
		private String type;
		private String playerPlayground;
		private String playerEmail;
	
		
		public ActivityTO() {
			super();
		}
		public ActivityTO(String playground, String elementPlayground, String elementId, String type,
				String playerPlayground, String playerEmail, Map<String, Object> attributes) {
			super();
			this.playground = playground;
			this.id = ""+ID++;
			this.elementPlayground = elementPlayground;
			this.elementId = elementId;
			this.type = type;
			this.playerPlayground = playerPlayground;
			this.playerEmail = playerEmail;
			this.attributes = attributes;
		}
		public String getPlayground() {
			return playground;
		}
		public void setPlayground(String playground) {
			this.playground = playground;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getElementPlayground() {
			return elementPlayground;
		}
		public void setElementPlayground(String elementPlayground) {
			this.elementPlayground = elementPlayground;
		}
		public String getElementId() {
			return elementId;
		}
		public void setElementId(String elementId) {
			this.elementId = elementId;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getPlayerPlayground() {
			return playerPlayground;
		}
		public void setPlayerPlayground(String playerPlayground) {
			this.playerPlayground = playerPlayground;
		}
		public String getPlayerEmail() {
			return playerEmail;
		}
		public void setPlayerEmail(String playerEmail) {
			this.playerEmail = playerEmail;
		}
		public Map<String, Object> getAttributes() {
			return attributes;
		}
		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}
		private Map<String,Object> attributes;

		public ActivityEntity ToEntity() {
			return new ActivityEntity(playground, ""+id ,elementPlayground, elementId, type, playerPlayground, playerEmail, attributes);
		}
		
}
