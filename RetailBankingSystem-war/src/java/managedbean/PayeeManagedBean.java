package managedbean;

import entity.Payee;
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import session.stateless.PayeeSessionLocal;

@Named(value = "transactionManagedBean")
@RequestScoped

public class PayeeManagedBean {
    @EJB
    private PayeeSessionLocal payeeSessionLocal;
    
    private Long payeeId;
    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    private Long newPayeeId;
    private String lastTransactionDate;
    private String statusMessage;
    private Long customerBasicId;
    
    private ExternalContext ec;
    
    public PayeeManagedBean() {
    }
    
    public String getPayeeName() {
        return payeeName;
    }
    
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    
    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }
    
    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }
    
    public String getPayeeAccountType() {
        return payeeAccountType;
    }
    
    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }
    
    public Long getNewPayeeId() {
        return newPayeeId;
    }
    
    public void setNewPayeeId(Long newPayeeId) {
        this.newPayeeId = newPayeeId;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public String getLastTransactionDate() {
        return lastTransactionDate;
    }
    
    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }
    
    public Long getPayeeId() {
        return payeeId;
    }
    
    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }
    
    public void addPayee() throws IOException{
        ec = FacesContext.getCurrentInstance().getExternalContext();
        Payee payee = payeeSessionLocal.retrievePayeeByName(payeeName);
        
        if(payee.getPayeeId()==null)
        {
            lastTransactionDate="";
            customerBasicId=Long.valueOf(1);
            newPayeeId=payeeSessionLocal.addNewPayee(payeeName,payeeAccountNum,payeeAccountType,lastTransactionDate,customerBasicId);
            statusMessage = "New Recipient Added Successfully.";
             
            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("newPayeeId", newPayeeId);
            ec.getFlash().put("payeeName", payeeName);
            ec.getFlash().put("payeeAccountNum", payeeAccountNum);
            ec.getFlash().put("payeeAccountType", payeeAccountType);
          
            ec.redirect("addRecipientDone.xhtml?faces-redirect=true");
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Recipient has existed.","Failed!"));
        }
    }
    
    public void deletePayee() throws IOException{
        ec = FacesContext.getCurrentInstance().getExternalContext();
        Payee payee = payeeSessionLocal.retrievePayeeByName(payeeName);
        
        if(payee.getPayeeId()==null)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Recipient does not exist.","Failed!"));
        }
        else
        {
            payeeAccountNum=payee.getPayeeAccountNum();
            payeeAccountType=payee.getPayeeAccountType();
            
            payeeSessionLocal.deletePayee(payeeName);
            statusMessage = "Recipient deleted Successfully.";
            
            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("payeeName", payeeName);
            ec.getFlash().put("payeeAccountNum", payeeAccountNum);
            ec.getFlash().put("payeeAccountType", payeeAccountType);
            
            ec.redirect("deleteRecipientDone.xhtml?faces-redirect=true");
        }
    }
}
