package com.workshop.DTO;

public class VendorLoginRequest {

	private Long vendorId;
	
	private String email;
	
	private String password;

	private String contactNo;

	public VendorLoginRequest(Long vendorId, String email, String password, String contactNo) {
		super();
		this.vendorId = vendorId;
		this.email = email;
		this.password = password;
		this.contactNo=contactNo;
	}

	public VendorLoginRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	
	
	
}
