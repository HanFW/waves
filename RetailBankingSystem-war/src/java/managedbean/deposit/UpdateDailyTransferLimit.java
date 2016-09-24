package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "updateDailyTransferLimit")
@RequestScoped

public class UpdateDailyTransferLimit {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    private ExternalContext ec;

    private Map<String, String> myBankAccounts = new HashMap<String, String>();
    private String bankAccountNumWithType;
    private String bankAccountNum;
    private String dailyTransferLimit;

    public UpdateDailyTransferLimit() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());

            myBankAccounts = new HashMap<String, String>();

            for (int i = 0; i < bankAccounts.size(); i++) {
                myBankAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }
        }
    }

    public Map<String, String> getMyBankAccounts() {
        return myBankAccounts;
    }

    public void setMyBankAccounts(Map<String, String> myBankAccounts) {
        this.myBankAccounts = myBankAccounts;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(String dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public void submit() {

        bankAccountNum = handleAccountString(bankAccountNumWithType);
        bankAccountSessionLocal.updateDailyTransferLimit(bankAccountNum, dailyTransferLimit);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have updated your daily transfer limit successfully.", " Successfully"));
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
