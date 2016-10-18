package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payement.session.GIROSessionBeanLocal;
import ejb.payment.entity.FastPayee;
import ejb.payment.entity.GIRO;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "nonStandingGIROManagedBean")
@RequestScoped

public class NonStandingGIROManagedBean {

    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;
    
    private ExternalContext ec;

    public NonStandingGIROManagedBean() {
    }

    public List<GIRO> getNonStandingGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<GIRO> standingGiros = gIROSessionBeanLocal.retrieveNonStandingGIROByCusId(customerBasic.getCustomerBasicId());

        return standingGiros;
    }
}
