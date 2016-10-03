package managedbean.payment.simulate;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import ejb.payement.session.DBSBankAccountSessionBeanLocal;
import ejb.payment.entity.DBSBankAccount;

@Named(value = "dBSViewAccountManagedBean")
@RequestScoped

public class DBSViewAccountManagedBean implements Serializable {

    @EJB
    private DBSBankAccountSessionBeanLocal dBSBankAccountSessionBeanLocal;

    public DBSViewAccountManagedBean() {
    }

    public List<DBSBankAccount> getAllDBSBankAccounts() throws IOException {

        List<DBSBankAccount> dbsBankAccounts = dBSBankAccountSessionBeanLocal.getAllDBSBankAccount();

        return dbsBankAccounts;
    }

}
