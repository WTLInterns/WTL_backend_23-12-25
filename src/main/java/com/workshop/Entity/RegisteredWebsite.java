package com.workshop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class RegisteredWebsite {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String email;
	private String phone;
	private String apiKey;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public RegisteredWebsite(int id, String name, String email, String phone, String apiKey) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.apiKey = apiKey;
	}
	@Override
	public String toString() {
		return "RegisteredWebsite [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", apiKey="
				+ apiKey + "]";
	}
	public RegisteredWebsite() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
