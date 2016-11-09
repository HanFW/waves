package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
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

@Named(value = "employeeViewRegularGIROTransferDone")
@RequestScoped

public class EmployeeViewRegularGIROTransferDone {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private RegularGIROSessionBeanLocal regularGIROSessionBeanLocal;

    private ExternalContext ec;
    private Long giroId;
    private String customerIdentificationNum;

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

    public EmployeeViewRegularGIROTransferDone() {
    }

    public List<RegularGIRO> getAllRegularGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<RegularGIRO> regularGIROs = regularGIROSessionBeanLocal.retrieveRegularGIROByCusIC(customerBasic.getCustomerIdentificationNum());

        return regularGIROs;
    }

    public void deleteRegularGIRO() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        regularGIROSessionBeanLocal.deleteRegularGIRO(giroId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly Deleted!", " "));
    }
}
