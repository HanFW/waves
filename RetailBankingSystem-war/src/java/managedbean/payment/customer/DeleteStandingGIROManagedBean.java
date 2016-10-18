package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payement.session.StandingGIROSessionBeanLocal;
import ejb.payment.entity.StandingGIRO;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "deleteStandingGIROManagedBean")
@RequestScoped

public class DeleteStandingGIROManagedBean {

    @EJB
    private StandingGIROSessionBeanLocal standingGIROSessionBeanLocal;

    private ExternalContext ec;

    private Long giroId;

    public DeleteStandingGIROManagedBean() {
    }

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public List<StandingGIRO> getStandingGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<StandingGIRO> standingGiro = standingGIROSessionBeanLocal.retrieveStandingGIROByCusId(customerBasic.getCustomerBasicId());

        return standingGiro;
    }

    public void deleteStandingGIRO() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        standingGIROSessionBeanLocal.deleteStandingGIRO(giroId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly! GIRO Arrangement deleted Successfully.", "Successfuly!"));
    }
}
