package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.payment.entity.SWIFTPayee;
import ejb.payment.session.SWIFTPayeeSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "deleteSWIFTPayeeManagedBean")
@RequestScoped

public class DeleteSWIFTPayeeManagedBean {

    @EJB
    private PayeeSessionBeanLocal payeeSessionBeanLocal;

    @EJB
    private SWIFTPayeeSessionBeanLocal sWIFTPayeeSessionBeanLocal;

    private Long payeeId;

    private ExternalContext ec;

    public DeleteSWIFTPayeeManagedBean() {
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public List<SWIFTPayee> getSWIFTPayees() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<SWIFTPayee> swiftPayee = sWIFTPayeeSessionBeanLocal.retrieveSWIFTPayeeByCusIC(customerBasic.getCustomerIdentificationNum());

        return swiftPayee;
    }

    public void deletePayee() throws IOException {

        SWIFTPayee swiftPayee = sWIFTPayeeSessionBeanLocal.retrieveSWIFTPayeeById(payeeId);

        if (swiftPayee.getPayeeId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Recipient does not exist.", "Failed!"));
        } else {
            payeeSessionBeanLocal.deletePayee(swiftPayee.getPayeeAccountNum());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully! Recipient deleted Successfully.", "Successfuly!"));
        }
    }
}
