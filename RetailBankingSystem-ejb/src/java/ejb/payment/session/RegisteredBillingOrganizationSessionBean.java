package ejb.payment.session;

import ejb.payment.entity.RegisteredBillingOrganization;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RegisteredBillingOrganizationSessionBean implements RegisteredBillingOrganizationSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public Long addNewRegisteredBillingOrganization(String billingOrganizationName, String bankAccountNum,
            String bankAccountType, String bankName) {

        RegisteredBillingOrganization billOrg = new RegisteredBillingOrganization();

        billOrg.setBankAccountNum(bankAccountNum);
        billOrg.setBankAccountType(bankAccountType);
        billOrg.setBankName(bankName);
        billOrg.setBillingOrganizationName(billingOrganizationName);

        entityManager.persist(billOrg);
        entityManager.flush();

        return billOrg.getBillingOrganizationId();
    }

    @Override
    public List<RegisteredBillingOrganization> getAllRegisteredBillingOrganization() {
        Query query = entityManager.createQuery("SELECT r FROM RegisteredBillingOrganization r");
        return query.getResultList();
    }
    
    @Override
    public RegisteredBillingOrganization retrieveRegisteredBillingOrganizationByName(String billingOrganizationName) {
        RegisteredBillingOrganization billingOrganization = new RegisteredBillingOrganization();

        try {
            Query query = entityManager.createQuery("Select r From RegisteredBillingOrganization r Where r.billingOrganizationName=:billingOrganizationName");
            query.setParameter("billingOrganizationName", billingOrganizationName);

            if (query.getResultList().isEmpty()) {
                return new RegisteredBillingOrganization();
            } else {
                billingOrganization = (RegisteredBillingOrganization) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new RegisteredBillingOrganization();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return billingOrganization;
    }

}
