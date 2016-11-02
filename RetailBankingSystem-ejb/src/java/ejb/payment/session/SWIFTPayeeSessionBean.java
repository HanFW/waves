package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.SWIFTPayee;
import java.util.ArrayList;
import java.util.List;
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
            String payeeBank, String payeeType, Long customerBasicId) {

        SWIFTPayee swiftPayee = new SWIFTPayee();

        swiftPayee.setLastTransactionDate(lastTransactionDate);
        swiftPayee.setPayeeAccountNum(swiftPayeeAccountNum);
        swiftPayee.setPayeeAccountType(swiftPayeeAccountType);
        swiftPayee.setPayeeType(payeeType);
        swiftPayee.setSwiftInstitution(swiftInstitution);
        swiftPayee.setSwiftPayeeCode(swiftPayeeCode);
        swiftPayee.setSwiftPayeeCountry(swiftPayeeCountry);
        swiftPayee.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(swiftPayee);
        entityManager.flush();

        return swiftPayee.getPayeeId();
    }

    @Override
    public SWIFTPayee retrieveSWIFTPayeeById(Long payeeId) {

        SWIFTPayee swiftPayee = new SWIFTPayee();

        try {
            Query query = entityManager.createQuery("Select s From SWIFTPayee s Where s.payeeId=:payeeId");
            query.setParameter("payeeId", payeeId);

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

    @Override
    public List<SWIFTPayee> retrieveSWIFTPayeeByCusIC(String customerIdentificationNum) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<SWIFTPayee>();
        }
        try {
            Query query = entityManager.createQuery("Select s From SWIFTPayee s Where s.customerBasic=:customerBasic And s.payeeType=:payeeType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("payeeType", "SWIFT");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<SWIFTPayee>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<SWIFTPayee>();
        }
    }

    @Override
    public SWIFTPayee retrieveSWIFTPayeeByInstitution(String swiftInstitution) {

        SWIFTPayee swiftPayee = new SWIFTPayee();

        try {
            Query query = entityManager.createQuery("Select s From SWIFTPayee s Where s.swiftInstitution=:swiftInstitution");
            query.setParameter("swiftInstitution", swiftInstitution);

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
