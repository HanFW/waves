/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerApplyDebitCardManagedBean")
@ViewScoped
public class CustomerApplyDebitCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerApplyDebitCardManagedBean
     */
    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    private CustomerBasic customer;
    private String cardHolderName;
    private String customerEmail;
    private String customerMobileNum;
    private String cardType;

    private List<String> depositAccounts = new ArrayList<String>();
    private String selectedDepositAccount;

    private boolean agreement;
    private boolean showResult = false;

    public CustomerApplyDebitCardManagedBean() {
    }

    public void createDebitCard(ActionEvent event) {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('applyDebitCardWizard').next();");

        showResult = true;
        System.out.println("debug: " + showResult);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date applicationDate1 = new Date();
        String applicationDate = df.format(applicationDate1);

        System.out.println("apply debit Card:");
        System.out.println("depositAccount:" + selectedDepositAccount);
        System.out.println("cardHolderName:" + cardHolderName);
        System.out.println("application Date:" + applicationDate);
        System.out.println("debitCardType: " + cardType);

        String[] depositAccountInfo = selectedDepositAccount.split("-");
        String depositAccount = depositAccountInfo[1];

        System.out.println("deposit Account: " + depositAccount);
        debitCardSessionBeanLocal.createDebitCard(depositAccount, cardHolderName, applicationDate, cardType);

    }

    public void checkExistingDebitCard(ActionEvent event) throws IOException {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        String[] bankAccountInfo = selectedDepositAccount.split("-");
        String bankAccount = bankAccountInfo[1];

        System.out.println("check existing debit card");
        System.out.println("depoist Account: " + bankAccount);
        System.out.println("card Type: " + cardType);

        if (cardType != null) {
            String result = debitCardSessionBeanLocal.checkDebitCardTypeForDepositAccount(bankAccount, cardType);
            if (result.equals("existing")) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, you cannot have the same type of debit card for one deposit account!", "Error!The debit card type has already Existed");
                context.addMessage(null, message);
                System.out.println("*** ApplyDebitCardManagedBean: debit card existed for the deposit account");

            } else {
                System.out.println("check existing debit card: not existing");
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('applyDebitCardWizard').next();");
            }

        }//end if cardType
    }

    public void checkReadAgreement(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        if (!isAgreement()) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please read Merlion Bank Debit Card Agreement before proceeding to next Step!", null);
            context.addMessage(null, message);
        } else {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('applyDebitCardWizard').next();");
        }

    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
        System.out.println("set card holder name" + cardHolderName);
    }

    public String getCustomerEmail() {
        if (customerEmail == null) {
            customerEmail = customer.getCustomerEmail();
        }

        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        System.out.println("set customer email" + customerEmail);
    }

    public String getCustomerMobileNum() {
        if (customerMobileNum == null) {
            customerMobileNum = customer.getCustomerMobile();
        }
        return customerMobileNum;
    }

    public void setCustomerMobileNum(String customerMobileNum) {
        this.customerMobileNum = customerMobileNum;
        System.out.println("set customer mobile number" + customerMobileNum);
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
        System.out.println("set agreement " + agreement);
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public List<String> getDepositAccounts() {

        customer = getCustomerViaSessionMap();
        Long id = customer.getCustomerBasicId();

        depositAccounts = debitCardSessionBeanLocal.getAllDepositAccounts(id);
        System.out.println("debug: "+depositAccounts);

        return depositAccounts;
    }

    public String getSelectedDepositAccount() {
        System.out.println("test get selectedDepositAccount " + selectedDepositAccount);
        return selectedDepositAccount;
    }

    public void setSelectedDepositAccount(String selectedDepositAccount) throws IOException {
        this.selectedDepositAccount = selectedDepositAccount;
//        checkExistingDebitCard();
    }

    public boolean isShowResult() {
        return showResult;
    }

    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }

}
