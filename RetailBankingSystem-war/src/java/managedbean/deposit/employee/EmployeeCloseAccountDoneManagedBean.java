package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Payee;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
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

@Named(value = "employeeCloseAccountDoneManagedBean")
@RequestScoped

public class EmployeeCloseAccountDoneManagedBean {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private PayeeSessionBeanLocal payeeSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    private ExternalContext ec;

    private BankAccount bankAccount;
    private String bankAccountNumWithType;
    private String bankAccountNum;
    private String bankAccountType;
    private String onlyOneAccount;
    private String statusMessage;
    private String customerIdentificationNum;
    private String confirmBankAccountPwd;
    private boolean checkOnlyOneAccount;

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

    public ExternalContext getEc() {
        return ec;
    }

    public void setEc(ExternalContext ec) {
        this.ec = ec;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
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

    public String getOnlyOneAccount() {
        return onlyOneAccount;
    }

    public void setOnlyOneAccount(String onlyOneAccount) {
        this.onlyOneAccount = onlyOneAccount;
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

    public String getConfirmBankAccountPwd() {
        return confirmBankAccountPwd;
    }

    public void setConfirmBankAccountPwd(String confirmBankAccountPwd) {
        this.confirmBankAccountPwd = confirmBankAccountPwd;
    }

    public boolean isCheckOnlyOneAccount() {
        return checkOnlyOneAccount;
    }

    public void setCheckOnlyOneAccount(boolean checkOnlyOneAccount) {
        this.checkOnlyOneAccount = checkOnlyOneAccount;
    }

    public Map<String, String> getMyBankAccounts() {
        return myBankAccounts;
    }

    public void setMyBankAccounts(Map<String, String> myBankAccounts) {
        this.myBankAccounts = myBankAccounts;
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

        checkOnlyOneAccount = bankAccountSessionBeanLocal.checkOnlyOneAccount(customerBasic.getCustomerIdentificationNum());

        if (onlyOneAccount.equals("Yes") && !checkOnlyOneAccount) {
            if (!bankAccount.getBankAccountBalance().equals("0")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please withdraw all your money.", "Failed!"));
            } else {

                bankAccountSessionBeanLocal.deleteAccount(bankAccountNum);
                statusMessage = "Account has been successfully deleted.";
                loggingSessionBeanLocal.createNewLogging("employee", null, "close account", "successful", null);

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("bankAccountNum", bankAccountNum);
                ec.getFlash().put("bankAccountType", bankAccountType);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeDeleteAccount.xhtml?faces-redirect=true");
            }

        } else if (onlyOneAccount.equals("Yes") && checkOnlyOneAccount) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You only have one account.", "Failed!"));
        } else if (onlyOneAccount.equals("No") && checkOnlyOneAccount) {

            if (!bankAccount.getBankAccountBalance().equals("0")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please withdraw all your money.", "Failed!"));
            } else {

                if (!customerBasic.getPayee().isEmpty()) {
                    List<Payee> payees = customerBasic.getPayee();
                    String payeeAccountNum = "";

                    for (int i = customerBasic.getPayee().size() - 1; i >= 0; i--) {
                        payeeAccountNum = payees.get(i).getPayeeAccountNum();
                        payeeSessionBeanLocal.deletePayee(payeeAccountNum);
                    }
                }

                bankAccountSessionBeanLocal.deleteAccount(bankAccountNum);
                customerSessionBeanLocal.deleteCustomerBasic(customerIdentificationNum);
                statusMessage = "Account has been successfully deleted.";

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("bankAccountNum", bankAccountNum);
                ec.getFlash().put("bankAccountType", bankAccountType);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeDeleteAccount.xhtml?faces-redirect=true");
            }
        } else if (onlyOneAccount.equals("No") && !checkOnlyOneAccount) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You have more than one accounts.", "Failed!"));
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
