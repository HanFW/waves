/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "appraiserSubmitValuationManagedBean")
@ViewScoped
public class AppraiserSubmitValuationManagedBean implements Serializable{
    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;
    
    private Long applicationId;
    private MortgageLoanApplication ma;
    private CustomerProperty cp;
    
    //property details
    private String customerPropertyAddress;
    private String customerPropertyPostal;
    private ArrayList<String> customerPropertyOwners = new ArrayList<String>();
    private String customerPropertyType;
    private Double customerPropertyBuiltUpArea;
    private Double customerPropertyLandArea;
    private String customerPropertyStatus;
    private Date customerPropertyTOPDate;
    private String customerPropertyUsage;
    private String customerPropertyTenureType;
    private Integer customerPropertyTenureDuration;
    private Integer customerPropertyTenureFromYear;
    
    private Double customerPropertyPurchasePrice;
    private Date customerPropertyDateOfPurchase;
    private String customerPropertySource;
    private String customerPropertyWithOTP;
    private Date customerPropertyOTPDate;
    private String customerPropertyWithTenancy;
    private Double customerPropertyTenancyIncome;
    private Integer customerPropertyTenancyExpiryYear;
    
    private double valuation;

    /**
     * Creates a new instance of AppraiserSubmitValuationManagedBean
     */
    public AppraiserSubmitValuationManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        applicationId = (Long) ec.getFlash().get("applicationId"); 
        ma = loanApplicationSessionBeanLocal.getMortgageLoanApplicationById(applicationId);
        cp = ma.getCustomerBasic().getCustomerProperty();
    }
    
    public void submitValuation() throws IOException{
        loanApplicationSessionBeanLocal.submitAppraisal(valuation, applicationId);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/AppraiserViewAllMortgageApplications.xhtml?faces-redirect=true");
    }

    public Long getApplicationId() {
        applicationId = ma.getLoanApplicationId();
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public MortgageLoanApplication getMa() {
        return ma;
    }

    public void setMa(MortgageLoanApplication ma) {
        this.ma = ma;
    }

    public String getCustomerPropertyAddress() {
        customerPropertyAddress = cp.getPropertyAddress();
        return customerPropertyAddress;
    }

    public void setCustomerPropertyAddress(String customerPropertyAddress) {
        this.customerPropertyAddress = customerPropertyAddress;
    }

    public String getCustomerPropertyPostal() {
        customerPropertyPostal = cp.getPropertyPostal();
        return customerPropertyPostal;
    }

    public void setCustomerPropertyPostal(String customerPropertyPostal) {
        this.customerPropertyPostal = customerPropertyPostal;
    }

    public ArrayList<String> getCustomerPropertyOwners() {
        customerPropertyOwners = cp.getPropertyOwners();
        return customerPropertyOwners;
    }

    public void setCustomerPropertyOwners(ArrayList<String> customerPropertyOwners) {
        this.customerPropertyOwners = customerPropertyOwners;
    }

    public String getCustomerPropertyType() {
        customerPropertyType = cp.getPropertyType();
        return customerPropertyType;
    }

    public void setCustomerPropertyType(String customerPropertyType) {
        this.customerPropertyType = customerPropertyType;
    }

    public Double getCustomerPropertyBuiltUpArea() {
        customerPropertyBuiltUpArea = cp.getPropertyBuiltUpArea();
        return customerPropertyBuiltUpArea;
    }

    public void setCustomerPropertyBuiltUpArea(Double customerPropertyBuiltUpArea) {
        this.customerPropertyBuiltUpArea = customerPropertyBuiltUpArea;
    }

    public Double getCustomerPropertyLandArea() {
        customerPropertyLandArea = cp.getPropertyLandArea();
        return customerPropertyLandArea;
    }

    public void setCustomerPropertyLandArea(Double customerPropertyLandArea) {
        this.customerPropertyLandArea = customerPropertyLandArea;
    }

    public String getCustomerPropertyStatus() {
        customerPropertyStatus = cp.getPropertyStatus();
        return customerPropertyStatus;
    }

    public void setCustomerPropertyStatus(String customerPropertyStatus) {
        this.customerPropertyStatus = customerPropertyStatus;
    }

    public Date getCustomerPropertyTOPDate() {
        customerPropertyTOPDate = cp.getPropertyTOPDate();
        return customerPropertyTOPDate;
    }

    public void setCustomerPropertyTOPDate(Date customerPropertyTOPDate) {
        this.customerPropertyTOPDate = customerPropertyTOPDate;
    }

    public String getCustomerPropertyUsage() {
        customerPropertyUsage = cp.getPropertyUsage();
        return customerPropertyUsage;
    }

    public void setCustomerPropertyUsage(String customerPropertyUsage) {
        this.customerPropertyUsage = customerPropertyUsage;
    }

    public String getCustomerPropertyTenureType() {
        customerPropertyTenureType = cp.getPropertyTenureType();
        return customerPropertyTenureType;
    }

    public void setCustomerPropertyTenureType(String customerPropertyTenureType) {
        this.customerPropertyTenureType = customerPropertyTenureType;
    }

    public Integer getCustomerPropertyTenureDuration() {
        customerPropertyTenureDuration = cp.getPropertyTenureDuration();
        return customerPropertyTenureDuration;
    }

    public void setCustomerPropertyTenureDuration(Integer customerPropertyTenureDuration) {
        this.customerPropertyTenureDuration = customerPropertyTenureDuration;
    }

    public Integer getCustomerPropertyTenureFromYear() {
        customerPropertyTenureFromYear = cp.getPropertyTenureStartYear();
        return customerPropertyTenureFromYear;
    }

    public void setCustomerPropertyTenureFromYear(Integer customerPropertyTenureFromYear) {
        this.customerPropertyTenureFromYear = customerPropertyTenureFromYear;
    }

    public Double getCustomerPropertyPurchasePrice() {
        customerPropertyPurchasePrice = ma.getPropertyPurchasePrice();
        return customerPropertyPurchasePrice;
    }

    public void setCustomerPropertyPurchasePrice(Double customerPropertyPurchasePrice) {
        this.customerPropertyPurchasePrice = customerPropertyPurchasePrice;
    }

    public Date getCustomerPropertyDateOfPurchase() {
        customerPropertyDateOfPurchase = ma.getPropertyDateOfPurchase();
        return customerPropertyDateOfPurchase;
    }

    public void setCustomerPropertyDateOfPurchase(Date customerPropertyDateOfPurchase) {
        this.customerPropertyDateOfPurchase = customerPropertyDateOfPurchase;
    }

    public String getCustomerPropertySource() {
        customerPropertySource = ma.getPropertySource();
        return customerPropertySource;
    }

    public void setCustomerPropertySource(String customerPropertySource) {
        this.customerPropertySource = customerPropertySource;
    }

    public String getCustomerPropertyWithOTP() {
        customerPropertyWithOTP = ma.getPropertyWithOTP();
        return customerPropertyWithOTP;
    }

    public void setCustomerPropertyWithOTP(String customerPropertyWithOTP) {
        this.customerPropertyWithOTP = customerPropertyWithOTP;
    }

    public Date getCustomerPropertyOTPDate() {
        customerPropertyOTPDate = ma.getPropertyOTPDate();
        return customerPropertyOTPDate;
    }

    public void setCustomerPropertyOTPDate(Date customerPropertyOTPDate) {
        this.customerPropertyOTPDate = customerPropertyOTPDate;
    }

    public String getCustomerPropertyWithTenancy() {
        customerPropertyWithTenancy = ma.getPropertyWithTenancy();
        return customerPropertyWithTenancy;
    }

    public void setCustomerPropertyWithTenancy(String customerPropertyWithTenancy) {
        this.customerPropertyWithTenancy = customerPropertyWithTenancy;
    }

    public Double getCustomerPropertyTenancyIncome() {
        customerPropertyTenancyIncome = ma.getPropertyTenancyIncome();
        return customerPropertyTenancyIncome;
    }

    public void setCustomerPropertyTenancyIncome(Double customerPropertyTenancyIncome) {
        this.customerPropertyTenancyIncome = customerPropertyTenancyIncome;
    }

    public Integer getCustomerPropertyTenancyExpiryYear() {
        customerPropertyTenancyExpiryYear = ma.getPropertyTenancyExpiryYear();
        return customerPropertyTenancyExpiryYear;
    }

    public void setCustomerPropertyTenancyExpiryYear(Integer customerPropertyTenancyExpiryYear) {
        this.customerPropertyTenancyExpiryYear = customerPropertyTenancyExpiryYear;
    }

    public CustomerProperty getCp() {
        return cp;
    }

    public void setCp(CustomerProperty cp) {
        this.cp = cp;
    }

    public double getValuation() {
        return valuation;
    }

    public void setValuation(double valuation) {
        this.valuation = valuation;
    }
    
    
}
