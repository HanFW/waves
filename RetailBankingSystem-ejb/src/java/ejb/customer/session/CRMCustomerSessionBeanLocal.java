package ejb.customer.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import java.util.List;
import javax.ejb.Local;

@Local

public interface CRMCustomerSessionBeanLocal {

    public List<CustomerBasic> getMyCustomerBasicProfile(String onlineBankingAccountNum);

    public CustomerAdvanced getCustomerAdvancedByAccNum(String onlineBankingAccountNum);

    public List<CustomerBasic> getAllCustomerBasicProfile();

    public String updateCustomerOnlineBankingAccountPIN(String customerOnlineBankingAccountNum, String inputPassowrd, String newPassword);

    public String updateCustomerBasicProfile(String customerOnlineBankingAccountNum, String customerNationality,
            String customerCountryOfResidence, String customerMaritalStatus, String customerOccupation,
            String customerCompany, String customerEmail, String customerMobile, String customerAddress,
            String customerPostal);

    public String updateCustomerAdvancedProfile(String customerOnlineBankingAccountNum, String customerEmploymentDetails,
            String customerFamilyInfo, String customerCreditReport, String customerFinancialRiskRating,
            String customerFinanacialAssets, String customerFinanacialGoals);

    public Long addNewCustomerBasic(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth,
            String customerNationality, String customerCountryOfResidence, String customerRace,
            String customerMaritalStatus, String customerOccupation, String customerCompany,
            String customerAddress, String customerPostal, String customerOnlineBankingAccountNum,
            String customerOnlineBankingPassword, byte[] customerSignature);

    public String deleteCustomerBasic(String customerIdentificationNum);

    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);

    public CustomerAdvanced retrieveCustomerAdvancedByAdId(Long customerAdvancedId);

    public void deleteCustomerAdvanced(Long customerAdvancedId);
}
