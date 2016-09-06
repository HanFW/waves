package session.stateless;

import entity.CustomerBasic;
import javax.ejb.Local;

@Local
public interface CRMCustomerSessionBeanLocal {
    public Long addNewCustomerBasic(String customerName, String salutation,
            String identificationType, String identificationNum, String gender,
            String email, String mobile, String dateOfBirth, String nationality,
            String countryOfResidence, String race, String maritalStatus,
            String occupation, String company, String address, String postal,
            String onlineBankingAccountNum, String onlineBankingPassword);
    public String deleteCustomerBasic(String customerIdentificationNum);
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);
}
