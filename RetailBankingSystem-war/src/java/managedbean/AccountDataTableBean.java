package managedbean;

import entity.BankAccount;
import entity.CustomerBasic;
import java.io.IOException;
import javax.ejb.EJB;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import session.stateless.BankAccountSessionLocal;

//@ManagedBean
@Named(value = "accountDataTableBean")
@RequestScoped

public class AccountDataTableBean {
    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;
    
    private String bankAccountNum;
    private String bankAccountType;
    private String customerName;
    private String customerIdentificationNum;
    private String statusMessage;
    
    private ExternalContext ec;
    
    private boolean visible;
    
    public AccountDataTableBean()
    {
        
    }
    
    public void show() {
        visible=true;
    }
    
    public void hide() {
        visible=false;
    }
    
    public List<BankAccount> getAllBankAccounts()
    {
        return bankAccountSessionLocal.getAllBankAccount();
    }
    
    public List<BankAccount> getBankAccount() throws IOException
    {
        System.out.println("*** accountDatabTableBean start: ");
        ec = FacesContext.getCurrentInstance().getExternalContext();
//        List<BankAccount> bankAccount = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIdentificationNum.toUpperCase());
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        String customerID = customer.getCustomerIdentificationNum();
        System.out.println("***accountDataTableBean: ID = " + customerID);
        List<BankAccount> bankAccount = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerID);
        
        if(bankAccount.isEmpty()) {
            customerIdentificationNum="";
            statusMessage="Your identification number is invalid";
            ec.redirect("viewAccount.xhtml?faces-redirect=true");
        }
        return bankAccount;
    }
    
    public String getBankAccountNum() {
        return bankAccountNum;
    }
    
    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
    
    public String getBankAccountType() {
        return bankAccountType;
    }
    
    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }
    
    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}