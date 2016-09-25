package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBean;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "changeBankAccountPwdManagedBean")
@RequestScoped

public class ChangeBankAccountPwdManagedBean {
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String bankAccountNum;
    private String bankAccountNumWithType;
    private Map<String, String> bankAccountNums = new HashMap<String, String>();
    private String oldPassword;
    private String newPassword;
    private String confirmPwd;

    private ExternalContext ec;

    public ChangeBankAccountPwdManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            bankAccountNums = new HashMap<String, String>();

            for (int i = 0; i < bankAccounts.size(); i++) {
                bankAccountNums.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }
        }
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public Map<String, String> getBankAccountNums() {
        return bankAccountNums;
    }

    public void setBankAccountNums(Map<String, String> bankAccountNums) {
        this.bankAccountNums = bankAccountNums;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public void submit() {
        System.out.println("=");
        System.out.println("====== deposit/ChangeBankAccountPwdManagedBean: submit() ======");
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        bankAccountNum = handleAccountString(bankAccountNumWithType);

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        String hashedPwd = "";

        try {
            hashedPwd = md5Hashing(oldPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BankAccountSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (bankAccount.getBankAccountPwd().equals(hashedPwd)) {
            bankAccountSessionBeanLocal.updatePwd(bankAccountNum, newPassword);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Update Password Successfully!", "Successfully!"));
            loggingSessionBeanLocal.createNewLogging("customer",customerBasic.getCustomerBasicId(), "change deposit account password","successful",null);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your old password is wrong.", "Failed!"));
            loggingSessionBeanLocal.createNewLogging("customer",customerBasic.getCustomerBasicId(), "change deposit account password","failed","Wrong old password");
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

}
