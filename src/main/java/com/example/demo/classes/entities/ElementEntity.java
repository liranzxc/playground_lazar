package com.example.demo.classes.entities;

import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.classes.Location;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Document
//@Table(name = "ELEMENTS")
public class ElementEntity {

	public ElementEntity() {
		super();
		ID++;
		this.id = ID + "";
	}

	private static int ID = 0;

	private String playground;
	private String id;
	private String key;
	
	private double x;
	private double y;
	private String name;
	private Date creationDate;
	private Date expireDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;
	

	public ElementEntity(String playground, String id, Location location, String name, Date creationDate,
			Date expireDate, String type, Map<String, Object> attributes, String creatorPlayground,
			String creatorEmail) {

		super();
		//ID++;
		this.playground = playground;
		this.id = id;
		this.key = playground + "@@" + id;
		
		this.x = location.getX();
		this.y = location.getY();
		this.name = name;
		this.creationDate = creationDate;
		this.expireDate = expireDate;
		this.type = type;
		this.attributes = attributes;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
	}

	@Id
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	
	//@Id
	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	//@Id
	public String getId() {
		return id;
	}
	

	public void setId(String id) {
		this.id = id;
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
		return "ElementEntity [playground=" + playground + ", id=" + id + ", x=" + x + ", y=" + y + ", name=" + name
				+ ", creationDate=" + creationDate + ", expireDate=" + expireDate + ", type=" + type + ", attributes="
				+ attributes + ", creatorPlayground=" + creatorPlayground + ", creatorEmail=" + creatorEmail + "]";
	}



//	public String getDatabaseKey() {
//		String key = this.playground + this.id;
//		return key;
//	}
	
	
	
	//public String toString
	
}
