package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.payment.entity.StandingGIRO;
import ejb.payment.session.StandingGIROSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeDeleteStandingGIROManagedBean")
@RequestScoped

public class EmployeeDeleteStandingGIROManagedBean {

    @EJB
    private StandingGIROSessionBeanLocal standingGIROSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;
    
    private ExternalContext ec;

    private Long giroId;
    private String customerIdentificationNum;
    
    public EmployeeDeleteStandingGIROManagedBean() {
    }
    
    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public List<StandingGIRO> getStandingGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<StandingGIRO> standingGiro = standingGIROSessionBeanLocal.retrieveStandingGIROByCusId(customerBasic.getCustomerBasicId());

        return standingGiro;
    }

    public void deleteStandingGIRO() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        standingGIROSessionBeanLocal.deleteStandingGIRO(giroId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly! GIRO Arrangement deleted Successfully.", "Successfuly!"));
    }
    
}
