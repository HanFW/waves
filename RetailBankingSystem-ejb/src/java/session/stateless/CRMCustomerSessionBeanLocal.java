package session.stateless;

import entity.CustomerBasic;
import java.util.List;
import javax.ejb.Local;

@Local

public interface CRMCustomerSessionBeanLocal {

    public List<CustomerBasic> getMyCustomerBasicProfile(String onlineBankingAccountNum);

    public List<CustomerBasic> getAllCustomerBasicProfile();

    public String updateCustomerOnlineBankingAccountPIN(String customerOnlineBankingAccountNum, String inputPassowrd, String newPassword);

    public String updateCustomerBasicProfile(String customerOnlineBankingAccountNum, String customerNationality,
            String customerCountryOfResidence, String customerMaritalStatus, String customerOccupation,
            String customerCompany, String customerEmail, String customerMobile, String customerAddress,
            String customerPostal);

    public Long addNewCustomerBasic(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth, 
            String customerNationality,String customerCountryOfResidence, String customerRace, 
            String customerMaritalStatus,String customerOccupation, String customerCompany, 
            String customerAddress, String customerPostal,String customerOnlineBankingAccountNum, 
            String customerOnlineBankingPassword,String customerPayeeNum,byte[] customerSignature);

    public String deleteCustomerBasic(String customerIdentificationNum);

    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);
}
