package managedbean;

import entity.BankAccount;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import session.stateless.BankAccountSessionLocal;
import session.stateless.TransactionSessionLocal;

@Named(value = "transferManagedBean")
@RequestScoped

public class TransferManagedBean {
    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;
    @EJB
    private TransactionSessionLocal transactionSessionLocal;
    
    private String fromAccount;
    private String fromCurrency;
    private String toAccount;
    private String toCurrency;
    private String transferAmt;
    
    private String statusMessage;
    
    private ExternalContext ec;
    
    public TransferManagedBean() {
    }
    
    public String getFromAccount() {
        return fromAccount;
    }
    
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
    
    public String getFromCurrency() {
        return fromCurrency;
    }
    
    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }
    
    public String getToAccount() {
        return toAccount;
    }
    
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
    
    public String getToCurrency() {
        return toCurrency;
    }
    
    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }
    
    public String getTransferAmt() {
        return transferAmt;
    }
    
    public void setTransferAmt(String transferAmt) {
        this.transferAmt = transferAmt;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public void toMyAccount() throws IOException {
        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        if(fromAccount.equals(toAccount)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Fund transfer cannot be done within the same accounts.","Failed!"));
        }
        else{
            
            if(bankAccountFrom.getBankAccountStatus().equals("Activated")){
                
            }
            else if(bankAccountFrom.getBankAccountStatus().equals("Inactivated")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account has not been activated.","Failed!"));
            }
            if(bankAccountTo.getBankAccountStatus().equals("Activated")) {
                
            }
            else if(bankAccountTo.getBankAccountStatus().equals("Inactivated")) {
                
            }
            Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance())-Double.valueOf(transferAmt);
           
            if(diffAmt>=0) {
                transactionSessionLocal.fundTransfer(fromAccount,toAccount,transferAmt);
                statusMessage = "Fund Transfer Successfully!";
                System.out.println("if");
                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("fromAccount", fromAccount);
                ec.getFlash().put("toAccount", toAccount);
                ec.getFlash().put("transferAmt", transferAmt);
                
                ec.redirect("transferDone.xhtml?faces-redirect=true");
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.","Failed!"));
            }
            
        }
    }
    
    public void toOthersAccount() throws IOException {
        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        transactionSessionLocal.fundTransfer(fromAccount,toAccount,transferAmt);
        statusMessage = "Fund Transfer Successfully!";
        
        if(bankAccountFrom.getBankAccountStatus().equals("Inactivated")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account has not been activated.","Failed"));
        }
        
        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("fromAccount", fromAccount);
        ec.getFlash().put("toAccount", toAccount);
        ec.getFlash().put("transferAmt", transferAmt);
        
        ec.redirect("transferDone.xhtml?faces-redirect=true");
    }
    
    public void oneTimeTransfer() throws IOException {
        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        transactionSessionLocal.fundTransfer(fromAccount,toAccount,transferAmt);
        statusMessage = "Fund Transfer Successfully!";
        
        if(bankAccountFrom.getBankAccountStatus().equals("Inactivated")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account has not been activated.","Failed"));
        }
        
        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("fromAccount", fromAccount);
        ec.getFlash().put("toAccount", toAccount);
        ec.getFlash().put("transferAmt", transferAmt);
        
        ec.redirect("transferDone.xhtml?faces-redirect=true");
    }
}

