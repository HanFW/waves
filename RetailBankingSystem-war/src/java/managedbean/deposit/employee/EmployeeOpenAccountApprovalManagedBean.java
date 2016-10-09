package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeOpenAccountApprovalManagedBean")
@RequestScoped

public class EmployeeOpenAccountApprovalManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private ExternalContext ec;

    private String customerIdentificationNum;
    private String customerName;
    Long customerId;

    public EmployeeOpenAccountApprovalManagedBean() {
    }

    @PostConstruct
    public void init() {
        customerIdentificationNum = null;
    }

    public Long getCustomerId() {
        if (customerId == null) {
            System.out.println("customerId null get");
        } else {
            System.out.println("customerId not null get");
        }
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            System.out.println("customerId null set");
        } else {
            System.out.println("customerId not null set");
        }
        this.customerId = customerId;
    }

    public String getCustomerIdentificationNum() {
        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerId);
        customerIdentificationNum = customerBasic.getCustomerIdentificationNum();

        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerName() {
        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerId);
        customerName = customerBasic.getCustomerName();

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CustomerBasic> getCustomerBasics() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        List<CustomerBasic> customerBasics = customerSessionBeanLocal.getAllNewCustomer();

        return customerBasics;
    }

//    public Image getFileUpload() throws IOException {
//
//        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
//
//        String filename = customerBasic.getCustomerName() + "-" + customerIdentificationNum + ".png";
//
//        File file = new File("/Users/hanfengwei/NetBeansProjects/waves/RetailBankingSystem-war/web/resources/customerIdentification", filename);
//        Image img = ImageIO.read(new File(file.toURI()));
//
//        return img;
//    }
    public void approveOpenAccount() {
        bankAccountSessionBeanLocal.approveAccount(customerIdentificationNum);
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Approve Customer " + customerBasic.getCustomerName(), "Successfully!"));
        customerIdentificationNum = null;
    }

    public void rejectOpenAccount() {

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Approve Customer " + customerBasic.getCustomerName(), "Successfully!"));
    }
}
