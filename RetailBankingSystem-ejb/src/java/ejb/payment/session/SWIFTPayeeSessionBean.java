package ejb.payment.session;

import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.SWIFTPayee;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class SWIFTPayeeSessionBean implements SWIFTPayeeSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewSWIFTPayee(String swiftInstitution, String swiftPayeeAccountNum, String swiftPayeeAccountType,
            String swiftPayeeCode, String lastTransactionDate, String swiftPayeeCountry, 
            String payeeBank, Long customerBasicId) {

        SWIFTPayee swiftPayee = new SWIFTPayee();

        swiftPayee.setLastTransactionDate(lastTransactionDate);
        swiftPayee.setSwiftPayeeAccountNum(swiftPayeeAccountNum);
        swiftPayee.setSwiftPayeeAccountType(swiftPayeeAccountType);
        swiftPayee.setSwiftPayeeCode(swiftPayeeCode);
        swiftPayee.setSwiftInstitution(swiftInstitution);
        swiftPayee.setSwiftPayeeCountry(swiftPayeeCountry);
        swiftPayee.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(swiftPayee);
        entityManager.flush();

        return swiftPayee.getSwiftPayeeId();
    }
    
    @Override
    public SWIFTPayee retrieveFastPayeeById(Long fastPayeeId) {
        SWIFTPayee swiftPayee = new SWIFTPayee();

        try {
            Query query = entityManager.createQuery("Select f From FastPayee f Where f.fastPayeeId=:fastPayeeId");
            query.setParameter("fastPayeeId", fastPayeeId);

            if (query.getResultList().isEmpty()) {
                return new SWIFTPayee();
            } else {
                swiftPayee = (SWIFTPayee) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SWIFTPayee();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return swiftPayee;
    }
}
