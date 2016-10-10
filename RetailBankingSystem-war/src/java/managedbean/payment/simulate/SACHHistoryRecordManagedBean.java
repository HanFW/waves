package managedbean.payment.simulate;

import ejb.payement.session.SACHSessionBeanLocal;
import ejb.payment.entity.SACH;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "sACHHistoryRecordManagedBean")
@RequestScoped

public class SACHHistoryRecordManagedBean {
    
    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;
    
    private ExternalContext ec;
    
    public SACHHistoryRecordManagedBean() {
    }
    
    public List<SACH> getDbsAndMerlionSACHs() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<SACH> sachs = sACHSessionBeanLocal.getAllSACH("DBS&Merlion");

        return sachs;
    }
    
}
