package ejb.customer.entity;

import ejb.card.entity.CreditCard;
import ejb.deposit.entity.BankAccount;
import ejb.infrastructure.entity.MessageBox;
import ejb.deposit.entity.Payee;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.CreditReportBureauScore;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import ejb.payment.entity.Cheque;
import ejb.payment.entity.OtherBankPayee;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.SWIFTPayee;
import ejb.wealth.entity.Portfolio;
import ejb.wealth.entity.RiskProfile;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class CustomerBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerBasicId;
    private String customerSalutation;
    private String customerName;
    private String customerGender;
    private String customerEmail;
    private String customerMobile;
    private String customerDateOfBirth;
    private String customerNationality;
    private String customerCountryOfResidence;
    private String customerRace;
    private String customerMaritalStatus;
    private String customerOccupation;
    private String customerCompany;
    private String customerAddress;
    private String customerPostal;
    private String customerOnlineBankingAccountNum;
    private String customerOnlineBankingPassword;
    private String customerIdentificationNum;
    private String customerStatus;
    private String customerPayeeNum;
    private String customerAge;
    private String customerOTPSecret;
    private String customerOnlineBankingAccountLocked;
    private String newCustomer;

    private byte[] customerSignature;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<BankAccount> bankAccount;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<Payee> payee;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<EnquiryCase> enquiryCase;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private CustomerAdvanced customerAdvanced;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<MessageBox> messageBox;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<CreditCard> creditCard;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<OtherBankPayee> otherBankPayee;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<GIRO> giro;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<Cheque> cheque;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<CustomerDebt> customerDebt;
    
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerProperty customerProperty;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<LoanApplication> loanApplication;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<CashlineApplication> cashlineApplication;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<Portfolio> portfolios;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private CreditReportBureauScore bureauScore;
    
    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private RiskProfile riskProfile;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private MortgageLoanApplication mortgageLoanApplication;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private RefinancingApplication refinancingApplication;
    
    public void addLoanApplication(LoanApplication newApplication){
        loanApplication.add(newApplication);
    }
    
    public void addCashlineApplication(CashlineApplication newApplication){
        cashlineApplication.add(newApplication);
    }

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "customerBasic")
    private List<SWIFTPayee> swiftPayee;

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }

    public String getCustomerPayeeNum() {
        return customerPayeeNum;
    }

    public void setCustomerPayeeNum(String customerPayeeNum) {
        this.customerPayeeNum = customerPayeeNum;
    }

    public byte[] getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(byte[] customerSignature) {
        this.customerSignature = customerSignature;
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

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
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

    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
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

    public void setCustomerCompany(String customerCompanyName) {
        this.customerCompany = customerCompanyName;
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

    public List<BankAccount> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(List<BankAccount> bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Payee> getPayee() {
        return payee;
    }

    public void setPayee(List<Payee> payee) {
        this.payee = payee;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(String customerAge) {
        this.customerAge = customerAge;
    }

    public List<EnquiryCase> getEnquiryCase() {
        return enquiryCase;
    }

    public void setEnquiryCase(List<EnquiryCase> enquiryCase) {
        this.enquiryCase = enquiryCase;
    }

    public void addNewCase(EnquiryCase ec) {
        enquiryCase.add(ec);
    }

    public String getCustomerOTPSecret() {
        return customerOTPSecret;
    }

    public void setCustomerOTPSecret(String customerOTPSecret) {
        this.customerOTPSecret = customerOTPSecret;
    }

    public List<MessageBox> getMessageBox() {
        return messageBox;
    }

    public void setMessageBox(List<MessageBox> messageBox) {
        this.messageBox = messageBox;
    }

    public CustomerAdvanced getCustomerAdvanced() {
        return customerAdvanced;
    }

    public void setCustomerAdvanced(CustomerAdvanced customerAdvanced) {
        this.customerAdvanced = customerAdvanced;
    }

    public List<CreditCard> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(List<CreditCard> creditCard) {
        this.creditCard = creditCard;
    }

    public void addNewCreditCard(CreditCard cc) {
        creditCard.add(cc);
    }
    
    public void removeCreditCard(CreditCard cc) {
        creditCard.remove(cc);
    }

    public String getCustomerOnlineBankingAccountLocked() {
        return customerOnlineBankingAccountLocked;
    }

    public void setCustomerOnlineBankingAccountLocked(String customerOnlineBankingAccountLocked) {
        this.customerOnlineBankingAccountLocked = customerOnlineBankingAccountLocked;
    }

    public List<OtherBankPayee> getOtherBankPayee() {
        return otherBankPayee;
    }

    public void setOtherBankPayee(List<OtherBankPayee> otherBankPayee) {
        this.otherBankPayee = otherBankPayee;
    }

    public List<GIRO> getGiro() {
        return giro;
    }

    public void setGiro(List<GIRO> giro) {
        this.giro = giro;
    }

    public String getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(String newCustomer) {
        this.newCustomer = newCustomer;
    }

    public List<Cheque> getCheque() {
        return cheque;
    }

    public void setCheque(List<Cheque> cheque) {
        this.cheque = cheque;
    }

    public List<SWIFTPayee> getSwiftPayee() {
        return swiftPayee;
    }

    public void setSwiftPayee(List<SWIFTPayee> swiftPayee) {
        this.swiftPayee = swiftPayee;
    }

    public List<CustomerDebt> getCustomerDebt() {
        return customerDebt;
    }

    public void setCustomerDebt(List<CustomerDebt> customerDebt) {
        this.customerDebt = customerDebt;
    }

    public CustomerProperty getCustomerProperty() {
        return customerProperty;
    }

    public void setCustomerProperty(CustomerProperty customerProperty) {
        this.customerProperty = customerProperty;
    }

    public List<LoanApplication> getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(List<LoanApplication> loanApplication) {
        this.loanApplication = loanApplication;
    }

    public CreditReportBureauScore getBureauScore() {
        return bureauScore;
    }

    public void setBureauScore(CreditReportBureauScore bureauScore) {
        this.bureauScore = bureauScore;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    public List<CashlineApplication> getCashlineApplication() {
        return cashlineApplication;
    }

    public void setCashlineApplication(List<CashlineApplication> cashlineApplication) {
        this.cashlineApplication = cashlineApplication;
    }

    public MortgageLoanApplication getMortgageLoanApplication() {
        return mortgageLoanApplication;
    }

    public void setMortgageLoanApplication(MortgageLoanApplication mortgageLoanApplication) {
        this.mortgageLoanApplication = mortgageLoanApplication;
    }

    public RefinancingApplication getRefinancingApplication() {
        return refinancingApplication;
    }

    public void setRefinancingApplication(RefinancingApplication refinancingApplication) {
        this.refinancingApplication = refinancingApplication;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPotrfolios(List<Portfolio> potfolios) {
        this.portfolios = potfolios;
    }
    
    public void addPortfolio(Portfolio portfolio){
        this.portfolios.add(portfolio);
    }
    
    public void removePortfolio(Portfolio portfolio){
        this.portfolios.remove(portfolio);
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerBasicId != null ? customerBasicId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerBasic)) {
            return false;
        }
        CustomerBasic other = (CustomerBasic) object;
        if ((this.customerBasicId == null && other.customerBasicId != null) || (this.customerBasicId != null && !this.customerBasicId.equals(other.customerBasicId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerBasic[ id=" + customerBasicId + " ]";
    }

}
