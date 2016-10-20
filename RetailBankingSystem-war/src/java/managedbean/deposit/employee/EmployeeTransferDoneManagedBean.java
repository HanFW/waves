package managedbean.deposit.employee;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeTransferDoneManagedBean")
@RequestScoped

public class EmployeeTransferDoneManagedBean {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String fromAccount;
    private String fromCurrency;
    private String toAccount;
    private String toCurrency;
    private Double transferAmt;
    private String fromBankAccountNumWithType;
    private String toBankAccountNumWithType;
    private Long newTransactionId;
    private String fromAccountBalance;

    private String statusMessage;
    private String customerIdentificationNum;

    private ExternalContext ec;

    public EmployeeTransferDoneManagedBean() {
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(Double transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public Long getNewTransactionId() {
        return newTransactionId;
    }

    public void setNewTransactionId(Long newTransactionId) {
        this.newTransactionId = newTransactionId;
    }

    public String getFromAccountBalance() {
        return fromAccountBalance;
    }

    public void setFromAccountBalance(String fromAccountBalance) {
        this.fromAccountBalance = fromAccountBalance;
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

    public void transfer() throws IOException {

        BankAccount bankAccountFrom = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccount);

        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (bankAccountFrom.getBankAccountId() == null || bankAccountTo.getBankAccountId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your deposit account does not exist.", "Failed!"));
        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer is not allowed transferring fund to a fixed deposit account. ", "Failed!"));
        } else {

            if (Double.valueOf(bankAccountFrom.getTransferBalance()) < transferAmt) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transfer amount has been exceeded your daily transfer limit.", "Failed!"));
            } else {

                if (fromAccount.equals(toAccount)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
                } else {
                    if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {

                        activationCheck = transactionSessionBeanLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt.toString());

                        if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                            if (bankAccountTo.getBankAccountType().equals("Bonus Savings Account")) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Minimum initial deposit amount is S$3000", "Failed"));
                            } else if (bankAccountTo.getBankAccountType().equals("Basic Savings Account")) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Minimum initial deposit amount is S$1", "Failed"));
                            } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Minimum initial deposit amount is S$1000", "Failed"));
                            }
                        } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                        } else if (activationCheck.equals("Please declare your deposit period")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please declare your fixed deposit period first.", "Failed"));
                        } else if (activationCheck.equals("Activated successfully.")) {
                            Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                            toBankAccountNumWithType = toAccount + "-" + bankAccountTo.getBankAccountType();
                            fromBankAccountNumWithType = fromAccount + "-" + bankAccountFrom.getBankAccountType();

                            if (diffAmt >= 0) {

                                Double currentBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                                newTransactionId = transactionSessionBeanLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                                statusMessage = "Your transaction has been completed.";

                                Double fromAccountBalanceDouble = currentBalance - transferAmt;
                                fromAccountBalance = fromAccountBalanceDouble.toString();

                                ec.getFlash().put("statusMessage", statusMessage);
                                ec.getFlash().put("newTransactionId", newTransactionId);
                                ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                                ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                                ec.getFlash().put("transferAmt", transferAmt);
                                ec.getFlash().put("fromAccount", fromAccount);
                                ec.getFlash().put("toAccount", toAccount);
                                ec.getFlash().put("fromAccountBalance", fromAccountBalance);

                                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeTransferFinished.xhtml?faces-redirect=true");
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your account balance is insufficient.", "Failed!"));
                            }
                        }
                    } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You account(from) has not been activated.", "Failed!"));
                    } else if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                        Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                        toBankAccountNumWithType = toAccount + "-" + bankAccountTo.getBankAccountType();
                        fromBankAccountNumWithType = fromAccount + "-" + bankAccountFrom.getBankAccountType();

                        if (diffAmt >= 0) {

                            Double currentBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                            newTransactionId = transactionSessionBeanLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                            statusMessage = "Your transaction has been completed.";

                            Double fromAccountBalanceDouble = currentBalance - transferAmt;
                            fromAccountBalance = fromAccountBalanceDouble.toString();

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newTransactionId", newTransactionId);
                            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                            ec.getFlash().put("transferAmt", transferAmt);
                            ec.getFlash().put("fromAccount", fromAccount);
                            ec.getFlash().put("toAccount", toAccount);
                            ec.getFlash().put("fromAccountBalance", fromAccountBalance);

                            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeTransferFinished.xhtml?faces-redirect=true");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your account balance is insufficient.", "Failed!"));
                        }
                    } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Both of accounts have not been activated.", "Failed!"));
                    }
                }
            }
        }
    }
}
