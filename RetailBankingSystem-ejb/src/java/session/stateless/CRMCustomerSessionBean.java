package session.stateless;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.CustomerBasic;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

@Stateless
@LocalBean

public class CRMCustomerSessionBean implements CRMCustomerSessionBeanLocal{
    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewCustomerBasic(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth, 
            String customerNationality,String customerCountryOfResidence, String customerRace, 
            String customerMaritalStatus,String customerOccupation, String customerCompany, 
            String customerAddress, String customerPostal,String customerOnlineBankingAccountNum, 
            String customerOnlineBankingPassword,String customerPayeeNum,byte[] customerSignature) {
        
        CustomerBasic customerBasic = new CustomerBasic();
        
        customerBasic.setCustomerName(customerName);
        customerBasic.setCustomerSalutation(customerSalutation);
        customerBasic.setCustomerIdentificationNum(customerIdentificationNum);
        customerBasic.setCustomerGender(customerGender);
        customerBasic.setCustomerEmail(customerEmail);
        customerBasic.setCustomerMobile(customerMobile);
        customerBasic.setCustomerDateOfBirth(customerDateOfBirth);
        customerBasic.setCustomerNationality(customerNationality);
        customerBasic.setCustomerCountryOfResidence(customerCountryOfResidence);
        customerBasic.setCustomerCompany(customerCompany);
        customerBasic.setCustomerRace(customerRace);
        customerBasic.setCustomerMaritalStatus(customerMaritalStatus);
        customerBasic.setCustomerOccupation(customerOccupation);
        customerBasic.setCustomerAddress(customerAddress);
        customerBasic.setCustomerPostal(customerPostal);
        customerBasic.setCustomerOnlineBankingAccountNum(null);
        customerBasic.setCustomerOnlineBankingPassword(null);
        customerBasic.setCustomerSignature(customerSignature);
        customerBasic.setCustomerPayeeNum(customerPayeeNum);
        
        entityManager.persist(customerBasic);
        entityManager.flush();
        
        return customerBasic.getCustomerBasicId();
        
    }
    
    @Override
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

    private CustomerBasic getCustomer(String onlineBankingAccountNum) {
        CustomerBasic customer = entityManager.find(CustomerBasic.class, onlineBankingAccountNum);
        return customer;
    }

    @Override
    public List<CustomerBasic> getMyCustomerBasicProfile(String onlineBankingAccountNum) {
        CustomerBasic customer = getCustomer(onlineBankingAccountNum);
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerBasicId = :inCustomer");
        query.setParameter("inCustomer", customer);
        return query.getResultList();
    }

    @Override
    public List<CustomerBasic> getAllCustomerBasicProfile() {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb");   
        return query.getResultList();
    }

    @Override
    public String updateCustomerOnlineBankingAccountPIN(String customerOnlineBankingAccountNum, String inputPassowrd, String newPassword) {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerOnlineBankingAccountNum = :customerOnlineBankingAccountNum AND cb.customerOnlineBankingPassword = :inputPassword");
        query.setParameter("customerOnlineBankingAccountNum", customerOnlineBankingAccountNum);
        query.setParameter("inputPassword", inputPassowrd);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Incorrect Current Password";
        } else {
            CustomerBasic cb = (CustomerBasic) resultList.get(0);
            cb.setCustomerOnlineBankingPassword(newPassword);
            entityManager.flush();
            return "Successfully Updated";
        }
    }

    @Override
    public String updateCustomerBasicProfile(String customerOnlineBankingAccountNum, String customerNationality, String customerCountryOfResidence, String customerMaritalStatus, String customerOccupation, String customerCompany, String customerEmail, String customerMobile, String customerAddress, String customerPostal) {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerOnlineBankingAccountNum = :customerOnlineBankingAccountNum");
        query.setParameter("customerOnlineBankingAccountNum", customerOnlineBankingAccountNum);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Cannot find your profile, please contact with our customer service";
        } else {
            CustomerBasic cb = (CustomerBasic) resultList.get(0);
            cb.setCustomerNationality(customerNationality);
            cb.setCustomerCountryOfResidence(customerCountryOfResidence);
            cb.setCustomerMaritalStatus(customerMaritalStatus);
            cb.setCustomerCompany(customerCompany);
            cb.setCustomerOccupation(customerOccupation);
            cb.setCustomerMobile(customerMobile);
            cb.setCustomerEmail(customerEmail);
            cb.setCustomerAddress(customerAddress);
            cb.setCustomerPostal(customerPostal);
            entityManager.flush();

            return "Successfully Updated";
        }
    }
    
    @Override
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
    
    @Override
    public boolean updatePayeeNum(Long customerBasicId) {
        CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId);

        Double currentPayeeNum = Double.valueOf(customerBasic.getCustomerPayeeNum());
        if (currentPayeeNum >= 2) {
            return false;
        } 
        else {
            Double payeeNum = currentPayeeNum + 1;
            customerBasic.setCustomerPayeeNum(payeeNum.toString());
            return true;
        }
    }
}

