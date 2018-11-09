package com.example.demo.classes;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ElementTO {

	private static int ID = 0;
		
		
	/*
	 * ------------------------------- READ -----------------------
	 * user shouldn't be able to add an id, creationDate, expireDate and maybe more of the privates
	 * should we have regular setters for those methods as we don't want jackson to have the 
     * ability to alter them.
	 * 
	 *-------------------------------- READ -----------------------
	 */

	private String playground;
	private String id;
	private Location location;
	private String name;
	private Date creationDate;
	private Date expireDate;
	private String type;
	private Map<String,Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;
	
	public ElementTO() {
		super();
		ID++;
		this.id = ID + "";
	}

	public ElementTO(String playground, String id, Location location, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		
		super();
		ID++;
		this.playground = playground;
		this.id = ID + "";
		this.location = location;
		this.name = name;
		this.creationDate = creationDate;
		this.expireDate = expireDate;
		this.type = type;
		this.attributes = attributes;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
	}
		
		
//	public ElementTO(String string, int i, Location location2, String string2, Date date, Date date2, String string3,
//			TreeMap<String, Object> treeMap, String string4, String string5) {
//		// TODO Auto-generated constructor stub
//	}

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
}

