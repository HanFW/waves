package managedbean;

import entity.BankAccount;
import entity.CustomerBasic;
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
import session.stateless.BankAccountSessionLocal;
import session.stateless.CRMCustomerSessionBeanLocal;
import session.stateless.TransactionSessionLocal;

@Named(value = "closeAccountManagedBean")
@RequestScoped

public class CloseAccountManagedBean {

    @EJB
    private TransactionSessionLocal transactionSessionLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;

    private ExternalContext ec;
    private BankAccount bankAccount;
    private String bankAccountNumWithType;
    private String bankAccountNum;
    private String bankAccountType;
    private String onlyOneAccount;
    private String statusMessage;
    private String customerIdentificationNum;
    private String bankAccountPwd;
    private String confirmBankAccountPwd;
    private boolean checkOnlyOneAccount;

    private Map<String, String> myBankAccounts = new HashMap<String, String>();

    public CloseAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            myBankAccounts = new HashMap<String, String>();

            for (int j = 0; j < bankAccounts.size(); j++) {
                myBankAccounts.put(bankAccounts.get(j).getBankAccountType() + "-" + bankAccounts.get(j).getBankAccountNum(), bankAccounts.get(j).getBankAccountType() + "-" + bankAccounts.get(j).getBankAccountNum());
            }
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

    public String getBankAccountPwd() {
        return bankAccountPwd;
    }

    public void setBankAccountPwd(String bankAccountPwd) {
        this.bankAccountPwd = bankAccountPwd;
    }

    public String getConfirmBankAccountPwd() {
        return confirmBankAccountPwd;
    }

    public void setConfirmBankAccountPwd(String confirmBankAccountPwd) {
        this.confirmBankAccountPwd = confirmBankAccountPwd;
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

    public boolean isCheckOnlyOneAccount() {
        return checkOnlyOneAccount;
    }

    public void setCheckOnlyOneAccount(boolean checkOnlyOneAccount) {
        this.checkOnlyOneAccount = checkOnlyOneAccount;
    }

    public void deleteAccount() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        bankAccountNum = handleAccountString(bankAccountNumWithType);
        bankAccountType = bankAccount.getBankAccountType();
        String passwordCheck = transactionSessionLocal.checkPassword(bankAccountNum, bankAccountPwd);
        bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);

        if (passwordCheck.equals("Error! Bank account does not exist!")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Error! Bank account does not exist!", "Failed!"));
        } else if (passwordCheck.equals("Password is incorrect!")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Password is incorrect!", "Failed!"));
        } else {

            checkOnlyOneAccount = bankAccountSessionLocal.checkOnlyOneAccount(customerBasic.getCustomerIdentificationNum());
          
            if (onlyOneAccount.equals("Yes") && !checkOnlyOneAccount) {
                if (!bankAccount.getBankAccountBalance().equals("0")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please withdraw all your money.", "Failed!"));
                } else {

                    bankAccountSessionLocal.deleteAccount(bankAccountNum);
                    statusMessage = "Account has been successfully deleted.";

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("bankAccountNum", bankAccountNum);
                    ec.getFlash().put("bankAccountType", bankAccountType);

                    ec.redirect("deleteAccount.xhtml?faces-redirect=true");
                }

            } else if (onlyOneAccount.equals("Yes") && checkOnlyOneAccount) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! You only have one account.", "Failed!"));
            } else if (onlyOneAccount.equals("No") && checkOnlyOneAccount) {

                if (!bankAccount.getBankAccountBalance().equals("0")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please withdraw all your money.", "Failed!"));
                } else {
                    bankAccountSessionLocal.deleteAccount(bankAccountNum);
                    customerSessionBeanLocal.deleteCustomerBasic(customerIdentificationNum);
                    statusMessage = "Account has been successfully deleted.";

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("bankAccountNum", bankAccountNum);
                    ec.getFlash().put("bankAccountType", bankAccountType);

                    ec.redirect("deleteAccount.xhtml?faces-redirect=true");
                }
            } else if (onlyOneAccount.equals("No") && !checkOnlyOneAccount) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! You have more than one accounts.", "Failed!"));
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
