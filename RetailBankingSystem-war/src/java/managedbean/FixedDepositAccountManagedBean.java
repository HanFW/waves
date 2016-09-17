package managedbean;

import entity.BankAccount;
import entity.CustomerBasic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.stateless.BankAccountSessionLocal;

@Named(value = "fixedDepositAccountManagedBean")
@RequestScoped

public class FixedDepositAccountManagedBean {

    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;

    private Map<String, String> fixedDepositAccounts = new HashMap<String, String>();
    private String fixedDepositAccountWithType;
    private String fixedDepositPeriod;
    private ExternalContext ec;

    public FixedDepositAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());

            fixedDepositAccounts = new HashMap<String, String>();

            for (int i = 0; i < bankAccounts.size(); i++) {
                fixedDepositAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
                fixedDepositAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }
        }
    }

    public Map<String, String> getFixedDepositAccounts() {
        return fixedDepositAccounts;
    }

    public void setFixedDepositAccounts(Map<String, String> fixedDepositAccounts) {
        this.fixedDepositAccounts = fixedDepositAccounts;
    }

    public String getFixedDepositAccountWithType() {
        return fixedDepositAccountWithType;
    }

    public void setFixedDepositAccountWithType(String fixedDepositAccountWithType) {
        this.fixedDepositAccountWithType = fixedDepositAccountWithType;
    }

    public String getFixedDepositPeriod() {
        return fixedDepositPeriod;
    }

    public void setFixedDepositPeriod(String fixedDepositPeriod) {
        this.fixedDepositPeriod = fixedDepositPeriod;
    }

    public void confirm() {
        bankAccountSessionLocal.updateDepositPeriod(fixedDepositAccountWithType,fixedDepositPeriod);
    }
}
