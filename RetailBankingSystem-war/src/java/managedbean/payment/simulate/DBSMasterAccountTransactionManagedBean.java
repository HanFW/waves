package managedbean.payment.simulate;

import ejb.payement.session.SACHMasterAccountTransactionSessionBeanLocal;
import ejb.payment.entity.SACHMasterAccountTransaction;
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
    private SACHMasterAccountTransactionSessionBeanLocal sACHMasterAccountTransactionSessionBeanLocal;

    private ExternalContext ec;
    
    public DBSMasterAccountTransactionManagedBean() {
    }
    
    public List<SACHMasterAccountTransaction> getDbsMasterAccountTransactions() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<SACHMasterAccountTransaction> dbsMasterAccountTransactions = sACHMasterAccountTransactionSessionBeanLocal.retrieveSACHMasterAccountTransactionByAccNum("44332211");

        return dbsMasterAccountTransactions;
    }
    
}
