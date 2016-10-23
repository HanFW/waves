package managedbean.deposit.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.InterestSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
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

@Named(value = "closeAccountManagedBean")
@RequestScoped

public class CloseAccountManagedBean {

    @EJB
    private InterestSessionBeanLocal interestSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

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

    private Map<String, String> myBankAccounts = new HashMap<String, String>();

    public CloseAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
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

    public void deleteAccount() throws IOException {

        System.out.println("=");
        System.out.println("====== deposit/CloseAccountManagedBean: deleteAccount() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        bankAccountNum = handleAccountString(bankAccountNumWithType);
        bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        bankAccountType = bankAccount.getBankAccountType();

        checkOnlyOneAccount = bankAccountSessionLocal.checkOnlyOneAccount(customerBasic.getCustomerIdentificationNum());

        if (!checkOnlyOneAccount) {
            if (!bankAccount.getAvailableBankAccountBalance().equals("0.0")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please withdraw all your money.", "Failed!"));
            } else {

                interestId = bankAccount.getInterest().getInterestId();
                bankAccountSessionLocal.deleteAccount(bankAccountNum);
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
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
