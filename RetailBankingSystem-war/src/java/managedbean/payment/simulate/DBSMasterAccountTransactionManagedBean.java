package managedbean.payment.simulate;

import ejb.payement.session.MEPSMasterAccountTransactionSessionBeanLocal;
import ejb.payment.entity.MEPSMasterAccountTransaction;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "dBSMasterAccountTransactionManagedBean")
@RequestScoped

public class DBSMasterAccountTransactionManagedBean {
    
    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    private ExternalContext ec;
    
    public DBSMasterAccountTransactionManagedBean() {
    }
    
    public List<MEPSMasterAccountTransaction> getDbsMasterAccountTransactions() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<MEPSMasterAccountTransaction> dbsMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("44332211");

        return dbsMasterAccountTransactions;
    }
    
}
