package managedbean.payment.simulate;

import ejb.payement.session.MEPSSessionBeanLocal;
import ejb.payment.entity.MEPS;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "mEPSHistoryRecordManagedBean")
@RequestScoped

public class MEPSHistoryRecordManagedBean {
    
    @EJB
    private MEPSSessionBeanLocal mEPSSessionBeanLocal;

    private ExternalContext ec;
    
    public MEPSHistoryRecordManagedBean() {
    }
    
    public List<MEPS> getDbsAndMerlionMEPSs() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<MEPS> mepses = mEPSSessionBeanLocal.getAllMEPS("DBS&Merlion");

        return mepses;
    }
}
