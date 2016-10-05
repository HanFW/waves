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

@Named(value = "merlionMasterAccountTransactionManagedBean")
@RequestScoped

public class MerlionMasterAccountTransactionManagedBean {
    
    @EJB
    private SACHMasterAccountTransactionSessionBeanLocal sACHMasterAccountTransactionSessionBeanLocal;

    private ExternalContext ec;
    
    public MerlionMasterAccountTransactionManagedBean() {
    }
    
    public List<SACHMasterAccountTransaction> getMerlionMasterAccountTransactions() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<SACHMasterAccountTransaction> merlionMasterAccountTransactions = sACHMasterAccountTransactionSessionBeanLocal.retrieveSACHMasterAccountTransactionByAccNum("88776655");

        return merlionMasterAccountTransactions;
    }
}
