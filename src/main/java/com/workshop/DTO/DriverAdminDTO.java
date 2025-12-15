package com.workshop.DTO;

import com.workshop.Entity.DriveAdmin;

public class DriverAdminDTO {
    

    private int id;
	
	private String DriverName; 
	
	private String ContactNo;
	
	private String AltMobNum;
	
	private String emailId;
	

    public DriverAdminDTO(DriveAdmin driveAdmin){
        this.id=driveAdmin.getId();
        this.DriverName=driveAdmin.getDriverName();
        this.ContactNo=driveAdmin.getcontactNo();
        this.emailId=driveAdmin.getEmailId();
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getDriverName() {
        return DriverName;
    }


    public void setDriverName(String driverName) {
        DriverName = driverName;
    }


    public String getContactNo() {
        return ContactNo;
    }


    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }


    public String getAltMobNum() {
        return AltMobNum;
    }


    public void setAltMobNum(String altMobNum) {
        AltMobNum = altMobNum;
    }


    public String getEmailId() {
        return emailId;
    }


    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    
}
