package com.workshop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "b2b_details")
public class B2B {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String contactNo;
    private String alternateMobileNo;
    private String cityName;

    // Changed from businessEmail to gmail for clarity
    private String gmail;

    // Added new password field for authentication
    private String password;

    private String bankName;
    private String bankAccountNo;
    private String ifscCode;
    private String panNo;
    private String companyOtherDetails;

    private String companyLogo; // Path for uploaded company logo
    private String govtApprovalCertificate;
    private String companyDocs;
    private String panDocs;
    private String businessGmail;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getBusinessGmail() {
        return businessGmail;
    }

    public void setBusinessGmail(String businessGmail) {
        this.businessGmail = businessGmail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getCompanyOtherDetails() {
        return companyOtherDetails;
    }

    public void setCompanyOtherDetails(String companyOtherDetails) {
        this.companyOtherDetails = companyOtherDetails;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getGovtApprovalCertificate() {
        return govtApprovalCertificate;
    }

    public void setGovtApprovalCertificate(String govtApprovalCertificate) {
        this.govtApprovalCertificate = govtApprovalCertificate;
    }

    public String getCompanyDocs() {
        return companyDocs;
    }

    public void setCompanyDocs(String companyDocs) {
        this.companyDocs = companyDocs;
    }

    public String getPanDocs() {
        return panDocs;
    }

    public void setPanDocs(String panDocs) {
        this.panDocs = panDocs;
    }

    @Override
    public String toString() {
        return "B2BDetails [id=" + id + ", companyName=" + companyName + ", contactNo=" + contactNo
                + ", alternateMobileNo=" + alternateMobileNo + ", cityName=" + cityName + ", gmail=" + gmail
                + ", password=" + password + ", bankName=" + bankName + ", bankAccountNo=" + bankAccountNo
                + ", ifscCode=" + ifscCode + ", panNo=" + panNo + ", companyOtherDetails=" + companyOtherDetails
                + ", companyLogo=" + companyLogo + ", govtApprovalCertificate=" + govtApprovalCertificate
                + ", companyDocs=" + companyDocs + ", panDocs=" + panDocs + "]";
    }
}
