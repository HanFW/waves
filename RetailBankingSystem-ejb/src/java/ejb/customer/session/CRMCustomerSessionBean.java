package ejb.customer.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import ejb.deposit.session.BankAccountSessionBean;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.Period;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.aerogear.security.otp.api.Base32;

@Stateless
@LocalBean

public class CRMCustomerSessionBean implements CRMCustomerSessionBeanLocal {

    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    @EJB
    private PayeeSessionBeanLocal payeeSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewCustomerBasic(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth,
            String customerNationality, String customerCountryOfResidence, String customerRace,
            String customerMaritalStatus, String customerOccupation, String customerCompany,
            String customerAddress, String customerPostal, String customerOnlineBankingAccountNum,
            String customerOnlineBankingPassword, byte[] customerSignature) {

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
        customerBasic.setCustomerAge(getAge(customerDateOfBirth));

        entityManager.persist(customerBasic);
        entityManager.flush();

        return customerBasic.getCustomerBasicId();

    }

    @Override
    public String deleteCustomerBasic(String customerIdentificationNum) {

        CustomerBasic customerBasic = retrieveCustomerBasicByIC(customerIdentificationNum);

        if (!customerBasic.getBankAccount().isEmpty()) {
            for (int i = customerBasic.getBankAccount().size() - 1; i > 0; i--) {
                bankAccountSessionLocal.deleteAccount(customerBasic.getBankAccount().get(i).getBankAccountNum());
            }
        }

        if (customerBasic.getCustomerAdvanced() != null) {
            deleteCustomerAdvanced(customerBasic.getCustomerAdvanced().getCustomerAdvancedId());
        }

        if (!customerBasic.getMessageBox().isEmpty()) {
            for (int i = customerBasic.getMessageBox().size() - 1; i > 0; i--) {
                messageSessionBeanLocal.deleteMessage(customerBasic.getMessageBox().get(i).getMessageId());
            }
        }

        if (!customerBasic.getPayee()
                .isEmpty()) {
            List<Payee> payees = customerBasic.getPayee();
            String payeeAccountNum = "";

            for (int i = customerBasic.getPayee().size() - 1; i > 0; i--) {
                payeeAccountNum = payees.get(i).getPayeeAccountNum();
                payeeSessionBeanLocal.deletePayee(payeeAccountNum);
            }
        }

        if (!customerBasic.getEnquiryCase().isEmpty()) {
            for (int j = customerBasic.getEnquiryCase().size() - 1; j > 0; j--) {
                enquirySessionBeanLocal.deleteCase(customerBasic.getEnquiryCase().get(j).getCaseId());
            }
        }

        entityManager.remove(customerBasic);

        entityManager.flush();

        return "Delete Customer Successfully";
    }

    public CustomerBasic getCustomer(String onlineBankingAccountNum) {
        CustomerBasic customer = entityManager.find(CustomerBasic.class, onlineBankingAccountNum);
        return customer;
    }

    private String getAge(String customerDateOfBirth) {
        String daystr = customerDateOfBirth.substring(0, 2);
        String monthstr = customerDateOfBirth.substring(3, 6);
        String yearstr = customerDateOfBirth.substring(7);
        int month = 0;
        int day = Integer.parseInt(daystr);
        int year = Integer.parseInt(yearstr);
        String customerAge = "";

        switch (monthstr) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }

        LocalDate localBirth = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        Period p = Period.between(localBirth, now);
        customerAge = String.valueOf(p.getYears());

        return customerAge;
    }

    @Override
    public List<CustomerBasic> getMyCustomerBasicProfile(String onlineBankingAccountNum) {
        CustomerBasic customer = getCustomer(onlineBankingAccountNum);
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerBasicId = :inCustomer");
        query.setParameter("inCustomer", customer);
        return query.getResultList();
    }

    @Override
    public CustomerAdvanced getCustomerAdvancedByAccNum(String onlineBankingAccountNum) {
        Query query = entityManager.createQuery("SELECT ca FROM CustomerAdvanced ca WHERE ca.customerOnlineBankingAccountNum = :onlineBankingAccountNum");
        query.setParameter("onlineBankingAccountNum", onlineBankingAccountNum);
        List resultList = query.getResultList();
        System.out.println("%%%%%" + resultList.isEmpty());
        if (resultList.isEmpty()) {
            return null;
        } else {
            CustomerAdvanced ca = (CustomerAdvanced) resultList.get(0);
            return ca;
        }
    }

    @Override
    public CustomerAdvanced getCustomerAdvancedById(Long id) {

        CustomerAdvanced ca = entityManager.find(CustomerAdvanced.class, id);

        System.out.println("%%%%%" + ca);
        return ca;
    }

    @Override
    public List<CustomerBasic> getAllCustomerBasicProfile() {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb");
        return query.getResultList();
    }

    @Override
    public String updateCustomerOnlineBankingAccountPIN(String customerOnlineBankingAccountNum, String hashedCurrentPassword, String hashedNewPassword) {

        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerOnlineBankingAccountNum = :customerOnlineBankingAccountNum AND cb.customerOnlineBankingPassword = :hashedCurrentPassword");
        query.setParameter("customerOnlineBankingAccountNum", customerOnlineBankingAccountNum);
        query.setParameter("hashedCurrentPassword", hashedCurrentPassword);
        List resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return "Incorrect Current Password";
        } else {
            CustomerBasic cb = (CustomerBasic) resultList.get(0);

            if (!cb.getCustomerOnlineBankingPassword().equalsIgnoreCase(hashedNewPassword)) {
                cb.setCustomerOnlineBankingPassword(hashedNewPassword);
                entityManager.flush();
                return "Update Successful";
            }
            return "Same Password";
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
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

            return "Update Successful";
        }
    }

    @Override
    public String updateCustomerAdvancedProfile(Long customerAdvancedId, String customerEmploymentDetails, String customerFamilyInfo, String customerCreditReport, String customerFinancialRiskRating, String customerFinanacialAssets, String customerFinanacialGoals) {
        Query query = entityManager.createQuery("SELECT ca FROM CustomerAdvanced ca WHERE ca.customerAdvancedId = :customerAdvancedId");
        query.setParameter("customerAdvancedId", customerAdvancedId);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Cannot find customer profile, please contact with our IT staff";
        } else {
            CustomerAdvanced ca = (CustomerAdvanced) resultList.get(0);
            ca.setCustomerEmploymentDetails(customerEmploymentDetails);
            ca.setCustomerCreditReport(customerCreditReport);
            ca.setCustomerFamilyInfo(customerFamilyInfo);
            ca.setCustomerFinanacialAssets(customerFinanacialAssets);
            ca.setCustomerFinancialRiskRating(customerFinancialRiskRating);
            ca.setCustomerFinanacialGoals(customerFinanacialGoals);
            entityManager.flush();

            return "Update Successful";
        }
    }

    @Override
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = new CustomerBasic();

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum", customerIdentificationNum);

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

    @Override
    public CustomerAdvanced retrieveCustomerAdvancedByAdId(Long customerAdvancedId) {
        CustomerAdvanced customerAdvanced = new CustomerAdvanced();

        try {
            Query query = entityManager.createQuery("Select ca From CustomerAdvanced ca Where ca.customerAdvancedId=:customerAdvancedId");
            query.setParameter("customerAdvancedId", customerAdvancedId);

            if (query.getResultList().isEmpty()) {
                return new CustomerAdvanced();
            } else {
                customerAdvanced = (CustomerAdvanced) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new CustomerAdvanced();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return customerAdvanced;
    }

    @Override
    public void deleteCustomerAdvanced(Long customerAdvancedId) {
        CustomerAdvanced customerAdvanced = retrieveCustomerAdvancedByAdId(customerAdvancedId);

        entityManager.remove(customerAdvanced);
        entityManager.flush();
    }
    
    @Override
    public Long addNewCustomerOneTime(String customerName, String customerSalutation,
            String customerIdentificationNum, String customerGender,
            String customerEmail, String customerMobile, String customerDateOfBirth,
            String customerNationality, String customerCountryOfResidence, String customerRace,
            String customerMaritalStatus, String customerOccupation, String customerCompany,
            String customerAddress, String customerPostal, String customerOnlineBankingAccountNum,
            String customerOnlineBankingPassword, byte[] customerSignature) {

        CustomerBasic customerBasic = new CustomerBasic();

        String hashedPwd = "";
        String secret = generateOTPSecret();

        try {
            hashedPwd = md5Hashing(customerOnlineBankingPassword+customerIdentificationNum.substring(0, 3));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BankAccountSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        customerBasic.setCustomerOnlineBankingAccountNum(customerOnlineBankingAccountNum);
        customerBasic.setCustomerOnlineBankingPassword(hashedPwd);
        customerBasic.setCustomerSignature(customerSignature);
        customerBasic.setCustomerAge(getAge(customerDateOfBirth));
        customerBasic.setCustomerOTPSecret(secret);
        customerBasic.setCustomerStatus("new");
        customerBasic.setCustomerOnlineBankingAccountLocked("no");

        entityManager.persist(customerBasic);
        entityManager.flush();

        return customerBasic.getCustomerBasicId();

    }
    
    private String generateOTPSecret() {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generateOTPSecret() ******");
        SecureRandom random = new SecureRandom();
//        String secret = new BigInteger(80, random).toString(32).toUpperCase();
        String secret = Base32.random();

        boolean isUnique = false;
        while (!isUnique) {
            Query query = entityManager.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOTPSecret = :secret");
            query.setParameter("secret", secret);
            List resultList = query.getResultList();
            if (resultList.isEmpty()) {
                isUnique = true;
            } else {
                secret = Base32.random();
            }
        }
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generateOTPSecret(): customer OTP secret generated");
        return secret;
    }
}
