/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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

    private List<String> depositAccounts = new ArrayList<String> ();
    private String selectedDepositAccount;

    private boolean agreement;

    public CustomerApplyDebitCardManagedBean() {
    }
    
    public void createDebitCard(ActionEvent event){
       DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
       Date applicationDate1 = new Date();
       String applicationDate=df.format(applicationDate1);
        
       System.out.println("apply debit Card:");
       System.out.println("depositAccount:"+selectedDepositAccount);
       System.out.println("cardHolderName:"+cardHolderName);
       System.out.println("application Date:"+applicationDate);
       System.out.println("debitCardType: "+cardType);
       
       String[] depositAccountInfo=selectedDepositAccount.split("-");
       String depositAccount=depositAccountInfo[1];
       
        System.out.println("deposit Account: "+depositAccount);
       debitCardSessionBeanLocal.createDebitCard(depositAccount, cardHolderName, applicationDate, cardType);
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
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public List<String> getDepositAccounts() {
//        if (depositAccounts == null) {
            customer = getCustomerViaSessionMap();
            System.out.println("check customer" + customer);
            List<BankAccount> depositAccountsOfCustomer = customer.getBankAccount();
            for (int i = 0; i < depositAccountsOfCustomer.size(); i++) {
                String info = depositAccountsOfCustomer.get(i).getBankAccountType() + "-" + depositAccountsOfCustomer.get(i).getBankAccountNum();            
                depositAccounts.add(i, info);
            }
//        }
        return depositAccounts;
    }

    public String getSelectedDepositAccount() {
        System.out.println("test get selectedDepositAccount "+selectedDepositAccount);
        return selectedDepositAccount;
    }

    public void setSelectedDepositAccount(String selectedDepositAccount) {
        this.selectedDepositAccount = selectedDepositAccount;
    }

}
