package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.session.ReceivedChequeSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import ws.client.sach.SACHWebService_Service;

@Named(value = "employeeEnterReceivedChequeInformationManagedBean")
@RequestScoped

public class EmployeeEnterReceivedChequeInformationManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SACHWebService/SACHWebService.wsdl")
    private SACHWebService_Service service_sach;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private ReceivedChequeSessionBeanLocal receivedChequeSessionBeanLocal;

    private String customerName;
    private String customerMobile;
    private String transactionAmt;
    private String receivedBankAccountNum;

    private ExternalContext ec;

    public EmployeeEnterReceivedChequeInformationManagedBean() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(String transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getReceivedBankAccountNum() {
        return receivedBankAccountNum;
    }

    public void setReceivedBankAccountNum(String receivedBankAccountNum) {
        this.receivedBankAccountNum = receivedBankAccountNum;
    }

    public void submit() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();
        String receivedChequeStatus = "Pending";
        
        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByAccNum(receivedBankAccountNum);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(receivedBankAccountNum);
        String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
        Double totalBalance = Double.valueOf(currentAvailableBalance) + Double.valueOf(transactionAmt);

//        bankAccountSessionBeanLocal.updateBankAccountAvailableBalance(receivedBankAccountNum, totalBalance.toString());

        Long receivedChequeId = receivedChequeSessionBeanLocal.addNewReceivedCheque(
                transactionDate, transactionAmt, receivedBankAccountNum, customerName,
                customerMobile, receivedChequeStatus, customerBasic.getCustomerBasicId());

        clearMerlionReceivedCheque(receivedChequeId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully Input Received Cheque Information", ""));
    }

    private void clearMerlionReceivedCheque(java.lang.Long chequeId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
        port.clearMerlionReceivedCheque(chequeId);
    }
}
