package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
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
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;
    
    private ExternalContext ec;
    private String paymentAmt;
    private Long giroId;

    public NonStandingGIROManagedBean() {
    }

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public List<NonStandingGIRO> getNonStandingGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<NonStandingGIRO> nonStandingGiros = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROByCusId(customerBasic.getCustomerBasicId());

        return nonStandingGiros;
    }
    
    public void nonStandingGIROTransfer() {
        
    }
}
