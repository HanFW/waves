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
import java.util.ArrayList;
import java.util.Vector;
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

    private CustomerBasic getCustomer(String onlineBankingAccountNum) {
        CustomerBasic customer = entityManager.find(CustomerBasic.class, onlineBankingAccountNum);
        return customer;
    }

    public List<CustomerBasic> getMyCustomerBasicProfile(String onlineBankingAccountNum) {
        CustomerBasic customer = getCustomer(onlineBankingAccountNum);
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerBasicId = :inCustomer");
        query.setParameter("inCustomer", customer);
        return query.getResultList();
    }

    public List<CustomerBasic> getAllCustomerBasicProfile() {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb");   
        return query.getResultList();
    }

    public String updateCustomerOnlineBankingAccountPIN(String onlineBankingAccountNum, String inputPassowrd, String newPassword) {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.onlineBankingAccountNum = :onlineBankingAccountNum AND cb.onlineBankingPassword = :inputPassword");
        query.setParameter("onlineBankingAccountNum", onlineBankingAccountNum);
        query.setParameter("inputPassword", inputPassowrd);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Incorrect Current Password";
        } else {
            CustomerBasic cb = (CustomerBasic) resultList.get(0);
            cb.setOnlineBankingPassword(newPassword);
            entityManager.persist(cb);
            entityManager.flush();
            return "Successfully Updated";
        }
    }

    public String updateCustomerBasicProfile(String onlineBankingAccountNum, String nationality, String countryOfResidence, String maritalStatus, String occupation, String company, String email, String mobile, String address, String postal) {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.onlineBankingAccountNum = :onlineBankingAccountNum");
        query.setParameter("onlineBankingAccountNum", onlineBankingAccountNum);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Cannot find your profile, please contact with our customer service";
        } else {
            CustomerBasic cb = (CustomerBasic) resultList.get(0);
            cb.setNationality(nationality);
            cb.setCountryOfResidence(countryOfResidence);
            cb.setMaritalStatus(maritalStatus);
            cb.setCompany(company);
            cb.setOccupation(occupation);
            cb.setMobile(mobile);
            cb.setEmail(email);
            cb.setAddress(address);
            cb.setPostal(postal);
            entityManager.persist(cb);
            entityManager.flush();

            return "Successfully Updated";
        }
    }

    public Long addNewCustomerBasic(String customerName, String salutation, String identificationType, String identificationNum, String gender, String email, String mobile, String dateOfBirth, String nationality, String countryOfResidence, String Race, String maritalStatus, String occupation, String company, String address, String postal, String onlineBankingAccountNum, String onlineBankingPassword) {

        CustomerBasic customerBasic = new CustomerBasic();
        customerBasic.setCustomerName(customerName);
        customerBasic.setSalutation(salutation);
        customerBasic.setIdentificationType(identificationType);
        customerBasic.setIdentificationNum(identificationNum);
        customerBasic.setGender(gender);
        customerBasic.setEmail(email);
        customerBasic.setMobile(mobile);
        customerBasic.setDateOfBirth(dateOfBirth);
        customerBasic.setNationality(nationality);
        customerBasic.setCountryOfResidence(countryOfResidence);
        customerBasic.setCompany(company);
        customerBasic.setRace(Race);
        customerBasic.setMaritalStatus(maritalStatus);
        customerBasic.setOccupation(occupation);
        customerBasic.setAddress(address);
        customerBasic.setPostal(postal);
        customerBasic.setOnlineBankingAccountNum("Not issued yet");
        customerBasic.setOnlineBankingPassword("Not issued yet");

        entityManager.persist(customerBasic);
        entityManager.flush();
        return customerBasic.getCustomerBasicId();

    }

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

    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = new CustomerBasic();
        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum", customerIdentificationNum);

            customerBasic = (CustomerBasic) query.getResultList().get(0);
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }

}
