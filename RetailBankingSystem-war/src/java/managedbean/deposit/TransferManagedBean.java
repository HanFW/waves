package managedbean.deposit;

import ejb.deposit.entity.BankAccount;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;

@Named(value = "transferManagedBean")
@RequestScoped

public class TransferManagedBean {

    @EJB
    private PayeeSessionBeanLocal payeeSessionLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionLocal;

    private String fromAccount;
    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private Map<String, String> toAccounts = new HashMap<String, String>();
    private Map<String, String> customerPayees = new HashMap<String, String>();
    private String fromCurrency;
    private String toAccount;
    private String toCurrency;
    private String transferAmt;
    private String payeeName;
    private String fromBankAccountNumWithType;
    private String toBankAccountNumWithType;

    private String statusMessage;

    private ExternalContext ec;

    public TransferManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            fromAccounts = new HashMap<String, String>();
            toAccounts = new HashMap<String, String>();
            customerPayees = new HashMap<String, String>();
            List<Payee> payees = payeeSessionLocal.retrievePayeeByCusId(customerBasic.getCustomerBasicId());

            for (int i = 0; i < bankAccounts.size(); i++) {
                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
                toAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }

            for (int j = 0; j < payees.size(); j++) {
                customerPayees.put(payees.get(j).getPayeeAccountType() + "-" + payees.get(j).getPayeeAccountNum() + "-" + payees.get(j).getPayeeName(), payees.get(j).getPayeeAccountType() + "-" + payees.get(j).getPayeeAccountNum() + "-" + payees.get(j).getPayeeName());
            }
        }
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

    public String getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(String transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public Map<String, String> getToAccounts() {
        return toAccounts;
    }

    public void setToAccounts(Map<String, String> toAccounts) {
        this.toAccounts = toAccounts;
    }

    public Map<String, String> getCustomerPayees() {
        return customerPayees;
    }

    public void setCustomerPayees(Map<String, String> customerPayees) {
        this.customerPayees = customerPayees;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
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

    public void toMyAccount() throws IOException {
        fromAccount = handleAccountString(fromBankAccountNumWithType);
        toAccount = handleAccountString(toBankAccountNumWithType);

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);

        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (fromAccount.equals(toAccount)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
        } else {
            if (bankAccountFrom.getBankAccountStatus().equals("Activated") && bankAccountTo.getBankAccountStatus().equals("Inactivated")) {
                
                activationCheck = transactionSessionLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt);
                
                if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Initial deposit amount is insufficient.", "Failed"));
                } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                } else if (activationCheck.equals("Please declare your deposit period")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Please declare your fixed deposit period first.", "Failed"));
                } else if (activationCheck.equals("Activated successfully.")) {
                    Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);

                    if (diffAmt >= 0) {
                        
                        transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt);
                        statusMessage = "Fund Transfer Successfully!";

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("fromAccount", fromAccount);
                        ec.getFlash().put("toAccount", toAccount);
                        ec.getFlash().put("transferAmt", transferAmt);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.", "Failed!"));
                    }
                }
            } else if (bankAccountFrom.getBankAccountStatus().equals("Inactivated") && bankAccountTo.getBankAccountStatus().equals("Activated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!You account(from) has not been activated.", "Failed!"));
            } else if (bankAccountFrom.getBankAccountStatus().equals("Activated") && bankAccountTo.getBankAccountStatus().equals("Activated")) {
                Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);

                if (diffAmt >= 0) {
                    
                    transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt);
                    statusMessage = "Fund Transfer Successfully!";

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("fromAccount", fromAccount);
                    ec.getFlash().put("toAccount", toAccount);
                    ec.getFlash().put("transferAmt", transferAmt);

                    ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.", "Failed!"));
                }
            } else if (bankAccountFrom.getBankAccountStatus().equals("Inactivated") && bankAccountTo.getBankAccountStatus().equals("Inactivated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Both of accounts have not been activated.", "Failed!"));
            }
        }
    }

    public void toOthersAccount() throws IOException {
        fromAccount = handleAccountString(fromBankAccountNumWithType);
        toAccount = handleAccountString(toBankAccountNumWithType);

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);
        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (fromAccount.equals(toAccount)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
        } else {
            if (bankAccountFrom.getBankAccountStatus().equals("Activated") && bankAccountTo.getBankAccountStatus().equals("Inactivated")) {
                
                activationCheck = transactionSessionLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt);
                
                if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Initial deposit amount is insufficient.", "Failed"));
                } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                } else if (activationCheck.equals("Please declare your deposit period")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Please declare your fixed deposit period first.", "Failed"));
                } else if (activationCheck.equals("Activated successfully.")) {
                    Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);

                    if (diffAmt >= 0) {
                        transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt);
                        payeeSessionLocal.updateLastTransactionDate(toAccount);
                        
                        statusMessage = "Fund Transfer Successfully!";

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("fromAccount", fromAccount);
                        ec.getFlash().put("toAccount", toAccount);
                        ec.getFlash().put("transferAmt", transferAmt);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.", "Failed!"));
                    }
                }
            } else if (bankAccountFrom.getBankAccountStatus().equals("Inactivated") && bankAccountTo.getBankAccountStatus().equals("Activated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!You account(from) has not been activated.", "Failed!"));
            } else if (bankAccountFrom.getBankAccountStatus().equals("Activated") && bankAccountTo.getBankAccountStatus().equals("Activated")) {
                Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);

                if (diffAmt >= 0) {
                    transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt);
                    payeeSessionLocal.updateLastTransactionDate(toAccount);
                    
                    statusMessage = "Fund Transfer Successfully!";

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("fromAccount", fromAccount);
                    ec.getFlash().put("toAccount", toAccount);
                    ec.getFlash().put("transferAmt", transferAmt);

                    ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.", "Failed!"));
                }
            } else if (bankAccountFrom.getBankAccountStatus().equals("Inactivated") && bankAccountTo.getBankAccountStatus().equals("Inactivated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Both of accounts have not been activated.", "Failed!"));
            }
        }
    }

    public void oneTimeTransfer() throws IOException {
        fromAccount = handleAccountString(fromBankAccountNumWithType);

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);
        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();
        String transferMessage;

        if (fromAccount.equals(toAccount)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
        } else {
            if (bankAccountFrom.getBankAccountStatus().equals("Activated") && bankAccountTo.getBankAccountStatus().equals("Inactivated")) {

                activationCheck = transactionSessionLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt);

                if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Initial deposit amount is insufficient.", "Failed"));
                } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                } else if (activationCheck.equals("Please declare your deposit period")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Please declare your fixed deposit period first.", "Failed"));
                } else if (activationCheck.equals("Activated successfully.")) {

                    Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);

                    if (diffAmt >= 0) {
                        transferMessage = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt);

                        if (transferMessage.equals("Fund Transfer Sucessfully!")) {
                            statusMessage = "Fund Transfer Successfully!";

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("fromAccount", fromAccount);
                            ec.getFlash().put("toAccount", toAccount);
                            ec.getFlash().put("transferAmt", transferAmt);

                            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Transfer failed.", "Failed!"));
                        }

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.", "Failed!"));
                    }
                }
            } else if (bankAccountFrom.getBankAccountStatus().equals("Inactivated") && bankAccountTo.getBankAccountStatus().equals("Activated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!You account(from) has not been activated.", "Failed!"));
            } else if (bankAccountFrom.getBankAccountStatus().equals("Activated") && bankAccountTo.getBankAccountStatus().equals("Activated")) {

                Double diffAmt = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);

                if (diffAmt >= 0) {
                    transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt);
                    statusMessage = "Fund Transfer Successfully!";

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("fromAccount", fromAccount);
                    ec.getFlash().put("toAccount", toAccount);
                    ec.getFlash().put("transferAmt", transferAmt);

                    ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Your account balance is insufficient.", "Failed!"));
                }
            } else if (bankAccountFrom.getBankAccountStatus().equals("Inactivated") && bankAccountTo.getBankAccountStatus().equals("Inactivated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!Both of accounts have not been activated.", "Failed!"));
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
