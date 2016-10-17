package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payement.session.GIROSessionBeanLocal;
import ejb.payment.entity.GIRO;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "deleteGIROManagedBean")
@RequestScoped

public class DeleteGIROManagedBean {
    
    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;

    private ExternalContext ec;
    
    private Long giroId;
    
    public DeleteGIROManagedBean() {
    }

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public List<GIRO> getGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<GIRO> giro = gIROSessionBeanLocal.retrieveGIROByCusId(customerBasic.getCustomerBasicId());

        return giro;
    }
    
    public void deleteGIRO() throws IOException{
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        gIROSessionBeanLocal.deleteGIRO(giroId);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly! GIRO Arrangement deleted Successfully.", "Successfuly!"));
    }
}
