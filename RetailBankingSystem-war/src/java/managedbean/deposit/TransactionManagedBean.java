package managedbean.deposit;

import ejb.deposit.entity.AccTransaction;
import java.io.IOException;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.ExternalContext;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "transactionManagedBean")
@SessionScoped

public class TransactionManagedBean implements Serializable{
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;
    
    @EJB
    private TransactionSessionBeanLocal transactionSessionLocal;
    
    private ExternalContext ec;
    private String bankAccountNum;
    
    public TransactionManagedBean() {
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public List<AccTransaction> getAccTransaction() throws IOException
    {
        System.out.println("=");
        System.out.println("====== deposit/TransactionManagedBean: getAccTransaction() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<AccTransaction> accTransaction = transactionSessionLocal.retrieveAccTransactionByBankNum(bankAccountNum);
        
        return accTransaction;
    }
}
