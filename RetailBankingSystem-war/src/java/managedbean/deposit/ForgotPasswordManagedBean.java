package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.util.Date;
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

@Named(value = "forgotPasswordManagedBean")
@RequestScoped

public class ForgotPasswordManagedBean {
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;
    
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    
    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private ExternalContext ec;
    
    private String customerIdentificationNum;
    private String customerName;
    private Date customerDateOfBirth;
    private String bankAccountNumWithType;
    private String bankAccountNum;
    private Map<String, String> bankAccountNums = new HashMap<String, String>();
    
    public ForgotPasswordManagedBean() {
    }
    
    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            bankAccountNums = new HashMap<String, String>();

            for (int i = 0; i < bankAccounts.size(); i++) {
                bankAccountNums.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }
        }
    }

    public BankAccountSessionBeanLocal getBankAccountSessionBeanLocal() {
        return bankAccountSessionBeanLocal;
    }

    public void setBankAccountSessionBeanLocal(BankAccountSessionBeanLocal bankAccountSessionBeanLocal) {
        this.bankAccountSessionBeanLocal = bankAccountSessionBeanLocal;
    }

    public CRMCustomerSessionBeanLocal getCustomerSessionBeanLocal() {
        return customerSessionBeanLocal;
    }

    public void setCustomerSessionBeanLocal(CRMCustomerSessionBeanLocal customerSessionBeanLocal) {
        this.customerSessionBeanLocal = customerSessionBeanLocal;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(Date customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
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

    public Map<String, String> getBankAccountNums() {
        return bankAccountNums;
    }

    public void setBankAccountNums(Map<String, String> bankAccountNums) {
        this.bankAccountNums = bankAccountNums;
    }
    
    public void submit() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/ForgotPasswordManagedBean: submit() ======");
        
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        bankAccountNum = handleAccountString(bankAccountNumWithType);

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());
        
        if (customerBasic.getCustomerBasicId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Customer does not exist.", "Failed!"));
        } else {
            
            String name = customerBasic.getCustomerName();
            String customerDateOfBirthString = customerBasic.getCustomerDateOfBirth();
            String dateOfBirth = bankAccountSessionBeanLocal.changeDateFormat(customerDateOfBirth);

            if (!name.toUpperCase().equals(customerName.toUpperCase())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Customer Name is Wrong.", "Failed!"));
            } else if (!dateOfBirth.equals(customerDateOfBirthString)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Customer Date of Birth is Wrong.", "Failed!"));
            } else {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                sessionMap.put("bankAccountNum", bankAccountNum);
                loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "forgot password", "successful", null);
                
                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerForgotPasswordDone.xhtml?faces-redirect=true");
            }
        }
    }
    
    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
