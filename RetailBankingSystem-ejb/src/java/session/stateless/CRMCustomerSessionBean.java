/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
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

/**
 *
 * @author Nicole
 */
@Stateless
@LocalBean

public class CRMCustomerSessionBean {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Long addNewCustomerBasic(String customerName, String salutation,
            String identificationType, String identificationNum, String gender,
            String email, String mobile, String dateOfBirth, String nationality,
            String countryOfResidence, String race, String maritalStatus,
            String occupation, String company, String address, String postal,
            String onlineBankingAccountNum, String onlineBankingPassword) {
        
        CustomerBasic customerBasic = new CustomerBasic();
        
        customerBasic.setCustomerName(customerName);
        customerBasic.setCustomerSalutation(salutation);
        customerBasic.setCustomerIdentificationType(identificationType);
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
        customerBasic.setCustomerOnlineBankingAccountNum("Not issued yet");
        customerBasic.setCustomerOnlineBankingPassword("Not issued yet");
        
        entityManager.persist(customerBasic);
        entityManager.flush();
        
        return customerBasic.getCustomerBasicId();
        
    }
    
    public String deleteCustomerBasic(String customerIdentificationNum){
        Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
        query.setParameter("customerIdentificationNum",customerIdentificationNum);
        List<CustomerBasic> result = query.getResultList();
        
        if(result.isEmpty()) {
            return "Error! Account does not exist!";
        }
        else {
            CustomerBasic customerBasic = result.get(0);
            entityManager.remove(customerBasic);
            return "Successfully deleted!";
        }
    }
    
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = new CustomerBasic();
        
        try{
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum",customerIdentificationNum);
            
            if(query.getResultList().isEmpty()){
                return new CustomerBasic();
            }
            else {
                customerBasic = (CustomerBasic)query.getResultList().get(0);
            }
        }
        
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new CustomerBasic();
        }
        
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return customerBasic;
    }
}
