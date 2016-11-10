/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.employee;

import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author aaa
 */
@Named(value = "creditCardManagerSupplementaryManagedBean")
@ViewScoped
public class CreditCardManagerSupplementaryManagedBean implements Serializable {

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;

    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;

    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    private CustomerBasic customer;
    private CustomerAdvanced ca;
    private PrincipalCard pc;
    private SupplementaryCard sc;

    //basic information
    private String customerName;
    private String customerDateOfBirth;
    private String customerNationality;
    private String customerIdentificationNum;

    //supplementary card holder info
    private String cardHolderName;
    private String cardHolderIdentificationNum;
    private String cardHolderDateOfBirth;
    private String relationship;

    //credit card details
    private Double creditLimit;
    private String creditCardTypeName;

    public CreditCardManagerSupplementaryManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Long supplementaryCardId = (Long) ec.getFlash().get("supplementaryCardId");
        sc = creditCardSessionLocal.getSupplementaryCardById(supplementaryCardId);
        pc = sc.getPrincipalCard();
        customer = pc.getCustomerBasic();
        ca = customer.getCustomerAdvanced();
        System.out.println("@@@@@@@@@@@@IC num " + customer.getCustomerIdentificationNum());

        customerName = customer.getCustomerName();
        customerDateOfBirth = customer.getCustomerDateOfBirth();
        customerNationality = customer.getCustomerNationality();
        customerIdentificationNum = customer.getCustomerIdentificationNum();

        cardHolderName = sc.getCardHolderName();
        cardHolderIdentificationNum = sc.getIdentificationNum();
        cardHolderDateOfBirth = sc.getDateOfBirth();
        relationship = sc.getRelationship();

        creditLimit = pc.getCreditLimit();
        creditCardTypeName = pc.getCreditCardType().getCreditCardTypeName();
    }

    public void approveRequest() throws IOException {
        creditCardSessionLocal.approveSupplementary(sc.getCardId());
        loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "employee approves supplementary card", "successful", null);

        Calendar cal = Calendar.getInstance();
        Date receivedDate = cal.getTime();

        String subject = "Your Supplementary card for " + sc.getCreditCardType().getCreditCardTypeName() + " has been approved.";
        String messageContent = "<br/><br/>Your Supplementary card for " + sc.getCreditCardType().getCreditCardTypeName() + " has been approved by one of our card managers. <br/><br/>"
                + "Please activate your credit card in 15 days. <br/><br/>"
                + "<a href=\"https://localhost:8181/RetailBankingSystem-war/web/onlineBanking/card/creditCard/customerActivateCreditCard.xhtml\">ACTIVATE HERE</a> <br/><br/>"
                + "Thank you. <br/>";

        messageSessionBeanLocal.sendMessage("Merlion Bank", "Supplementary Card", subject, receivedDate.toString(),
                messageContent, customer.getCustomerBasicId());

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerViewApplication.xhtml?faces-redirect=true");
    }

    public void rejectRequest() throws IOException {
        creditCardSessionLocal.rejectSupplementary(sc.getCardId());
        loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "employee rejects supplementary card", "successful", null);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerViewApplication.xhtml?faces-redirect=true");
    }

    public CreditCardSessionBeanLocal getCreditCardSessionLocal() {
        return creditCardSessionLocal;
    }

    public void setCreditCardSessionLocal(CreditCardSessionBeanLocal creditCardSessionLocal) {
        this.creditCardSessionLocal = creditCardSessionLocal;
    }

    public CustomerAdminSessionBeanLocal getCustomerAdminSessionBeanLocal() {
        return customerAdminSessionBeanLocal;
    }

    public void setCustomerAdminSessionBeanLocal(CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal) {
        this.customerAdminSessionBeanLocal = customerAdminSessionBeanLocal;
    }

    public CRMCustomerSessionBeanLocal getcRMCustomerSessionBeanLocal() {
        return cRMCustomerSessionBeanLocal;
    }

    public void setcRMCustomerSessionBeanLocal(CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal) {
        this.cRMCustomerSessionBeanLocal = cRMCustomerSessionBeanLocal;
    }

    public MessageSessionBeanLocal getMessageSessionBeanLocal() {
        return messageSessionBeanLocal;
    }

    public void setMessageSessionBeanLocal(MessageSessionBeanLocal messageSessionBeanLocal) {
        this.messageSessionBeanLocal = messageSessionBeanLocal;
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public CustomerAdvanced getCa() {
        return ca;
    }

    public void setCa(CustomerAdvanced ca) {
        this.ca = ca;
    }

    public PrincipalCard getPc() {
        return pc;
    }

    public void setPc(PrincipalCard pc) {
        this.pc = pc;
    }

    public SupplementaryCard getSc() {
        return sc;
    }

    public void setSc(SupplementaryCard sc) {
        this.sc = sc;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardHolderIdentificationNum() {
        return cardHolderIdentificationNum;
    }

    public void setCardHolderIdentificationNum(String cardHolderIdentificationNum) {
        this.cardHolderIdentificationNum = cardHolderIdentificationNum;
    }

    public String getCardHolderDateOfBirth() {
        return cardHolderDateOfBirth;
    }

    public void setCardHolderDateOfBirth(String cardHolderDateOfBirth) {
        this.cardHolderDateOfBirth = cardHolderDateOfBirth;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCreditCardTypeName() {
        return creditCardTypeName;
    }

    public void setCreditCardTypeName(String creditCardTypeName) {
        this.creditCardTypeName = creditCardTypeName;
    }

    private Long getEmployeeViaSessionMap() {
        Long employeeId;
        FacesContext context = FacesContext.getCurrentInstance();
        Employee employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        employeeId = employee.getEmployeeId();

        return employeeId;
    }

}
