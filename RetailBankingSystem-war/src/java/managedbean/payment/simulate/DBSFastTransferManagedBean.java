package managedbean.payment.simulate;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payement.session.OtherBankAccountSessionBeanLocal;
import ejb.payement.session.OtherTransactionSessionBeanLocal;
import ejb.payement.session.SACHSessionBeanLocal;
import ejb.payment.entity.OtherBankAccount;
import java.io.IOException;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "dBSFastTransferManagedBean")
@RequestScoped

public class DBSFastTransferManagedBean {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    private String toAccountNum;
    private String toCurrency;
    private Double transferAmt;
    private String fromAccountNum;
    private String fromCurrency;
    private String statusMessage;
    private String toBankAccountNumWithType;
    private String fromBankAccountNumWithType;
    private Long transactionId;
    private String fromAccountBalance;
    private Double currentBalance;

    private ExternalContext ec;

    public DBSFastTransferManagedBean() {
    }

    public String getToAccountNum() {
        return toAccountNum;
    }

    public void setToAccountNum(String toAccountNum) {
        this.toAccountNum = toAccountNum;
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

    public String getFromAccountNum() {
        return fromAccountNum;
    }

    public void setFromAccountNum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccountBalance() {
        return fromAccountBalance;
    }

    public void setFromAccountBalance(String fromAccountBalance) {
        this.fromAccountBalance = fromAccountBalance;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void transfer() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        OtherBankAccount dbsBankAccountFrom = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccountNum);
        BankAccount merlionBankAccountTo = bankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccountNum);

        Double diffAmt = Double.valueOf(dbsBankAccountFrom.getOtherBankAccountBalance()) - transferAmt;
        if (diffAmt >= 0) {

            currentBalance = Double.valueOf(dbsBankAccountFrom.getOtherBankAccountBalance()) - transferAmt;
            otherBankAccountSessionBeanLocal.updateBankAccountBalance(fromAccountNum, currentBalance.toString());

            Calendar cal = Calendar.getInstance();
            String transactionCode = "ICT";
            String transactionRef = "Transfer to " + merlionBankAccountTo.getBankAccountType() + "-" + merlionBankAccountTo.getBankAccountNum();

            transactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(cal.getTime().toString(), transactionCode,
                    transactionRef, transferAmt.toString(), " ", dbsBankAccountFrom.getOtherBankAccountId());

            sACHSessionBeanLocal.SACHTransferDTM(fromAccountNum, toAccountNum, transferAmt);

            statusMessage = "Your transaction has been completed.";
            fromAccountBalance = dbsBankAccountFrom.getOtherBankAccountBalance();

            toBankAccountNumWithType = merlionBankAccountTo.getBankAccountNum() + "-" + merlionBankAccountTo.getBankAccountType();
            fromBankAccountNumWithType = fromAccountNum + "-" + "DBS Savings Account";

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("transactionId", transactionId);
            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
            ec.getFlash().put("transferAmt", transferAmt);
            ec.getFlash().put("fromAccount", fromAccountNum);
            ec.getFlash().put("toAccount", toAccountNum);
            ec.getFlash().put("fromAccountBalance", fromAccountBalance);

            ec.redirect(ec.getRequestContextPath() + "/web/simulate/payment/dbsFastTransferDone.xhtml?faces-redirect=true");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account balance is insufficient.", "Failed!"));
        }
    }
}
