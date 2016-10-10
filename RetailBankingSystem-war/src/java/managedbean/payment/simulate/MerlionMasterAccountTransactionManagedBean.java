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

@Named(value = "merlionMasterAccountTransactionManagedBean")
@RequestScoped

public class MerlionMasterAccountTransactionManagedBean {
    
    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    private ExternalContext ec;
    
    public MerlionMasterAccountTransactionManagedBean() {
    }
    
    public List<MEPSMasterAccountTransaction> getMerlionMasterAccountTransactions() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<MEPSMasterAccountTransaction> merlionMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("88776655");

        return merlionMasterAccountTransactions;
    }
}
