package session.stateless;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.CustomerBasic;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

@Stateless
@LocalBean

public class CRMCustomerSessionBean implements CRMCustomerSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewCustomerBasic(String customerName, String salutation,
            String identificationNum, String gender,
            String email, String mobile, String dateOfBirth, String nationality,
            String countryOfResidence, String race, String maritalStatus,
            String occupation, String company, String address, String postal,
            String onlineBankingAccountNum, String onlineBankingPassword,
            byte[] customerSignature) {

        CustomerBasic customerBasic = new CustomerBasic();

        customerBasic.setCustomerName(customerName);
        customerBasic.setCustomerSalutation(salutation);
        customerBasic.setCustomerIdentificationNum(identificationNum);
        customerBasic.setCustomerGender(gender);
        customerBasic.setCustomerEmail(email);
        customerBasic.setCustomerMobile(mobile);
        customerBasic.setCustomerDateOfBirth(dateOfBirth);
        customerBasic.setCustomerNationality(nationality);
        customerBasic.setCustomerCountryOfResidence(countryOfResidence);
        customerBasic.setCustomerCompany(company);
        customerBasic.setCustomerRace(race);
        customerBasic.setCustomerMaritalStatus(maritalStatus);
        customerBasic.setCustomerOccupation(occupation);
        customerBasic.setCustomerAddress(address);
        customerBasic.setCustomerPostal(postal);
        customerBasic.setCustomerOnlineBankingAccountNum(null);
        customerBasic.setCustomerOnlineBankingPassword(null);
        customerBasic.setCustomerSignature(customerSignature);

        entityManager.persist(customerBasic);
        entityManager.flush();

        return customerBasic.getCustomerBasicId();

    }

    @Override
    public String deleteCustomerBasic(String customerIdentificationNum) {
        Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
        query.setParameter("customerIdentificationNum", customerIdentificationNum);
        List<CustomerBasic> result = query.getResultList();

        if (result.isEmpty()) {
            return "Error! Account does not exist!";
        } else {
            CustomerBasic customerBasic = result.get(0);
            entityManager.remove(customerBasic);
            return "Successfully deleted!";
        }
    }

    @Override
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = new CustomerBasic();

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum", customerIdentificationNum.toUpperCase());

            if (query.getResultList().isEmpty()) {
                return new CustomerBasic();
            } else {
                customerBasic = (CustomerBasic) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }
}
