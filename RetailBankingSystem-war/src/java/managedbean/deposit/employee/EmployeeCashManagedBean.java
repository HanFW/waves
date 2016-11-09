package managedbean.deposit.employee;

import ejb.bi.session.DepositAccountOpenSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

@Named(value = "employeeCashManagedBean")
@RequestScoped

public class EmployeeCashManagedBean {

    @EJB
    private DepositAccountOpenSessionBeanLocal depositAccountOpenSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionLocal;

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

    private String customerIdentificationNum;
    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String depositAccountNumWithType;
    private String withdrawAccountNumWithType;

    private ExternalContext ec;

    private BankAccount bankAccount;
    
    private String customerName;

    public EmployeeCashManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
        customerName=customerBasic.getCustomerName();

        List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
        fromAccounts = new HashMap<String, String>();

        for (int i = 0; i < bankAccounts.size(); i++) {
            fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
        }
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

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public String getDepositAccountNumWithType() {
        return depositAccountNumWithType;
    }

    public void setDepositAccountNumWithType(String depositAccountNumWithType) {
        this.depositAccountNumWithType = depositAccountNumWithType;
    }

    public String getWithdrawAccountNumWithType() {
        return withdrawAccountNumWithType;
    }

    public void setWithdrawAccountNumWithType(String withdrawAccountNumWithType) {
        this.withdrawAccountNumWithType = withdrawAccountNumWithType;
    }

    public void cashDeposit() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeCashManagedBean: cashDeposit() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        depositAccountNum = handleAccountString(depositAccountNumWithType);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(depositAccountNum);
        String activationCheck;

        if (bankAccount.getBankAccountId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Bank account does not exist.", "Failed"));
        } else {

            if (bankAccount.getBankAccountStatus().equals("Active")) {
                transactionId = transactionSessionLocal.cashDeposit(depositAccountNum, depositAmt.toString());

                statusMessage = "Cash deposit Successfully!";
                loggingSessionBeanLocal.createNewLogging("employee", null, "cash deposit", "successful",customerName);

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("depositAccountNum", depositAccountNum);
                ec.getFlash().put("depositAmt", depositAmt);
                ec.getFlash().put("transactionId", transactionId);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeDepositDone.xhtml?faces-redirect=true");
            } else if (bankAccount.getBankAccountStatus().equals("Inactive")) {

                activationCheck = transactionSessionLocal.checkAccountActivation(depositAccountNum, depositAmt.toString());

                if (activationCheck.equals("Insufficient")) {
                    if (bankAccount.getBankAccountType().equals("Bonus Savings Account")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Minimum initial deposit amount is S$3000", "Failed"));
                    } else if (bankAccount.getBankAccountType().equals("Basic Savings Account")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Minimum initial deposit amount is S$1", "Failed"));
                    } else if (bankAccount.getBankAccountType().equals("Fixed Deposit Account")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Minimum initial deposit amount is S$1000", "Failed"));
                    }
                } else if (activationCheck.equals("Declare")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please declare your fixed deposit period first.", "Failed"));
                } else if (activationCheck.equals("Activated")) {

                    transactionId = transactionSessionLocal.cashDeposit(depositAccountNum, depositAmt.toString());

                    Calendar cal = Calendar.getInstance();
                    depositAccountOpenSessionBeanLocal.addNewDepositAccountOpen(cal.getTimeInMillis(), cal.getTime().toString());

                    statusMessage = "Cash deposit Successfully!";
                    loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "cash deposit", "successful", customerName);

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
        System.out.println("=");
        System.out.println("====== deposit/CashManagedBean: cashWithdraw() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        withdrawAccountNum = handleAccountString(withdrawAccountNumWithType);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(withdrawAccountNum);

        if (bankAccount.getBankAccountId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Bank account does not exist.", "Failed"));
        } else {

            if (bankAccount.getBankAccountStatus().equals("Inactive")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Customer account has not been activated.", "Failed"));
            } else {

                Double diffAmt = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - withdrawAmt;
                if (diffAmt >= 0) {
                    transactionId = transactionSessionLocal.cashWithdraw(withdrawAccountNum, withdrawAmt.toString());
                    statusMessage = "Cash withdraw Successfully!";
                    loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "cash withdraw", "successful", customerName);

                    ec.getFlash().put("statusMessage", statusMessage);
                    ec.getFlash().put("withdrawAccountNum", withdrawAccountNum);
                    ec.getFlash().put("withdrawAmt", withdrawAmt);
                    ec.getFlash().put("transactionId", transactionId);

                    ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeWithdrawDone.xhtml?faces-redirect=true");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Customer account balance is insufficient.", ""));
                }
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
    
        private Long getEmployeeViaSessionMap(){
        Long employeeId;
        FacesContext context = FacesContext.getCurrentInstance();
        Employee employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        employeeId=employee.getEmployeeId();
        
        return employeeId;
    }
}
