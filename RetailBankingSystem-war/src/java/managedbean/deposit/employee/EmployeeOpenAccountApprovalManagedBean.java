package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Interest;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.InterestSessionBeanLocal;
import ejb.payment.session.MerlionBankSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeOpenAccountApprovalManagedBean")
@SessionScoped

public class EmployeeOpenAccountApprovalManagedBean implements Serializable {

    @EJB
    private MerlionBankSessionBeanLocal merlionBankSessionBeanLocal;

    @EJB
    private InterestSessionBeanLocal interestSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private ExternalContext ec;

    private String customerIdentificationNum;

    public EmployeeOpenAccountApprovalManagedBean() {
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public List<CustomerBasic> getCustomerBasics() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        List<CustomerBasic> customerBasics = customerSessionBeanLocal.getAllNewCustomer();

        return customerBasics;
    }

    public void approveOpenAccount() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        merlionBankSessionBeanLocal.approveAccount(customerIdentificationNum);
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Approve Customer " + customerBasic.getCustomerName(), "Successfully!"));
    }

    public void rejectOpenAccount() {

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerIdentificationNum).get(0);
        Interest interest = bankAccount.getInterest();
        Long interestId = interest.getInterestId();

        merlionBankSessionBeanLocal.sendEmailToRejectCustomer(customerIdentificationNum);

        customerSessionBeanLocal.deleteCustomerBasic(customerIdentificationNum);
        interestSessionBeanLocal.deleteInterest(interestId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Reject Customer " + customerBasic.getCustomerName(), "Successfully!"));
    }
}
