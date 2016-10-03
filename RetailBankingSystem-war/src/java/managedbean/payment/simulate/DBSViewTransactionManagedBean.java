package managedbean.payment.simulate;

import java.io.IOException;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import ejb.payement.session.DBSTransactionSessionBeanLocal;
import ejb.payment.entity.DBSTransaction;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "dBSViewTransactionManagedBean")
@SessionScoped

public class DBSViewTransactionManagedBean implements Serializable {
    
    @EJB
    private DBSTransactionSessionBeanLocal dBSTransactionSessionBeanLocal;

    private String dbsBankAccountNum;

    public DBSViewTransactionManagedBean() {
    }

    public String getDbsBankAccountNum() {
        return dbsBankAccountNum;
    }

    public void setDbsBankAccountNum(String dbsBankAccountNum) {
        this.dbsBankAccountNum = dbsBankAccountNum;
    }

    public List<DBSTransaction> getDBSTransaction() throws IOException {
        
        List<DBSTransaction> dbsTransaction = dBSTransactionSessionBeanLocal.retrieveAccTransactionByBankNum(dbsBankAccountNum);

        return dbsTransaction;
    }
}
