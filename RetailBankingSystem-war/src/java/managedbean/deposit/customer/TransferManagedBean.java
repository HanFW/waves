package managedbean.deposit.customer;

import ejb.deposit.entity.BankAccount;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.RegularPayee;
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
import javax.inject.Named;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.deposit.session.RegularPayeeSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;

@Named(value = "transferManagedBean")
@RequestScoped

public class TransferManagedBean {

    @EJB
    private RegularPayeeSessionBeanLocal regularPayeeSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

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
    private Double transferAmt;
    private String payeeName;
    private String fromBankAccountNumWithType;
    private String toBankAccountNumWithType;
    private Long newTransactionId;
    private String fromAccountAvailableBalance;
    private String fromAccountTotalBalance;
    private String fromAccountDefaultTransferLimit;
    private String fromAccountRemainingTransferLimit;

    private boolean dailyTransferRender = false;

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
            List<RegularPayee> regularPayees = regularPayeeSessionBeanLocal.retrieveRegularPayeeByCusId(customerBasic.getCustomerBasicId());

            for (int i = 0; i < bankAccounts.size(); i++) {
                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }

            for (int a = 0; a < bankAccounts.size(); a++) {
                if (!bankAccounts.get(a).getBankAccountType().equals("Fixed Deposit Account")) {
                    toAccounts.put(bankAccounts.get(a).getBankAccountType() + "-" + bankAccounts.get(a).getBankAccountNum(), bankAccounts.get(a).getBankAccountType() + "-" + bankAccounts.get(a).getBankAccountNum());
                }
            }

            for (int j = 0; j < regularPayees.size(); j++) {
                customerPayees.put(regularPayees.get(j).getPayeeAccountType() + "-" + regularPayees.get(j).getPayeeAccountNum() + "-" + regularPayees.get(j).getPayeeName(), regularPayees.get(j).getPayeeAccountType() + "-" + regularPayees.get(j).getPayeeAccountNum() + "-" + regularPayees.get(j).getPayeeName());
            }
        }
    }

    public void show() {
        dailyTransferRender = true;
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

    public Long getNewTransactionId() {
        return newTransactionId;
    }

    public void setNewTransactionId(Long newTransactionId) {
        this.newTransactionId = newTransactionId;
    }

    public boolean isDailyTransferRender() {
        return dailyTransferRender;
    }

    public void setDailyTransferRender(boolean dailyTransferRender) {
        this.dailyTransferRender = dailyTransferRender;
    }

    public String getFromAccountAvailableBalance() {
        return fromAccountAvailableBalance;
    }

    public void setFromAccountAvailableBalance(String fromAccountAvailableBalance) {
        this.fromAccountAvailableBalance = fromAccountAvailableBalance;
    }

    public String getFromAccountTotalBalance() {
        return fromAccountTotalBalance;
    }

    public void setFromAccountTotalBalance(String fromAccountTotalBalance) {
        this.fromAccountTotalBalance = fromAccountTotalBalance;
    }

    public String getFromAccountDefaultTransferLimit() {

        if (fromBankAccountNumWithType != null) {
            fromAccount = handleAccountString(fromBankAccountNumWithType);
            BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
            fromAccountDefaultTransferLimit = bankAccountFrom.getTransferDailyLimit();
        } else {
            fromAccountDefaultTransferLimit = "3000.0";
        }

        return fromAccountDefaultTransferLimit;
    }

    public void setFromAccountDefaultTransferLimit(String fromAccountDefaultTransferLimit) {
        this.fromAccountDefaultTransferLimit = fromAccountDefaultTransferLimit;
    }

    public String getFromAccountRemainingTransferLimit() {

        if (fromBankAccountNumWithType != null) {
            fromAccount = handleAccountString(fromBankAccountNumWithType);
            BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
            fromAccountRemainingTransferLimit = bankAccountFrom.getTransferBalance();
        } else {
            fromAccountRemainingTransferLimit = "3000.0";
        }

        return fromAccountRemainingTransferLimit;
    }

    public void setFromAccountRemainingTransferLimit(String fromAccountRemainingTransferLimit) {
        this.fromAccountRemainingTransferLimit = fromAccountRemainingTransferLimit;
    }

    public void toMyAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/TransferManagedBean: toMyAccount() ======");
        fromAccount = handleAccountString(fromBankAccountNumWithType);
        toAccount = handleAccountString(toBankAccountNumWithType);

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);

        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (Double.valueOf(bankAccountFrom.getTransferBalance()) < transferAmt) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dear Customer, your transfer amount has been exceeded your daily transfer limit. "
                    + "You remaining daily limit is S$" + bankAccountFrom.getTransferBalance(), "Failed!"));
        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dear Customer, you are not allowed transferring fund to a fixed deposit account. ", "Failed!"));
        } else {

            if (fromAccount.equals(toAccount)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
            } else {
                if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {

                    activationCheck = transactionSessionLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt.toString());

                    if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                        if (bankAccountTo.getBankAccountType().equals("Bonus Savings Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$3000", "Failed"));
                        } else if (bankAccountTo.getBankAccountType().equals("Basic Savings Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$1", "Failed"));
                        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$1000", "Failed"));
                        }
                    } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                    } else if (activationCheck.equals("Please declare your deposit period")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please declare your fixed deposit period first.", "Failed"));
                    } else if (activationCheck.equals("Activate successfully.")) {
                        Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                        if (diffAmt >= 0) {

                            Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                            Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                            newTransactionId = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                            statusMessage = "Your transaction has been completed.";
                            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "transfer to my account", "successful", null);

                            Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                            Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                            fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                            fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newTransactionId", newTransactionId);
                            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                            ec.getFlash().put("transferAmt", transferAmt);
                            ec.getFlash().put("fromAccount", fromAccount);
                            ec.getFlash().put("toAccount", toAccount);
                            ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                            ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
                        }
                    }
                } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!You account(from) has not been activated.", "Failed!"));
                } else if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                    Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                    if (diffAmt >= 0) {

                        Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                        Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                        newTransactionId = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                        statusMessage = "Your transaction has been completed.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "transfer to my account", "successful", null);

                        Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                        Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                        fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                        fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("newTransactionId", newTransactionId);
                        ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                        ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                        ec.getFlash().put("transferAmt", transferAmt);
                        ec.getFlash().put("fromAccount", fromAccount);
                        ec.getFlash().put("toAccount", toAccount);
                        ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                        ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
                    }
                } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Both of accounts have not been activated.", "Failed!"));
                }
            }
        }
    }

    public void toOthersAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/TransferManagedBean: toOthersAccount() ======");
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        fromAccount = handleAccountString(fromBankAccountNumWithType);
        toAccount = handleAccountString(toBankAccountNumWithType);

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);
        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (Double.valueOf(bankAccountFrom.getTransferBalance()) < transferAmt) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dear Customer, your transfer amount has been exceeded your daily transfer limit. "
                    + "You remaining daily limit is S$" + bankAccountFrom.getTransferBalance(), "Failed!"));
        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dear Customer, you are not allowed transferring fund to a fixed deposit account. ", "Failed!"));
        } else {

            if (fromAccount.equals(toAccount)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
            } else {
                if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {

                    activationCheck = transactionSessionLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt.toString());

                    if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                        if (bankAccountTo.getBankAccountType().equals("Bonus Savings Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$3000", "Failed"));
                        } else if (bankAccountTo.getBankAccountType().equals("Basic Savings Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$1", "Failed"));
                        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$1000", "Failed"));
                        }
                    } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                    } else if (activationCheck.equals("Please declare your deposit period")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please declare your fixed deposit period first.", "Failed"));
                    } else if (activationCheck.equals("Activated successfully.")) {
                        Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                        if (diffAmt >= 0) {

                            Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                            Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                            newTransactionId = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                            payeeSessionLocal.updateLastTransactionDate(toAccount);

                            statusMessage = "Your transaction has been completed.";
                            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "transfer to other account", "successful", null);

                            Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                            Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                            fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                            fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newTransactionId", newTransactionId);
                            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                            ec.getFlash().put("transferAmt", transferAmt);
                            ec.getFlash().put("fromAccount", fromAccount);
                            ec.getFlash().put("toAccount", toAccount);
                            ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                            ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
                        }
                    }
                } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!You account(from) has not been activated.", "Failed!"));
                } else if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                    Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                    if (diffAmt >= 0) {

                        Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                        Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                        newTransactionId = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                        payeeSessionLocal.updateLastTransactionDate(toAccount);

                        statusMessage = "Your transaction has been completed.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "transfer to other account", "successful", null);

                        Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                        Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                        fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                        fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("newTransactionId", newTransactionId);
                        ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                        ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                        ec.getFlash().put("transferAmt", transferAmt);
                        ec.getFlash().put("fromAccount", fromAccount);
                        ec.getFlash().put("toAccount", toAccount);
                        ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                        ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
                    }
                } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Both of accounts have not been activated.", "Failed!"));
                }
            }
        }
    }

    public void oneTimeTransfer() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/TransferManagedBean: oneTimeTransfer() ======");

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        fromAccount = handleAccountString(fromBankAccountNumWithType);

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);
        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (Double.valueOf(bankAccountFrom.getTransferBalance()) < transferAmt) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dear Customer, your transfer amount has been exceeded your daily transfer limit. "
                    + "You remaining daily limit is S$" + bankAccountFrom.getTransferBalance(), "Failed!"));
        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dear Customer, you are not allowed transferring fund to a fixed deposit account. ", "Failed!"));
        } else {

            if (fromAccount.equals(toAccount)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
            } else {
                if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {

                    activationCheck = transactionSessionLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt.toString());

                    if (activationCheck.equals("Initial deposit amount is insufficient.")) {
                        if (bankAccountTo.getBankAccountType().equals("Bonus Savings Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Dear customer, minimum initial deposit amount is S$3000", "Failed"));
                        } else if (bankAccountTo.getBankAccountType().equals("Basic Savings Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Dear customer, minimum initial deposit amount is S$1", "Failed"));
                        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Dear customer, minimum initial deposit amount is S$1000", "Failed"));
                        }
                    } else if (activationCheck.equals("Please contact us at 800 820 8820 or visit our branch.")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                    } else if (activationCheck.equals("Please declare your deposit period")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Please declare your fixed deposit period first.", "Failed"));
                    } else if (activationCheck.equals("Activated successfully.")) {

                        Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                        if (diffAmt >= 0) {

                            Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                            Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                            newTransactionId = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                            statusMessage = "Your transaction has been completed.";
                            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "one time transfer", "successful", null);

                            Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                            Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                            fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                            fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                            toBankAccountNumWithType = bankAccountTo.getBankAccountType() + "-" + bankAccountTo.getBankAccountNum();

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newTransactionId", newTransactionId);
                            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                            ec.getFlash().put("transferAmt", transferAmt);
                            ec.getFlash().put("fromAccount", fromAccount);
                            ec.getFlash().put("toAccount", toAccount);
                            ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                            ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");

                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
                        }
                    }
                } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!You account(from) has not been activated.", "Failed!"));
                } else if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Active")) {

                    Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                    if (diffAmt >= 0) {

                        Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                        Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                        newTransactionId = transactionSessionLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                        statusMessage = "Your transaction has been completed.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "one time transfer", "successful", null);

                        Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                        Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                        fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                        fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                        toBankAccountNumWithType = bankAccountTo.getBankAccountType() + "-" + bankAccountTo.getBankAccountNum();

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("newTransactionId", newTransactionId);
                        ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                        ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                        ec.getFlash().put("transferAmt", transferAmt);
                        ec.getFlash().put("fromAccount", fromAccount);
                        ec.getFlash().put("toAccount", toAccount);
                        ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                        ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
                    }
                } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Both of accounts have not been activated.", "Failed!"));
                }
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
