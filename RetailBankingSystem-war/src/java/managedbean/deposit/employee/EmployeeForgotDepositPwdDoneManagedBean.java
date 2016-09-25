package managedbean.deposit.employee;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeForgotDepositPwdDoneManagedBean")
@RequestScoped

public class EmployeeForgotDepositPwdDoneManagedBean {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private ExternalContext ec;
    
    private String bankAccountNum;
    private String newPassword;
    private String confirmPassword;
    
    public EmployeeForgotDepositPwdDoneManagedBean() {
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public void submit() {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeForgotDepositPwdDoneManagedBean: submit() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        bankAccountNum = ec.getSessionMap().get("bankAccountNum").toString();
        
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        bankAccountSessionBeanLocal.updatePwd(bankAccountNum, newPassword);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have updated your password successfully.", " "));
    }
}
