package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
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

@Named(value = "employeeActivateFixedDepositDoneManagedBean")
@RequestScoped

public class EmployeeActivateFixedDepositDoneManagedBean {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private Map<String, String> fixedDepositAccounts = new HashMap<String, String>();
    private String fixedDepositAccountWithType;
    private String fixedDepositPeriod;
    private String customerIdentificationNum;

    private ExternalContext ec;

    public EmployeeActivateFixedDepositDoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());

        fixedDepositAccounts = new HashMap<String, String>();

        for (int i = 0; i < bankAccounts.size(); i++) {
            fixedDepositAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
        }
    }

    public BankAccountSessionBeanLocal getBankAccountSessionLocal() {
        return bankAccountSessionLocal;
    }

    public void setBankAccountSessionLocal(BankAccountSessionBeanLocal bankAccountSessionLocal) {
        this.bankAccountSessionLocal = bankAccountSessionLocal;
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
        System.out.println("=");
        System.out.println("====== deposit/EmployeeActivateFixedDepositDoneManagedBean: confirm() ======");

        ec = FacesContext.getCurrentInstance().getExternalContext();

        String bankAccountNum = handleAccountString(fixedDepositAccountWithType);

        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount.getBankAccountType().equals("Fixed Deposit Account")) {
            if (bankAccount.getBankAccountDepositPeriod().equals("None")) {
                bankAccountSessionLocal.updateDepositPeriod(bankAccountNum, fixedDepositPeriod);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully!You have successfully declared your fixed deposit period.", "Successfully"));
                loggingSessionBeanLocal.createNewLogging("employee", null, "declare fixed deposit period", "successful", null);
            } else {
                loggingSessionBeanLocal.createNewLogging("employee", null, "declare fixed deposit period", "failed", "Already declared before");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed!You have already declared your fixed deposit period.", "Failed"));
            }
        } else {
            loggingSessionBeanLocal.createNewLogging("employee", null, "declare fixed deposit period", "failed", "Service only for fixed deposit account");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Service only for Fixed Deposit Account.", "Failed"));
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

}
