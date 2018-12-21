package com.example.demo.activity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Entity
@Document
public class ActivityEntity {
		private String playground;
		private String id;
		private String elementPlayground;
		private String elementId;
		private String type;
		private String playerPlayground;
		private String playerEmail;
		
		public ActivityEntity() {
			super();
		}
		public ActivityEntity(String playground, String id, String elementPlayground, String elementId, String type,
				String playerPlayground, String playerEmail, Map<String, Object> attributes) {
			super();
			this.playground = playground;
			this.id = id;
			this.elementPlayground = elementPlayground;
			this.elementId = elementId;
			this.type = type;
			this.playerPlayground = playerPlayground;
			this.playerEmail = playerEmail;
			this.attributes = attributes;
		}
		public ActivityEntity(ActivityTO activityTO) {
			// TODO Auto-generated constructor stub
			this.id = activityTO.getId();
			this.playground  = activityTO.getPlayground();
			this.elementPlayground = activityTO.getElementPlayground();
			this.elementId = activityTO.getElementId() ;
			this.type = activityTO.getType();
			this.playerPlayground = activityTO.getPlayerPlayground();
			this.playerEmail = getPlayerEmail();
			this.attributes = getAttributes();
		}
		public String getPlayground() {
			return playground;
		}
		public void setPlayground(String playground) {
			this.playground = playground;
		}
		@Id
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
		@Transient
		public Map<String, Object> getAttributes() {
			return attributes;
		}
		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}
		private Map<String,Object> attributes;
}
