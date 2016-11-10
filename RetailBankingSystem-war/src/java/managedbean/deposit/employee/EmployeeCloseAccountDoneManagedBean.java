package managedbean.deposit.employee;

import ejb.bi.session.DepositAccountClosureSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.InterestSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeCloseAccountDoneManagedBean")
@RequestScoped

public class EmployeeCloseAccountDoneManagedBean {

    @EJB
    private DepositAccountClosureSessionBeanLocal depositAccountClosureSessionBeanLocal;

    @EJB
    private InterestSessionBeanLocal interestSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private ExternalContext ec;
    private BankAccount bankAccount;
    private String bankAccountNumWithType;
    private String bankAccountNum;
    private String bankAccountType;
    private String statusMessage;
    private String customerIdentificationNum;
    private String customerName;
    private Long interestId;
    private boolean checkOnlyOneAccount;
    private String reasonOfAccountClosure;
    private String customerMobile;
    private String customerAddress;
    private boolean agreement;
    private String customerSignature;

    private Map<String, String> myBankAccounts = new HashMap<String, String>();

    public EmployeeCloseAccountDoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
        myBankAccounts = new HashMap<String, String>();

        for (int j = 0; j < bankAccounts.size(); j++) {
            myBankAccounts.put(bankAccounts.get(j).getBankAccountType() + "-" + bankAccounts.get(j).getBankAccountNum(), bankAccounts.get(j).getBankAccountType() + "-" + bankAccounts.get(j).getBankAccountNum());
        }

    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCustomerIdentificationNum() {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        customerIdentificationNum = customerBasic.getCustomerIdentificationNum();

        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public Map<String, String> getMyBankAccounts() {
        return myBankAccounts;
    }

    public void setMyBankAccounts(Map<String, String> myBankAccounts) {
        this.myBankAccounts = myBankAccounts;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public String getCustomerName() {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        customerName = customerBasic.getCustomerName();

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

    public boolean isCheckOnlyOneAccount() {
        return checkOnlyOneAccount;
    }

    public void setCheckOnlyOneAccount(boolean checkOnlyOneAccount) {
        this.checkOnlyOneAccount = checkOnlyOneAccount;
    }

    public String getReasonOfAccountClosure() {
        return reasonOfAccountClosure;
    }

    public void setReasonOfAccountClosure(String reasonOfAccountClosure) {
        this.reasonOfAccountClosure = reasonOfAccountClosure;
    }

    public String getCustomerMobile() {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        customerMobile = customerBasic.getCustomerMobile();

        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        customerAddress = customerBasic.getCustomerAddress();

        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public void deleteAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeCloseAccountDoneManagedBean: deleteAccount() ======");

        ec = FacesContext.getCurrentInstance().getExternalContext();

        bankAccountNum = handleAccountString(bankAccountNumWithType);
        bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        bankAccountType = bankAccount.getBankAccountType();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        customerSignature = ec.getSessionMap().get("customerSignature").toString();
        if (customerSignature.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please provide your digital signature", "Failed!"));
        } else {

            if (agreement) {

                bankAccountNum = handleAccountString(bankAccountNumWithType);
                bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
                bankAccountType = bankAccount.getBankAccountType();

                checkOnlyOneAccount = bankAccountSessionBeanLocal.checkOnlyOneAccount(customerBasic.getCustomerIdentificationNum());

                if (!checkOnlyOneAccount) {
                    if (!bankAccount.getAvailableBankAccountBalance().equals("0.0")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please withdraw all your money.", "Failed!"));
                    } else {

                        String accountStatus = bankAccount.getBankAccountStatus();
                        Calendar cal = Calendar.getInstance();

                        Long newDepositAccountClosureId = depositAccountClosureSessionBeanLocal.addNewDepositAccountClosure(reasonOfAccountClosure,
                                cal.getTimeInMillis(), cal.getTime().toString(), accountStatus);

                        interestId = bankAccount.getInterest().getInterestId();
                        bankAccountSessionBeanLocal.deleteAccount(bankAccountNum);
                        interestSessionBeanLocal.deleteInterest(interestId);
                        statusMessage = "Account has been successfully deleted.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "close account", "successful", null);

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("bankAccountNum", bankAccountNum);
                        ec.getFlash().put("bankAccountType", bankAccountType);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerDeleteAccount.xhtml?faces-redirect=true");
                    }

                } else if (checkOnlyOneAccount) {

                    if (!bankAccount.getAvailableBankAccountBalance().equals("0.0")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please withdraw all your money.", "Failed!"));
                    } else {

                        String accountStatus = bankAccount.getBankAccountStatus();
                        Calendar cal = Calendar.getInstance();

                        Long newDepositAccountClosureId = depositAccountClosureSessionBeanLocal.addNewDepositAccountClosure(reasonOfAccountClosure,
                                cal.getTimeInMillis(), cal.getTime().toString(), accountStatus);

                        interestId = bankAccount.getInterest().getInterestId();
                        customerSessionBeanLocal.deleteCustomerBasic(customerBasic.getCustomerIdentificationNum());
                        interestSessionBeanLocal.deleteInterest(interestId);

                        statusMessage = "Account has been successfully deleted.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "close account", "successful", null);

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("bankAccountNum", bankAccountNum);
                        ec.getFlash().put("bankAccountType", bankAccountType);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerDeleteAccount.xhtml?faces-redirect=true");
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please agree to terms.", "Failed!"));
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
