package managedbean.deposit;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Interest;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;

@Named(value = "cashManagedBean")
@RequestScoped

public class CashManagedBean {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionLocal;

    private String depositModel;
    private Double depositAmt;
    private String depositAccountNum;
    private String depositAccountPwd;
    private String confirmDepositAccountPwd;

    private Double withdrawAmt;
    private String withdrawAccountNum;
    private String withdrawAccountPwd;
    private String confirmWithdrawAccountPwd;

    private String statusMessage;
    private Long transactionId;

    private ExternalContext ec;
    //ec = FacesContext.getCurrentInstance().getExternalContext();

    private BankAccount bankAccount;

    public CashManagedBean() {
    }

    public String getDepositModel() {
        return depositModel;
    }

    public void setDepositModel(String depositModel) {
        this.depositModel = depositModel;
    }

    public Double getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(Double depositAmt) {
        this.depositAmt = depositAmt;
    }

    public String getDepositAccountNum() {
        return depositAccountNum;
    }

    public void setDepositAccountNum(String depositAccountNum) {
        this.depositAccountNum = depositAccountNum;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDepositAccountPwd() {
        return depositAccountPwd;
    }

    public void setDepositAccountPwd(String depositAccountPwd) {
        this.depositAccountPwd = depositAccountPwd;
    }

    public Double getWithdrawAmt() {
        return withdrawAmt;
    }

    public void setWithdrawAmt(Double withdrawAmt) {
        this.withdrawAmt = withdrawAmt;
    }

    public String getWithdrawAccountNum() {
        return withdrawAccountNum;
    }

    public void setWithdrawAccountNum(String withdrawAccountNum) {
        this.withdrawAccountNum = withdrawAccountNum;
    }

    public String getWithdrawAccountPwd() {
        return withdrawAccountPwd;
    }

    public void setWithdrawAccountPwd(String withdrawAccountPwd) {
        this.withdrawAccountPwd = withdrawAccountPwd;
    }

    public String getConfirmDepositAccountPwd() {
        return confirmDepositAccountPwd;
    }

    public void setConfirmDepositAccountPwd(String confirmDepositAccountPwd) {
        this.confirmDepositAccountPwd = confirmDepositAccountPwd;
    }

    public String getConfirmWithdrawAccountPwd() {
        return confirmWithdrawAccountPwd;
    }

    public void setConfirmWithdrawAccountPwd(String confirmWithdrawAccountPwd) {
        this.confirmWithdrawAccountPwd = confirmWithdrawAccountPwd;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void cashDeposit() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(depositAccountNum);
        String passwordCheck = transactionSessionLocal.checkPassword(depositAccountNum, depositAccountPwd);
        String activationCheck;

        if (passwordCheck.equals("Password is incorrect!")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your password is incorrect.", "Your password is incorrect!"));
        } else if (passwordCheck.equals("Error! Bank account does not exist!")) {
            withdrawAccountNum = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your bank account does not exits.", "Bank account does not exist!"));
        } else {

            if (bankAccount.getBankAccountStatus().equals("Activated")) {
                transactionId = transactionSessionLocal.cashDeposit(depositAccountNum, depositAmt.toString());

                statusMessage = "Cash deposit Successfully!";

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("depositAccountNum", depositAccountNum);
                ec.getFlash().put("depositAmt", depositAmt);
                ec.getFlash().put("transactionId", transactionId);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeDepositDone.xhtml?faces-redirect=true");
            } else if (bankAccount.getBankAccountStatus().equals("Inactivated")) {

                activationCheck = transactionSessionLocal.checkAccountActivation(depositAccountNum, depositAmt.toString());

                if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                    if (bankAccount.getBankAccountType().equals("Bonus Savings Account")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Dear customer, minimum initial deposit amount is S$3000", "Failed"));
                    } else if (bankAccount.getBankAccountType().equals("Basic Savings Account")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Dear customer, minimum initial deposit amount is S$1", "Failed"));
                    } else if (bankAccount.getBankAccountType().equals("Fixed Deposit Account")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Dear customer, minimum initial deposit amount is S$1000", "Failed"));
                    }
                } else if (activationCheck.equals("Please declare your deposit period")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please declare your fixed deposit period first.", "Failed"));
                } else if (activationCheck.equals("Activated successfully.")) {

                    transactionId = transactionSessionLocal.cashDeposit(depositAccountNum, depositAmt.toString());

                    statusMessage = "Cash deposit Successfully!";

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("depositAccountNum", depositAccountNum);
                    ec.getFlash().put("depositAmt", depositAmt);
                    ec.getFlash().put("transactionId", transactionId);

                    ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeDepositDone.xhtml?faces-redirect=true");
                }
            }

        }
    }

    public void cashWithdraw() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(withdrawAccountNum);

        if (bankAccount.getBankAccountStatus().equals("Inactivated")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account has not been activated.", "Failed"));
        }

        String passwordCheck = transactionSessionLocal.checkPassword(withdrawAccountNum, withdrawAccountPwd);

        if (passwordCheck.equals("Password is incorrect!")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your password is incorrect.", "Your password is incorrect!"));
        } else if (passwordCheck.equals("Error! Bank account does not exist!")) {
            withdrawAccountNum = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your bank account does not exist.", "Bank account does not exist!"));
        } else {
            Double diffAmt = Double.valueOf(bankAccount.getBankAccountBalance()) - withdrawAmt;
            if (diffAmt >= 0) {
                transactionId = transactionSessionLocal.cashWithdraw(withdrawAccountNum, withdrawAmt.toString());
                statusMessage = "Cash withdraw Successfully!";

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("withdrawAccountNum", withdrawAccountNum);
                ec.getFlash().put("withdrawAmt", withdrawAmt);
                ec.getFlash().put("transactionId", transactionId);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeWithdrawDone.xhtml?faces-redirect=true");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account balance is insufficient.", "Your account balance is insufficient."));
            }
        }
    }
}
