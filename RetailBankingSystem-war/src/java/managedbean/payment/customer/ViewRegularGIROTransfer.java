package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import static ejb.customer.entity.CustomerBasic_.otherBankPayee;
import ejb.payment.entity.RegularGIRO;
import ejb.payment.session.RegularGIROSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "viewRegularGIROTransfer")
@RequestScoped

public class ViewRegularGIROTransfer {

    @EJB
    private RegularGIROSessionBeanLocal regularGIROSessionBeanLocal;

    private ExternalContext ec;
    private Long giroId;

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public ViewRegularGIROTransfer() {
    }

    public List<RegularGIRO> getAllRegularGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<RegularGIRO> regularGIROs = regularGIROSessionBeanLocal.retrieveRegularGIROByCusIC(customerBasic.getCustomerIdentificationNum());

        return regularGIROs;
    }

    public void deleteRegularGIRO() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        regularGIROSessionBeanLocal.deleteRegularGIRO(giroId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly Deleted!", " "));
    }
}
