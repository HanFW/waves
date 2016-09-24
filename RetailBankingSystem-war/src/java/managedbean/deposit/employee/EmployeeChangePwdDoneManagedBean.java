package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBean;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.Serializable;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named(value = "employeeChangePwdDoneManagedBean")
@ViewScoped

public class EmployeeChangePwdDoneManagedBean implements Serializable{
    
    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String bankAccountNum;
    private String bankAccountNumWithType;
    private Map<String, String> bankAccountNums = new HashMap<String, String>();
    private String oldPassword;
    private String newPassword;
    private String confirmPwd;
    
    private String customerIdentificationNum;

    private ExternalContext ec;

    public EmployeeChangePwdDoneManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId()!=null) {
            
            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            
            bankAccountNums = new HashMap<String, String>();

            for (int i = 0; i < bankAccounts.size(); i++) {
                bankAccountNums.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }
        }
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
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
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your old password is wrong.", "Failed!"));
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
