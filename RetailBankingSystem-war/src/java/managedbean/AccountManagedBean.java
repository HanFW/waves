package managedbean;

import entity.CustomerBasic;
import javax.ejb.EJB;
import session.stateless.CRMCustomerSessionBean;
import entity.BankAccount;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;
import session.stateless.AdminSessionBeanLocal;
import session.stateless.BankAccountSessionLocal;
import session.stateless.InterestSessionLocal;

//@ManagedBean
@Named(value = "accountManagedBean")
@ViewScoped

public class AccountManagedBean implements Serializable{
    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;
    
    @EJB
    private InterestSessionLocal interestSessionLocal;
    
    @EJB
    private CRMCustomerSessionBean customerSessionBean;
    
    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;
       
    private BankAccount bankAccount;
    
    private Long bankAccountId;
    private String bankAccountNum;
    private String bankAccountPwd;
    private String confirmBankAccountPwd;
    private String bankAccountType;
    private String bankAccountBalance;
    private Long newAccountId;
    private String transferDailyLimit;
    private String statusMessage;
    
    private String existingCustomer;
    private String onlyOneAccount;
    private Long customerBasicId;
    private String customerSalutation;
    private String customerSalutationOthers;
    private String customerName;
    private String customerEmail;
    private String customerMobile;
    private String customerNationality;
    private String customerCountryOfResidence;
    private String customerDateOfBirth;
    private String customerGender;
    private String customerRace;
    private String customerMaritalStatus;
    private String customerOccupation;
    private String customerCompany;
    private String customerAddress;
    private String customerPostal;
    private String customerIdentificationNum;
    private String customerIdentificationType;
    private Long newCustomerBasicId;
    private String customerOnlineBankingAccountNum;
    private String customerOnlineBankingPassword;
    private String customerPayeeNum;
    private String customerSignature;
    
    private Long newInterestId;
    private Long interestId;
    private String dailyInterest;
    private String monthlyInterest;
    private boolean isTransfer;
    private boolean isWithdraw;
    
    private boolean visible=false;
    private ExternalContext ec;
    private CustomerBasic customerBasic;
    //private ExternalContext ec;
    //ec = FacesContext.getCurrentInstance().getExternalContext();
    
    public AccountManagedBean() {
    }
    
    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }
    
    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }
    
    public String getBankAccountNum()
    {
        return bankAccountNum;
    }
    
    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
    
    public String getBankAccountPwd() {
        return bankAccountPwd;
    }
    
    public void setBankAccountPwd(String bankAccountPwd) {
        this.bankAccountPwd = bankAccountPwd;
    }
    
    public String getBankAccountType() {
        return bankAccountType;
    }
    
    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }
    
    public Long getNewAccountId() {
        return newAccountId;
    }
    
    public void setNewAccountId(Long newAccountId) {
        this.newAccountId = newAccountId;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }
    
    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }
    
    public Long getNewCustomerBasicId() {
        return newCustomerBasicId;
    }
    
    public void setNewCustomerBasicId(Long newCustomerBasicId) {
        this.newCustomerBasicId = newCustomerBasicId;
    }
    
    public String getCustomerSalutation() {
        return customerSalutation;
    }
    
    public void setCustomerSalutation(String customerSalutation) {
        this.customerSalutation = customerSalutation;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerMobile() {
        return customerMobile;
    }
    
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }
    
    public String getCustomerNationality() {
        return customerNationality;
    }
    
    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }
    
    public String getCustomerCountryOfResidence() {
        return customerCountryOfResidence;
    }
    
    public void setCustomerCountryOfResidence(String customerCountryOfResidence) {
        this.customerCountryOfResidence = customerCountryOfResidence;
    }
    
    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }
    
    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }
    
    public String getCustomerGender() {
        return customerGender;
    }
    
    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }
    
    public String getCustomerRace() {
        return customerRace;
    }
    
    public void setCustomerRace(String customerRace) {
        this.customerRace = customerRace;
    }
    
    public String getCustomerMaritalStatus() {
        return customerMaritalStatus;
    }
    
    public void setCustomerMaritalStatus(String customerMaritalStatus) {
        this.customerMaritalStatus = customerMaritalStatus;
    }
    
    public String getCustomerOccupation() {
        return customerOccupation;
    }
    
    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }
    
    public String getCustomerCompany() {
        return customerCompany;
    }
    
    public void setCustomerCompany(String customerCompany) {
        this.customerCompany = customerCompany;
    }
    
    public String getCustomerAddress() {
        return customerAddress;
    }
    
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
    
    public String getCustomerPostal() {
        return customerPostal;
    }
    
    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }
    
    public String getCustomerIdentificationType() {
        return customerIdentificationType;
    }
    
    public void setCustomerIdentificationType(String customerIdentificationType) {
        this.customerIdentificationType = customerIdentificationType;
    }
    
    public String getCustomerOnlineBankingAccountNum() {
        return customerOnlineBankingAccountNum;
    }
    
    public void setCustomerOnlineBankingAccountNum(String customerOnlineBankingAccountNum) {
        this.customerOnlineBankingAccountNum = customerOnlineBankingAccountNum;
    }
    
    public String getCustomerOnlineBankingPassword() {
        return customerOnlineBankingPassword;
    }
    
    public void setCustomerOnlineBankingPassword(String customerOnlineBankingPassword) {
        this.customerOnlineBankingPassword = customerOnlineBankingPassword;
    }
    
    public String getBankAccountBalance() {
        return bankAccountBalance;
    }
    
    public void setBankAccountBalance(String bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }
    
    public String getConfirmBankAccountPwd() {
        return confirmBankAccountPwd;
    }
    
    public void setConfirmBankAccountPwd(String confirmBankAccountPwd) {
        this.confirmBankAccountPwd = confirmBankAccountPwd;
    }
    
    public Long getCustomerBasicId() {
        return customerBasicId;
    }
    
    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }
    
    public String getExistingCustomer() {
        return existingCustomer;
    }
    
    public void setExistingCustomer(String existingCustomer) {
        this.existingCustomer = existingCustomer;
    }
    
    public String getOnlyOneAccount() {
        return onlyOneAccount;
    }
    
    public void setOnlyOneAccount(String onlyOneAccount) {
        this.onlyOneAccount = onlyOneAccount;
    }

    public Long getNewInterestId() {
        return newInterestId;
    }

    public void setNewInterestId(Long newInterestId) {
        this.newInterestId = newInterestId;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

    public String getTransferDailyLimit() {
        return transferDailyLimit;
    }

    public void setTransferDailyLimit(String transferDailyLimit) {
        this.transferDailyLimit = transferDailyLimit;
    }

    public String getDailyInterest() {
        return dailyInterest;
    }

    public void setDailyInterest(String dailyInterest) {
        this.dailyInterest = dailyInterest;
    }

    public String getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(String monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }

    public boolean isIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(boolean isTransfer) {
        this.isTransfer = isTransfer;
    }

    public boolean isIsWithdraw() {
        return isWithdraw;
    }

    public void setIsWithdraw(boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
    }
    
    public void show() {
        
        if(customerSalutation.equals("Others")) {
            visible=true;
        }
        else {
            visible=false;
        }
    }
    
    public void hide() {
        visible=false;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public Long getBankAccountId() {
        return bankAccountId;
    }
    
    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
    
    public String getCustomerSalutationOthers() {
        return customerSalutationOthers;
    }
    
    public void setCustomerSalutationOthers(String customerSalutationOthers) {
        this.customerSalutationOthers = customerSalutationOthers;
    }
    
    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String getCustomerPayeeNum() {
        return customerPayeeNum;
    }

    public void setCustomerPayeeNum(String customerPayeeNum) {
        this.customerPayeeNum = customerPayeeNum;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }
    
    public void saveAccount() {
        
        SecureRandom random = new SecureRandom();
        if(customerIdentificationType.equals("Passport")) {
            bankAccountNum=new BigInteger(13,random).setBit(12).toString(10)+customerIdentificationNum.hashCode();
        }
        else if(customerIdentificationType.equals("NRIC")) {
            bankAccountNum=new BigInteger(13,random).setBit(12).toString(10)+"-"+customerIdentificationNum.hashCode();
        }
//        String bankAccountNum=new BigInteger (23, 90, random).toString(10);
//        UID unique = new UID();
//        bankAccountNum=accountNum+unique.hashCode();
//        UUID account = UUID.randomUUID();
//        bankAccountNum = String.valueOf(account.hashCode());
        if(existingCustomer.equals("Yes"))
        {
            dailyInterest="0";
            monthlyInterest="0";
            isTransfer=false;
            isWithdraw=false;
            
            customerBasicId = customerSessionBean.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase()).getCustomerBasicId();
            newInterestId = interestSessionLocal.addNewInterest(dailyInterest,monthlyInterest,isTransfer, isWithdraw);
            
            bankAccountBalance="0";
            transferDailyLimit = "2000";
            newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum,bankAccountPwd,bankAccountType,bankAccountBalance,transferDailyLimit,customerBasicId,interestId);
            
            bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIdentificationNum).add(bankAccount);
            
            interestSessionLocal.calculateInterest(bankAccountNum);
//            customerBasic=customerSessionBean.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());
//            customerSessionBean.updateCustomerBasic(customerName, customerSalutation, customerIdentificationType,
//                    customerIdentificationNum, customerGender, customerEmail, customerMobile, customerDateOfBirth,
//                    customerNationality, customerCountryOfResidence, customerRace, customerMaritalStatus,
//                    customerOccupation, customerName, statusMessage, customerName,customerBasic);
            adminSessionBeanLocal.createOnlineBankingAccount(customerBasicId);
        }
        else if(existingCustomer.equals("No"))
        {
            customerPayeeNum="0";
            newCustomerBasicId = customerSessionBean.addNewCustomerBasic(customerName,
                    customerSalutation,customerIdentificationNum.toUpperCase(),
                    customerGender, customerEmail, customerMobile, customerDateOfBirth,
                    customerNationality, customerCountryOfResidence,customerRace,
                    customerMaritalStatus, customerOccupation, customerCompany,
                    customerAddress, customerPostal, customerOnlineBankingAccountNum,
                    customerOnlineBankingPassword,customerPayeeNum,customerSignature.getBytes());
            
            dailyInterest="0";
            monthlyInterest="0";
            isTransfer=false;
            isWithdraw=false;
            newInterestId = interestSessionLocal.addNewInterest(dailyInterest,monthlyInterest,isTransfer, isWithdraw);
            
            bankAccountBalance="0";
            transferDailyLimit = "2000";
            newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum,bankAccountPwd,bankAccountType,bankAccountBalance,transferDailyLimit,newCustomerBasicId,newInterestId);
            adminSessionBeanLocal.createOnlineBankingAccount(newAccountId);
        }
        
        statusMessage = "New Account Saved Successfully.";
    }
    
    public void deleteAccount() throws IOException
    {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        bankAccountType = bankAccount.getBankAccountType();
        
        if(onlyOneAccount.equals("Yes")) {
            if(!bankAccount.getBankAccountBalance().equals("0")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please withdraw all your money.","Failed!"));
            }
            else {
                bankAccountSessionLocal.deleteAccount(bankAccountNum);
                statusMessage="Account has been successfully deleted.";
                
                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("bankAccountNum", bankAccountNum);
                ec.getFlash().put("bankAccountType", bankAccountType);
                
                ec.redirect("deleteAccount.xhtml?faces-redirect=true");
            }
            
        }
        else if (onlyOneAccount.equals("No")) {
            if(!bankAccount.getBankAccountBalance().equals("0")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please withdraw all your money.","Failed!"));
            }
            else{
                bankAccountSessionLocal.deleteAccount(bankAccountNum);
                customerSessionBean.deleteCustomerBasic(customerIdentificationNum);
                statusMessage="Account has been successfully deleted.";
                
                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("bankAccountNum", bankAccountNum);
                ec.getFlash().put("bankAccountType", bankAccountType);
                
                ec.redirect("deleteAccount.xhtml?faces-redirect=true");
            }
        }
    }
    
}
