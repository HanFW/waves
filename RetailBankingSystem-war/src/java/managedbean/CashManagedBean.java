package managedbean;

import entity.BankAccount;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import session.stateless.BankAccountSessionLocal;
import session.stateless.TransactionSessionLocal;

@Named(value = "cashManagedBean")
@RequestScoped


public class CashManagedBean {
    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;
    
    @EJB
    private TransactionSessionLocal transactionSessionLocal;
    
    private String depositModel;
    private String depositAmt;
    private String depositAccountNum;
    private String depositAccountPwd;
    private String confirmDepositAccountPwd;
    
    private String withdrawAmt;
    private String withdrawAccountNum;
    private String withdrawAccountPwd;
    private String confirmWithdrawAccountPwd;
    
    private String statusMessage;
    
    private ExternalContext ec;
    //ec = FacesContext.getCurrentInstance().getExternalContext();
    
    private BankAccount bankAccount;
    
    public CashManagedBean() {
    }
    
    public String getDepositModel() {
        return depositModel;
    }
    
    public void setDepositModel(String depositModel) {
        this.depositModel = depositModel;
    }
    
    public String getDepositAmt() {
        return depositAmt;
    }
    
    public void setDepositAmt(String depositAmt) {
        this.depositAmt = depositAmt;
    }
    
    public String getDepositAccountNum() {
        return depositAccountNum;
    }
    
    public void setDepositAccountNum(String depositAccountNum) {
        this.depositAccountNum = depositAccountNum;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public String getDepositAccountPwd() {
        return depositAccountPwd;
    }
    
    public void setDepositAccountPwd(String depositAccountPwd) {
        this.depositAccountPwd = depositAccountPwd;
    }
    
    public String getWithdrawAmt() {
        return withdrawAmt;
    }
    
    public void setWithdrawAmt(String withdrawAmt) {
        this.withdrawAmt = withdrawAmt;
    }
    
    public String getWithdrawAccountNum() {
        return withdrawAccountNum;
    }
    
    public void setWithdrawAccountNum(String withdrawAccountNum) {
        this.withdrawAccountNum = withdrawAccountNum;
    }
    
    public String getWithdrawAccountPwd() {
        return withdrawAccountPwd;
    }
    
    public void setWithdrawAccountPwd(String withdrawAccountPwd) {
        this.withdrawAccountPwd = withdrawAccountPwd;
    }
    
    public String getConfirmDepositAccountPwd() {
        return confirmDepositAccountPwd;
    }
    
    public void setConfirmDepositAccountPwd(String confirmDepositAccountPwd) {
        this.confirmDepositAccountPwd = confirmDepositAccountPwd;
    }
    
    public String getConfirmWithdrawAccountPwd() {
        return confirmWithdrawAccountPwd;
    }
    
    public void setConfirmWithdrawAccountPwd(String confirmWithdrawAccountPwd) {
        this.confirmWithdrawAccountPwd = confirmWithdrawAccountPwd;
    }
    
    public void cashDeposit() throws IOException{
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        String status = transactionSessionLocal.checkPassword(depositAccountNum,depositAccountPwd);
        
        if(status.equals("Password is incorrect!")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your password is incorrect.","Your password is incorrect!"));
        } else if(status.equals("Error! Bank account does not exist!")) {
            withdrawAccountNum = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your bank account does not exits.","Bank account does not exist!"));
        } else {
            transactionSessionLocal.cashDeposit(depositAccountNum,depositAmt);
            statusMessage = "Cash deposit Successfully!";
            
            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("depositAccountNum", depositAccountNum);
            ec.getFlash().put("depositAmt", depositAmt);
            
            ec.redirect("depositDone.xhtml?faces-redirect=true");
        }
    }
    
    public void cashWithdraw() throws IOException{
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(withdrawAccountNum);
        
        String status = transactionSessionLocal.checkPassword(withdrawAccountNum,withdrawAccountPwd);
        
        if(status.equals("Password is incorrect!")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your password is incorrect.","Your password is incorrect!"));
        } else if(status.equals("Error! Bank account does not exist!")) {
            withdrawAccountNum = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your bank account does not exist.","Bank account does not exist!"));
        } else {
            Double diffAmt = Double.valueOf(bankAccount.getBankAccountBalance())-Double.valueOf(withdrawAmt);
            if(diffAmt>=0){
                transactionSessionLocal.cashWithdraw(withdrawAccountNum,withdrawAmt);
                statusMessage = "Cash withdraw Successfully!";
                
                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("withdrawAccountNum", withdrawAccountNum);
                ec.getFlash().put("withdrawAmt", withdrawAmt);
                
                ec.redirect("withdrawDone.xhtml?faces-redirect=true");
            }
            else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account balance is insufficient.","Your account balance is insufficient."));
            }
        }
    }
}
