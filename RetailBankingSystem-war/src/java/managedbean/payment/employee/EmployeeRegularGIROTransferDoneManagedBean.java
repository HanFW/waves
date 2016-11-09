package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.OtherBankPayee;
import ejb.payment.session.OtherBankPayeeSessionBeanLocal;
import ejb.payment.session.RegularGIROSessionBeanLocal;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import ws.client.sach.SACHWebService_Service;

@Named(value = "employeeRegularGIROTransferDoneManagedBean")
@RequestScoped

public class EmployeeRegularGIROTransferDoneManagedBean {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private RegularGIROSessionBeanLocal regularGIROSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SACHWebService/SACHWebService.wsdl")
    private SACHWebService_Service service_sach;

    @EJB
    private OtherBankPayeeSessionBeanLocal otherBankPayeeSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String toCurrency;
    private String fromBankAccountNumWithType;
    private String fromCurrency;
    private Double transferAmt;
    private String transferMethod;
    private boolean visible;
    private String transferFrequency;
    private boolean transferMethodRender;
    private String toBankName;
    private String statusMessage;
    private String fromBankAccountAvailableBalance;
    private String fromBankAccountTotalBalance;
    private String customerIdentificationNum;
    private String toOtherBankAccountNum;

    private ExternalContext ec;

    public EmployeeRegularGIROTransferDoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
        fromAccounts = new HashMap<String, String>();
        List<OtherBankPayee> otherBankPayees = otherBankPayeeSessionBeanLocal.retrieveOtherBankPayeeByCusId(customerBasic.getCustomerBasicId());

        for (int i = 0; i < bankAccounts.size(); i++) {
            fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
        }
    }

    public void show() {
        if (transferMethod.equals("One Time")) {
            visible = false;
            transferMethodRender = false;
        } else {
            visible = true;
            transferMethodRender = true;
        }
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Double getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(Double transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getTransferFrequency() {
        return transferFrequency;
    }

    public void setTransferFrequency(String transferFrequency) {
        this.transferFrequency = transferFrequency;
    }

    public boolean isTransferMethodRender() {
        return transferMethodRender;
    }

    public void setTransferMethodRender(boolean transferMethodRender) {
        this.transferMethodRender = transferMethodRender;
    }

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getFromBankAccountAvailableBalance() {
        return fromBankAccountAvailableBalance;
    }

    public void setFromBankAccountAvailableBalance(String fromBankAccountAvailableBalance) {
        this.fromBankAccountAvailableBalance = fromBankAccountAvailableBalance;
    }

    public String getFromBankAccountTotalBalance() {
        return fromBankAccountTotalBalance;
    }

    public void setFromBankAccountTotalBalance(String fromBankAccountTotalBalance) {
        this.fromBankAccountTotalBalance = fromBankAccountTotalBalance;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getToOtherBankAccountNum() {
        return toOtherBankAccountNum;
    }

    public void setToOtherBankAccountNum(String toOtherBankAccountNum) {
        this.toOtherBankAccountNum = toOtherBankAccountNum;
    }

    public void regularGIROTransfer() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        String fromBankAccountNum = handleAccountString(fromBankAccountNumWithType);

        OtherBankPayee otherBankPayee = otherBankPayeeSessionBeanLocal.retrieveOtherBankPayeeByNum(toOtherBankAccountNum);
        String payeeName = otherBankPayee.getOtherBankPayeeName();

        BankAccount fromBankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromBankAccountNum);
        Double currentAvailableBankAccountBalance = Double.valueOf(fromBankAccount.getAvailableBankAccountBalance()) - transferAmt;
        bankAccountSessionBeanLocal.updateBankAccountAvailableBalance(fromBankAccountNum, currentAvailableBankAccountBalance.toString());

        if (transferMethod.equals("One Time")) {
            transferFrequency = "One Time";
        }

        Long regularGIROId = regularGIROSessionBeanLocal.addNewRegularGRIO(fromBankAccountNum,
                fromBankAccountNumWithType, "Regular GIRO", transferAmt.toString(), transferFrequency,
                toBankName, toOtherBankAccountNum, "Active", payeeName, fromBankAccount.getBankAccountId());

        if (toBankName.equals("DBS") && transferMethod.equals("One Time")) {

            sachRegularGIROTransferMTD(fromBankAccountNum, toOtherBankAccountNum, transferAmt);

            otherBankPayeeSessionBeanLocal.updateLastTransactionDate(toOtherBankAccountNum);

            statusMessage = "Your transaction has been completed.";
            fromBankAccountAvailableBalance = currentAvailableBankAccountBalance.toString();
            fromBankAccountTotalBalance = fromBankAccount.getTotalBankAccountBalance();

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("toBankAccountNumWithType", toOtherBankAccountNum);
            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
            ec.getFlash().put("transferAmt", transferAmt);
            ec.getFlash().put("fromBankAccountAvailableBalance", fromBankAccountAvailableBalance);
            ec.getFlash().put("fromBankAccountTotalBalance", fromBankAccountTotalBalance);

            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/payment/employeeRegularGIROTransferFinal.xhtml?faces-redirect=true");

        } else if (toBankName.equals("DBS") && !transferMethod.equals("One Time")) {

            sachRegularGIROTransferMTD(fromBankAccountNum, toOtherBankAccountNum, transferAmt);

            regularGIROSessionBeanLocal.updatePaymentAmt(regularGIROId, transferAmt.toString());
            otherBankPayeeSessionBeanLocal.updateLastTransactionDate(toOtherBankAccountNum);

            statusMessage = "Your transaction has been completed.";
            fromBankAccountAvailableBalance = currentAvailableBankAccountBalance.toString();
            fromBankAccountTotalBalance = fromBankAccount.getTotalBankAccountBalance();

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("toBankAccountNumWithType", toOtherBankAccountNum);
            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
            ec.getFlash().put("transferAmt", transferAmt);
            ec.getFlash().put("fromBankAccountAvailableBalance", fromBankAccountAvailableBalance);
            ec.getFlash().put("fromBankAccountTotalBalance", fromBankAccountTotalBalance);

            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/payment/employeeRegularGIROTransferFinal.xhtml?faces-redirect=true");

        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

    private void sachRegularGIROTransferMTD(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
        port.sachRegularGIROTransferMTD(fromBankAccountNum, toBankAccountNum, transferAmt);
    }
}
