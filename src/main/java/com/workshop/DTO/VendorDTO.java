package com.workshop.DTO;

import com.workshop.Entity.Vendor;

public class VendorDTO {

    private Long id;
    private String vendorCompanyName;
    private String contactNo;
    private String vendorEmail;

    // Constructor to map from Vendor entity
    public VendorDTO(Vendor vendor) {
        this.id = vendor.getId();
        this.vendorCompanyName = vendor.getVendorCompanyName();
        this.contactNo = vendor.getContactNo();
        this.vendorEmail = vendor.getVendorEmail();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVendorCompanyName() {
		return vendorCompanyName;
	}

	public void setVendorCompanyName(String vendorCompanyName) {
		this.vendorCompanyName = vendorCompanyName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

    // Getters and Setters
    
    
}
