package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.RegularGIRO;
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
public class RegularGIROSessionBean implements RegularGIROSessionBeanLocal {

    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRegularGRIO(String bankAccountNum, String bankAccountNumWithType, String giroType,
            String paymentAmt, String paymentFrequency, String payeeBankName, String payeeBankAccountNum,
            String regularGIROStatus, String payeeName, Long customerBasicId) {

        RegularGIRO regularGIRO = new RegularGIRO();

        regularGIRO.setBankAccountNum(bankAccountNum);
        regularGIRO.setBankAccountNumWithType(bankAccountNumWithType);
        regularGIRO.setGiroType(giroType);
        regularGIRO.setPaymentAmt(paymentAmt);
        regularGIRO.setPaymentFrequency(paymentFrequency);
        regularGIRO.setPayeeBankName(payeeBankName);
        regularGIRO.setPayeeAccountNum(payeeBankAccountNum);
        regularGIRO.setRegularGIROStatus(regularGIROStatus);
        regularGIRO.setPayeeName(payeeName);
        regularGIRO.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(regularGIRO);
        entityManager.flush();

        return regularGIRO.getGiroId();
    }

    @Override
    public RegularGIRO retrieveRegularGIROById(Long giroId) {
        RegularGIRO regularGIRO = new RegularGIRO();

        try {
            Query query = entityManager.createQuery("Select r From RegularGIRO r Where r.giroId=:giroId");
            query.setParameter("giroId", giroId);

            if (query.getResultList().isEmpty()) {
                return new RegularGIRO();
            } else {
                regularGIRO = (RegularGIRO) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new RegularGIRO();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return regularGIRO;
    }

    @Override
    public List<RegularGIRO> retrieveRegularGIROByNum(String payeeAccountNum) {
        try {
            Query query = entityManager.createQuery("Select r From RegularGIRO r Where r.payeeAccountNum=:payeeAccountNum And r.giroType=:giroType");
            query.setParameter("payeeAccountNum", payeeAccountNum);
            query.setParameter("giroType", "Regular GIRO");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<RegularGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<RegularGIRO>();
        }
    }

    @Override
    public List<RegularGIRO> retrieveRegularGIROByCusIC(String customerIdentificationNum) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<RegularGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select r From RegularGIRO r Where r.customerBasic=:customerBasic And r.giroType=:giroType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Regular GIRO");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<RegularGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<RegularGIRO>();
        }
    }

    @Override
    public void updatePaymentAmt(Long giroId, String paymentAmt) {
        RegularGIRO regularGIRO = retrieveRegularGIROById(giroId);
        regularGIRO.setPaymentAmt(paymentAmt);
    }

    @Override
    public String deleteRegularGIRO(Long giroId) {

        GIRO regularGIRO = gIROSessionBeanLocal.retrieveGIROById(giroId);

        entityManager.remove(regularGIRO);
        entityManager.flush();

        return "Successfully deleted!";
    }
}
