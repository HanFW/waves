package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "sWIFTTransferManagedBean")
@RequestScoped

public class SWIFTTransferManagedBean {

    private String toBankAccountNumWithType;
    private String toCurrency;
    private Map<String, String> customerFastPayee = new HashMap<String, String>();
    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String fromBankAccountNumWithType;
    private String fromCurrency;
    private Double transferAmt;
    private String fromBankAccount;
    private String toBankAccount;
    private String fromAccountAvailableBalance;
    private String fromAccountTotalBalance;
    
    private String statusMessage;

    private ExternalContext ec;
    
    public SWIFTTransferManagedBean() {
    }
    
    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

//            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
//            fromAccounts = new HashMap<String, String>();
//            customerFastPayee = new HashMap<String, String>();
//            List<FastPayee> fastPayees = fastPayeeSessionBeanLocal.retrieveFastPayeeByCusId(customerBasic.getCustomerBasicId());

//            for (int i = 0; i < bankAccounts.size(); i++) {
//                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
//            }
//
//            for (int j = 0; j < fastPayees.size(); j++) {
//                customerFastPayee.put(fastPayees.get(j).getFastPayeeAccountType() + "-" + fastPayees.get(j).getFastPayeeAccountNum() + "-" + fastPayees.get(j).getFastPayeeName(), fastPayees.get(j).getFastPayeeAccountType() + "-" + fastPayees.get(j).getFastPayeeAccountNum() + "-" + fastPayees.get(j).getFastPayeeName());
//            }
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

    public Map<String, String> getCustomerFastPayee() {
        return customerFastPayee;
    }

    public void setCustomerFastPayee(Map<String, String> customerFastPayee) {
        this.customerFastPayee = customerFastPayee;
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
    
}
