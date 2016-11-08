package ejb.customer.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import java.util.List;
import javax.ejb.Local;

@Local

public interface CRMCustomerSessionBeanLocal {

    public CustomerBasic getCustomer(String onlineBankingAccountNum);

    public List<CustomerBasic> getMyCustomerBasicProfile(String onlineBankingAccountNum);

    public CustomerAdvanced getCustomerAdvancedByAccNum(String onlineBankingAccountNum);

    public List<CustomerBasic> getAllCustomerBasicProfile();

    public String updateCustomerOnlineBankingAccountPIN(String customerOnlineBankingAccountNum, String inputPassowrd, String newPassword);

    public String updateCustomerBasicProfile(String customerOnlineBankingAccountNum, String customerNationality,
            String customerCountryOfResidence, String customerMaritalStatus, String customerOccupation,
            String customerCompany, String customerEmail, String customerMobile, String customerAddress,
            String customerPostal);

    public String updateCustomerAdvancedProfile(Long customerAdvancedId, String education, String incomeMonthly, String jobDuration, String jobStatus, String jobIndustry, String jobType,
            String numOfDependent, String residentialStatus, String yearInResidence);

    public Long addNewCustomerBasic(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth,
            String customerNationality, String customerCountryOfResidence, String customerRace,
            String customerMaritalStatus, String customerOccupation, String customerCompany,
            String customerAddress, String customerPostal, String customerOnlineBankingAccountNum,
            String customerOnlineBankingPassword, byte[] customerSignature, String newCustomer);

    public String deleteCustomerBasic(String customerIdentificationNum);

    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);

    public CustomerAdvanced retrieveCustomerAdvancedByAdId(Long customerAdvancedId);

    public void deleteCustomerAdvanced(Long customerAdvancedId);

    public CustomerAdvanced getCustomerAdvancedById(Long id);

    public Long addNewCustomerOneTime(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth,
            String customerNationality, String customerCountryOfResidence, String customerRace,
            String customerMaritalStatus, String customerOccupation, String customerCompany,
            String customerAddress, String customerPostal, String customerOnlineBankingAccountNum,
            String customerOnlineBankingPassword, byte[] customerSignature, String newCustomer);

    public List<CustomerBasic> getAllNewCustomer();

    public void updateCustomerMobile(Long customerId, String customerMobile);
    
    public CustomerBasic getCustomerBasicById(Long customerId);
    
    public Long addNewCustomerAdvanced(int customerNumOfDependents, String customerEducation, String customerResidentialStatus, 
            int customerLengthOfResidence, String customerIndustryType, int customerLengthOfCurrentJob, String customerEmploymentStatus,
            double customerMonthlyFixedIncome, String customerResidentialType, String customerCompanyAddress, 
            String customerCompanyPostal, String customerCurrentPosition, String customerCurrentJobTitle, 
            String customerPreviousCompany, int customerLengthOfPreviousJob, double customerOtherMonthlyIncome, 
            String customerOtherMonthlyIncomeSource);
    
    public boolean checkExistingCustomerByIdentification(String identificationNum);
    public Long updateCustomerBasic(String customerIdentificationNum, String customerEmail, String customerMobile, 
            String customerNationality, String customerCountryOfResidence, 
            String customerMaritalStatus, String customerOccupation, String customerCompany,
            String customerAddress, String customerPostal);
    public boolean checkExistingCustomerAdvanced(String customerIdentification);
    public Long updateCustomerAdvanced(String identification, int customerNumOfDependents, String customerEducation, String customerResidentialStatus,
            int customerLengthOfResidence, String customerIndustryType, int customerLengthOfCurrentJob, String customerEmploymentStatus,
            double customerMonthlyFixedIncome, String customerResidentialType, String customerCompanyAddress,
            String customerCompanyPostal, String customerCurrentPosition, String customerCurrentJobTitle,
            String customerPreviousCompany, int customerLengthOfPreviousJob, double customerOtherMonthlyIncome,
            String customerOtherMonthlyIncomeSource);
    public boolean hasOnlineBankingAcc(Long customerBasicId);
    public void RMUpdateCustomerAdvancedInfo(Long id, String jobStatus, String occupation,String jobIndustry,double fixedMonthlyIncome,
            double otherMonthlyIncome, int numOfDependents);
}
