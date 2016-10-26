package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payment.entity.ReceivedCheque;
import ejb.payment.session.ReceivedChequeSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "viewChequeStatusManagedBean")
@RequestScoped

public class ViewChequeStatusManagedBean {
    @EJB
    private ReceivedChequeSessionBeanLocal receivedChequeSessionBeanLocal;

    private ExternalContext ec;
    
    public ViewChequeStatusManagedBean() {
    }
    
    public List<ReceivedCheque> getReceivedCheque() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<ReceivedCheque> receivedCheque = receivedChequeSessionBeanLocal.retrieveReceivedChequeByCusId(customerBasic.getCustomerIdentificationNum());

        return receivedCheque;
    }
}
