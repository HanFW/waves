package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.payment.entity.IssuedCheque;
import ejb.payment.entity.ReceivedCheque;
import ejb.payment.session.IssuedChequeSessionBeanLocal;
import ejb.payment.session.ReceivedChequeSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeViewChequeStatusDoneManagedBean")
@RequestScoped

public class EmployeeViewChequeStatusDoneManagedBean {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private IssuedChequeSessionBeanLocal issuedChequeSessionBeanLocal;

    @EJB
    private ReceivedChequeSessionBeanLocal receivedChequeSessionBeanLocal;

    private ExternalContext ec;
    private String customerIdentificationNum;

    public EmployeeViewChequeStatusDoneManagedBean() {
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public List<ReceivedCheque> getReceivedCheque() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<ReceivedCheque> receivedCheque = receivedChequeSessionBeanLocal.retrieveReceivedChequeByCusIC(customerBasic.getCustomerIdentificationNum());

        return receivedCheque;
    }

    public List<IssuedCheque> getIssuedCheque() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<IssuedCheque> issuedCheque = issuedChequeSessionBeanLocal.retrieveIssuedChequeByCusIC(customerBasic.getCustomerIdentificationNum());

        return issuedCheque;
    }
}
