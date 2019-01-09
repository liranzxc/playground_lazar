package com.example.demo.activity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Document
public class ActivityEntity {
		private String elementPlayground;
		private String elementId;
		private String type;
		private String playerPlayground;
		private String playerEmail;
		private String key;
		private Map<String,Object> attributes;
		
		public ActivityEntity() {
			super();
		}
		
		public ActivityEntity(String playground,String id, String elementPlayground, String elementId, String type,
				String playerPlayground, String playerEmail, Map<String, Object> attributes) {
			super();
			this.key = generateKey(playground, id);
			this.elementPlayground = elementPlayground;
			this.elementId = elementId;
			this.type = type;
			this.playerPlayground = playerPlayground;
			this.playerEmail = playerEmail;
			this.attributes = attributes;
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
		
		public void setJsonAttributes(String jsonAttributes) {
			try {
				this.attributes = new ObjectMapper().readValue(jsonAttributes, Map.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Lob
		public String getJsonAttributes() {
			try {
				return new ObjectMapper().writeValueAsString(this.attributes);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Id
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public static String generateKey(String playground, String id) {
			return id +"@@" +playground;
		}

		@Override
		public String toString() {
			return "ActivityEntity [elementPlayground=" + elementPlayground + ", elementId=" + elementId + ", type="
					+ type + ", playerPlayground=" + playerPlayground + ", playerEmail=" + playerEmail + ", key=" + key
					+ ", attributes=" + attributes + "]";
		}
		
		
		
}
