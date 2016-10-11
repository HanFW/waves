package managedbean.payment.simulate;

import java.io.IOException;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import ejb.payement.session.OtherTransactionSessionBeanLocal;
import ejb.payment.entity.OtherBankAccountTransaction;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "dBSViewTransactionManagedBean")
@SessionScoped

public class DBSViewTransactionManagedBean implements Serializable {
    
    @EJB
    private OtherTransactionSessionBeanLocal dBSTransactionSessionBeanLocal;

    private String dbsBankAccountNum;

    public DBSViewTransactionManagedBean() {
    }

    public String getDbsBankAccountNum() {
        return dbsBankAccountNum;
    }

    public void setDbsBankAccountNum(String dbsBankAccountNum) {
        this.dbsBankAccountNum = dbsBankAccountNum;
    }

    public List<OtherBankAccountTransaction> getDBSTransaction() throws IOException {
        
        List<OtherBankAccountTransaction> dbsTransaction = dBSTransactionSessionBeanLocal.retrieveAccTransactionByBankNum(dbsBankAccountNum);

        return dbsTransaction;
    }
}
