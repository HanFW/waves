package ejb.payment.session;

import ejb.payment.entity.BillingOrganization;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BillingOrganizationSessionBean implements BillingOrganizationSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public Long addNewBillingOrganization(String billingOrganizationName, String bankAccountNum,
            String bankAccountType, String bankName) {

        BillingOrganization billOrg = new BillingOrganization();

        billOrg.setBankAccountNum(bankAccountNum);
        billOrg.setBankAccountType(bankAccountType);
        billOrg.setBankName(bankName);
        billOrg.setBillingOrganizationName(billingOrganizationName);

        entityManager.persist(billOrg);
        entityManager.flush();

        return billOrg.getBillingOrganizationId();
    }

    @Override
    public List<BillingOrganization> getAllBillingOrganization() {
        Query query = entityManager.createQuery("SELECT b FROM BillingOrganization b");
        return query.getResultList();
    }
    
    @Override
    public BillingOrganization retrieveBillingOrganizationByName(String billingOrganizationName) {
        BillingOrganization billingOrganization = new BillingOrganization();

        try {
            Query query = entityManager.createQuery("Select b From BillingOrganization b Where b.billingOrganizationName=:billingOrganizationName");
            query.setParameter("billingOrganizationName", billingOrganizationName);

            if (query.getResultList().isEmpty()) {
                return new BillingOrganization();
            } else {
                billingOrganization = (BillingOrganization) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new BillingOrganization();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return billingOrganization;
    }

}
