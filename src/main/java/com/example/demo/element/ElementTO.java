package com.example.demo.element;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ElementTO {


	private String playground;
	private String id;
	private Location location;
	private String name;
	private Date creationDate;
	private Date expireDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;

	public ElementTO() {
		super();
	}

	public ElementTO(String playground, String id, Location location, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {

		super();
		this.playground = playground;
		this.location = location;
		this.name = name;
		this.creationDate = creationDate;
		this.expireDate = expireDate;
		this.type = type;
		this.attributes = attributes;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
	}


	public ElementTO(ElementEntity entity) {
		
		this.playground = entity.getPlayground();
		this.id = entity.getId();
		this.location = new Location(entity.getX(), entity.getY());
		this.name = entity.getName();
		this.creationDate = entity.getCreationDate();
		this.expireDate = entity.getExpireDate();
		this.type = entity.getType();
		this.attributes = entity.getAttributes();
		this.creatorPlayground = entity.getCreatorPlayground();
		this.creatorEmail = entity.getCreatorEmail();
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
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
	
	public ElementEntity ToEntity()
	{
		return new ElementEntity(playground, id, this.location.getX(), this.location.getY()
				,name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
	}
	
	
	public boolean equals(ElementTO elementTO) {
		if(elementTO.id.equals(this.id)
				&& elementTO.playground.equals(this.playground))
			return true;
		
		return false;
		
	}

	@Override
	public String toString() {
		return "ElementTO [playground=" + playground + ", id=" + id + ", location=" + location + ", name=" + name
				+ ", creationDate=" + creationDate + ", expireDate=" + expireDate + ", type=" + type + ", attributes="
				+ attributes + ", creatorPlayground=" + creatorPlayground + ", creatorEmail=" + creatorEmail + "]";
	}
	
	
}
