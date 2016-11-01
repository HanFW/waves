/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author hanfengwei
 */
@Entity
public class EducationLoanGuarantor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String salutation;
    private String name;
    private String gender;
    private String email;
    private String mobile;
    private String dateOfBirth;
    private String nationality;
    private String countryOfResidence;
    private String race;
    private String maritalStatus;
    private String occupation;
    private String company;
    private String address;
    private String postal;
    private String identificationNum;
    private String payeeNum;
    private String age;
    private byte[] signature;
    
    private int numOfDependent;
    private String education;
    private String residentialStatus;
    private int yearInResidence;
    private String industryType;
    private int lengthOfCurrentJob;
    private String employmentStatus;
    private double monthlyFixedIncome;
    private String residentialType;
    private String companyAddress;
    private String companyPostal;
    private String currentPosition;
    private String currentJobTitle;
    private String previousCompanyName;
    private int lengthOfPreviousJob;
    private double otherMonthlyIncome;
    private String otherMonthlyIncomeSource;
    
    @OneToOne(mappedBy = "educationLoanGuarantor")
    private EducationLoanApplication educationLoanApplication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public String getPayeeNum() {
        return payeeNum;
    }

    public void setPayeeNum(String payeeNum) {
        this.payeeNum = payeeNum;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public int getNumOfDependent() {
        return numOfDependent;
    }

    public void setNumOfDependent(int numOfDependent) {
        this.numOfDependent = numOfDependent;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public int getYearInResidence() {
        return yearInResidence;
    }

    public void setYearInResidence(int yearInResidence) {
        this.yearInResidence = yearInResidence;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public int getLengthOfCurrentJob() {
        return lengthOfCurrentJob;
    }

    public void setLengthOfCurrentJob(int lengthOfCurrentJob) {
        this.lengthOfCurrentJob = lengthOfCurrentJob;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public double getMonthlyFixedIncome() {
        return monthlyFixedIncome;
    }

    public void setMonthlyFixedIncome(double monthlyFixedIncome) {
        this.monthlyFixedIncome = monthlyFixedIncome;
    }

    public String getResidentialType() {
        return residentialType;
    }

    public void setResidentialType(String residentialType) {
        this.residentialType = residentialType;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPostal() {
        return companyPostal;
    }

    public void setCompanyPostal(String companyPostal) {
        this.companyPostal = companyPostal;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCurrentJobTitle() {
        return currentJobTitle;
    }

    public void setCurrentJobTitle(String currentJobTitle) {
        this.currentJobTitle = currentJobTitle;
    }

    public String getPreviousCompanyName() {
        return previousCompanyName;
    }

    public void setPreviousCompanyName(String previousCompanyName) {
        this.previousCompanyName = previousCompanyName;
    }

    public int getLengthOfPreviousJob() {
        return lengthOfPreviousJob;
    }

    public void setLengthOfPreviousJob(int lengthOfPreviousJob) {
        this.lengthOfPreviousJob = lengthOfPreviousJob;
    }

    public double getOtherMonthlyIncome() {
        return otherMonthlyIncome;
    }

    public void setOtherMonthlyIncome(double otherMonthlyIncome) {
        this.otherMonthlyIncome = otherMonthlyIncome;
    }

    public String getOtherMonthlyIncomeSource() {
        return otherMonthlyIncomeSource;
    }

    public void setOtherMonthlyIncomeSource(String otherMonthlyIncomeSource) {
        this.otherMonthlyIncomeSource = otherMonthlyIncomeSource;
    }
    
    

    public EducationLoanApplication getEducationLoanApplication() {
        return educationLoanApplication;
    }

    public void setEducationLoanApplication(EducationLoanApplication educationLoanApplication) {
        this.educationLoanApplication = educationLoanApplication;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EducationLoanGuarantor)) {
            return false;
        }
        EducationLoanGuarantor other = (EducationLoanGuarantor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.EducationLoanGuarantor[ id=" + id + " ]";
    }
    
}
