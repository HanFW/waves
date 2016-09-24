package managedbean.deposit.employee;

import ejb.deposit.entity.AccTransaction;
import ejb.deposit.session.TransactionSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeViewTransactionFinalManagedBean")
@RequestScoped

public class EmployeeViewTransactionFinalManagedBean {
    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;
    
    private ExternalContext ec;
    private String bankAccountNum;
    
    public EmployeeViewTransactionFinalManagedBean() {
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
    
    public List<AccTransaction> getAccTransaction() throws IOException
    {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        List<AccTransaction> accTransaction = transactionSessionBeanLocal.retrieveAccTransactionByBankNum(bankAccountNum);
        
        return accTransaction;
    }
    
}
