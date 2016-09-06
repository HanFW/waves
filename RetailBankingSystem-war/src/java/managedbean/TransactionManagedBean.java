package managedbean;

import entity.AccTransaction;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.stateless.TransactionSessionLocal;

@ManagedBean
@RequestScoped

public class TransactionManagedBean {
    @EJB
    private TransactionSessionLocal transactionSessionLocal;
    
    private String statusMessage;
    private String bankAccountNum;
    
    private ExternalContext ec;
    
    public TransactionManagedBean() {
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
    
    public List<AccTransaction> getTransaction() throws IOException
    {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<AccTransaction> accTransaction = transactionSessionLocal.retrieveAccTransactionByBankNum(bankAccountNum);
        
        if(accTransaction.isEmpty()) {
            bankAccountNum="";
            statusMessage="Your account number is invalid";
            ec.redirect("viewTransaction.xhtml?faces-redirect=true");
        }
        
        return accTransaction;
    }
}
