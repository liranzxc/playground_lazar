package com.example.demo.element;

import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Document
//@Table(name = "ELEMENTS")
public class ElementEntity {
	
	public ElementEntity() {
		super();
		this.id = -1;
		this.key = "playground_lazar" + "@@" + this.id; //TODO do we need an empty contructor?
	}


	protected String key;
	
	protected double x;
	protected double y;
	protected String name;
	protected Date creationDate;
	protected Date expireDate;
	protected String type;
	protected Map<String, Object> attributes;
	protected String creatorPlayground;
	protected String creatorEmail;
	
	//@GeneratedValue(strategy=GenerationType.AUTO)
	protected int id;
		
	
	public ElementEntity(String playground, String id, double x, double y, String name, Date creationDate, Date expireDate, String type,
			Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super();
		this.key = createKeyFromIdAndPlayground(id, playground);
		this.x = x;
		this.y = y;
		this.name = name;
		this.creationDate = creationDate;
		this.expireDate = expireDate;
		this.type = type;
		this.attributes = attributes;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
		this.id = Integer.parseInt(id);
	}

	@Id
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	
	public String getPlayground() {
		return this.key.split("@@")[1];
	}
	
	public void setPlayground(String playground) {
		
	}

	public String getId() {
		return this.key.split("@@")[0];
	}
	
	public void setId(String id) {
		
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}

	@Transient
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Lob
	public String getJsonAttributes() {
		try {
			return new ObjectMapper().writeValueAsString(this.attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setJsonAttributes(String jsonAttributes) {
		try {
			this.attributes = new ObjectMapper().readValue(jsonAttributes, Map.class);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String getCreatorPlayground() {
		return creatorPlayground;
	}

	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}


	@Override
	public String toString() {
		return "ElementEntity [playground=" + getPlayground() + ", id=" + getId() +", key=" + getKey() + ", x=" + x + ", y=" + y + ", name=" + name
				+ ", creationDate=" + creationDate + ", expireDate=" + expireDate + ", type=" + type + ", attributes="
				+ attributes + ", creatorPlayground=" + creatorPlayground + ", creatorEmail=" + creatorEmail + "]";
	}	
	
	public static String createKeyFromIdAndPlayground(String id, String playground) {
		return id + "@@" + playground;
	}
}
