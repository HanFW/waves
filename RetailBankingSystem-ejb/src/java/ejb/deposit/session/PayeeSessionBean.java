package ejb.deposit.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
public class PayeeSessionBean implements PayeeSessionBeanLocal, PayeeSessionBeanRemote {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String deletePayee(String payeeAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/PayeeSessionBean: deletePayee() ******");
        Payee payee = retrievePayeeByNum(payeeAccountNum);

        entityManager.remove(payee);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public Payee retrievePayeeById(Long payeeId) {
        System.out.println("*");
        System.out.println("****** deposit/PayeeSessionBean: retrievePayeeById() ******");
        Payee payee = new Payee();

        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeId=:payeeId");
            query.setParameter("payeeId", payeeId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeById(): invalid payee Id: no result found, return new payee");
                return new Payee();
            } else {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeById(): valid payee Id: return payee");
                payee = (Payee) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeById(): Entity not found error: " + enfe.getMessage());
            return new Payee();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeById(): Non unique result error: " + nure.getMessage());
        }

        return payee;
    }

    @Override
    public Payee retrievePayeeByNum(String payeeAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByNum() ******");
        Payee payee = new Payee();

        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeAccountNum=:payeeAccountNum");
            query.setParameter("payeeAccountNum", payeeAccountNum);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByNum(): invalid payee account number: no result found, return new payee");
                return new Payee();
            } else {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByNum(): valid payee account number: return payee");
                payee = (Payee) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByNum(): Entity not found error: " + enfe.getMessage());
            return new Payee();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByNum(): Non unique result error: " + nure.getMessage());
        }

        return payee;
    }

    @Override
    public List<Payee> retrievePayeeByCusId(Long customerBasicId) {
        System.out.println("*");
        System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByCusId() ******");
        CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByCusId(): invalid customer Id: no result found, return new payee");
            return new ArrayList<Payee>();
        }
        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByCusId(): wrong customer Id: no result found, return new payee");
                return new ArrayList<Payee>();
            } else {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByCusId(): correct customer Id: return payee");
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/PayeeSessionBean: Entity not found error: " + enfe.getMessage());
            return new ArrayList<Payee>();
        }
    }

    @Override
    public void updateLastTransactionDate(String bankAccountNum) {

        Payee customerPayee = retrievePayeeByNum(bankAccountNum);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        String lastTransactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

        customerPayee.setLastTransactionDate(lastTransactionDate);
    }
}
