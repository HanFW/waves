package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "forgotPasswordDoneManagedBean")
@RequestScoped

public class ForgotPasswordDoneManagedBean {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private ExternalContext ec;

    private String newPassword;
    private String confirmPassword;
    private String bankAccountNum;

    public ForgotPasswordDoneManagedBean() {
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

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public void submit() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        bankAccountNum = ec.getSessionMap().get("bankAccountNum").toString();

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        bankAccountSessionBeanLocal.updatePwd(bankAccountNum, newPassword);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have updated your password successfully.", " "));
    }
}
