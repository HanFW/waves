package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payment.entity.Currency;
import ejb.payment.entity.SWIFTPayee;
import ejb.payment.session.CurrencySessionBeanLocal;
import ejb.payment.session.MerlionBankSessionBeanLocal;
import ejb.payment.session.SWIFTPayeeSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named(value = "sWIFTTransferManagedBean")
@ViewScoped

public class SWIFTTransferManagedBean implements Serializable {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private MerlionBankSessionBeanLocal merlionBankSessionBeanLocal;

    @EJB
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    @EJB
    private SWIFTPayeeSessionBeanLocal sWIFTPayeeSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String toBankAccountNumWithType;
    private String toCurrencyWithDollar;
    private String toCurrency;
    private Map<String, String> customerSWIFTPayee = new HashMap<String, String>();
    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String fromBankAccountNumWithType;
    private String fromCurrency;
    private Double transferAmt;
    private Double receivedCountryTransferAmt;
    private String fromBankAccount;
    private String toBankAccount;
    private String fromAccountAvailableBalance;
    private String fromAccountTotalBalance;
    private String recipientSWIFTCode;
    private String receivedCountryTransferAmtSGD;
    private String statusMessage;
    private Double serviceCharge;

    private ExternalContext ec;

    public SWIFTTransferManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            fromAccounts = new HashMap<String, String>();
            customerSWIFTPayee = new HashMap<String, String>();
            List<SWIFTPayee> swiftPayees = sWIFTPayeeSessionBeanLocal.retrieveSWIFTPayeeByCusIC(customerBasic.getCustomerIdentificationNum());

            for (int i = 0; i < bankAccounts.size(); i++) {
                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }

            for (int j = 0; j < swiftPayees.size(); j++) {
                customerSWIFTPayee.put(swiftPayees.get(j).getPayeeAccountType() + "-" + swiftPayees.get(j).getPayeeAccountNum() + "-"
                        + swiftPayees.get(j).getSwiftInstitution(), swiftPayees.get(j).getPayeeAccountType() + "-"
                        + swiftPayees.get(j).getPayeeAccountNum() + "-" + swiftPayees.get(j).getSwiftInstitution());
            }
        }
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Double getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(Double transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getFromBankAccount() {
        return fromBankAccount;
    }

    public void setFromBankAccount(String fromBankAccount) {
        this.fromBankAccount = fromBankAccount;
    }

    public String getToBankAccount() {
        return toBankAccount;
    }

    public void setToBankAccount(String toBankAccount) {
        this.toBankAccount = toBankAccount;
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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getCustomerSWIFTPayee() {
        return customerSWIFTPayee;
    }

    public void setCustomerSWIFTPayee(Map<String, String> customerSWIFTPayee) {
        this.customerSWIFTPayee = customerSWIFTPayee;
    }

    public String getToCurrencyWithDollar() {
        return toCurrencyWithDollar;
    }

    public void setToCurrencyWithDollar(String toCurrencyWithDollar) {
        this.toCurrencyWithDollar = toCurrencyWithDollar;
    }

    public Double getReceivedCountryTransferAmt() {
        return receivedCountryTransferAmt;
    }

    public void setReceivedCountryTransferAmt(Double receivedCountryTransferAmt) {
        this.receivedCountryTransferAmt = receivedCountryTransferAmt;
    }

    public String getRecipientSWIFTCode() {
        return recipientSWIFTCode;
    }

    public void setRecipientSWIFTCode(String recipientSWIFTCode) {
        this.recipientSWIFTCode = recipientSWIFTCode;
    }

    public String getReceivedCountryTransferAmtSGD() {
        return receivedCountryTransferAmtSGD;
    }

    public void setReceivedCountryTransferAmtSGD(String receivedCountryTransferAmtSGD) {
        this.receivedCountryTransferAmtSGD = receivedCountryTransferAmtSGD;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public void transferForeignAmountToSGD() {

        DecimalFormat df = new DecimalFormat("#.00");

        if (toCurrencyWithDollar != null && receivedCountryTransferAmt != null) {
            toCurrency = handleCurrencyString(toCurrencyWithDollar);
            Currency foreignCurrency = currencySessionBeanLocal.retrieveCurrencyByType(toCurrency);

            Double buyingCurrencyRate = Double.valueOf(foreignCurrency.getBuyingRate());
            Double unit = Double.valueOf(foreignCurrency.getUnit());
            receivedCountryTransferAmtSGD = df.format(receivedCountryTransferAmt / (buyingCurrencyRate * unit));
        }
    }

    public void swiftTransfer() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        DecimalFormat df = new DecimalFormat("#.00");
        serviceCharge = 10.0;

        toCurrency = handleCurrencyString(toCurrencyWithDollar);
        Currency foreignCurrency = currencySessionBeanLocal.retrieveCurrencyByType(toCurrency);

        Double buyingCurrencyRate = Double.valueOf(foreignCurrency.getBuyingRate());
        Double unit = Double.valueOf(foreignCurrency.getUnit());
        transferAmt = receivedCountryTransferAmt / (buyingCurrencyRate * unit);
        transferAmt = transferAmt + serviceCharge;

        SWIFTPayee swiftPayee = handleStringReturnSWIFTPayee(toBankAccountNumWithType);

        Calendar cal = Calendar.getInstance();
        String myAccountNum = handleAccountString(fromBankAccountNumWithType);
        String transactionDate = cal.getTime().toString();
        String swiftCodeA = "LIONSGSGMER";
        String swiftCodeB = swiftPayee.getSwiftPayeeCode();
        String organizationA = "Merlion Bank";
        String organizationB = swiftPayee.getSwiftInstitution();
        String countryA = "Singapore";
        String countryB = swiftPayee.getSwiftPayeeCountry();
        String swiftMessage = myAccountNum + " from Merlion Bank transfer S$" + df.format(transferAmt) + " to " + organizationB;

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(myAccountNum);
        String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
        Double totalBalance = Double.valueOf(currentAvailableBalance) - transferAmt;

        bankAccountSessionBeanLocal.updateBankAccountAvailableBalance(myAccountNum, df.format(totalBalance));

        merlionBankSessionBeanLocal.sendSWIFTMessage(swiftMessage, transactionDate,
                swiftCodeA, swiftCodeB, organizationA, organizationB, countryA, countryB,
                df.format(transferAmt), myAccountNum);

        sWIFTPayeeSessionBeanLocal.updateLastTransactionDate(swiftPayee.getPayeeAccountNum());

        String transactionCode = "SWIFT";
        String transactionRef = toBankAccountNumWithType;

        Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                transactionCode, transactionRef, df.format(transferAmt), " ",
                cal.getTimeInMillis(), bankAccount.getBankAccountId());

        statusMessage = "We have received your application. We will process your application within 3 working days.";

        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
        ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
        ec.getFlash().put("transferAmt", df.format(transferAmt));

        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerSWIFTTransferDone.xhtml?faces-redirect=true");
    }

    private String handleCurrencyString(String toCurrencyWithDollar) {

        String[] toCurrencyWithDollars = toCurrencyWithDollar.split(" ");
        String toCurrencyWithoutDollar = "";

        if (toCurrencyWithDollars.length == 1) {
            toCurrencyWithoutDollar = toCurrencyWithDollars[0];
        } else if (toCurrencyWithDollars.length == 2) {
            toCurrencyWithoutDollar = toCurrencyWithDollars[0];
        } else if (toCurrencyWithDollars.length == 3) {
            toCurrencyWithoutDollar = toCurrencyWithDollars[0] + " " + toCurrencyWithDollars[1];
        }

        return toCurrencyWithoutDollar;
    }

    private SWIFTPayee handleStringReturnSWIFTPayee(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String payeeInstitution = bankAccountNums[2];

        SWIFTPayee swiftPayee = sWIFTPayeeSessionBeanLocal.retrieveSWIFTPayeeByInstitution(payeeInstitution);

        return swiftPayee;
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

    public void printCurrency() {
        System.out.println("Print Currency = " + toCurrencyWithDollar);
    }

    public void printTransactionAmt() {
        System.out.println("Print Transaction Amount = " + receivedCountryTransferAmt);
    }
}
