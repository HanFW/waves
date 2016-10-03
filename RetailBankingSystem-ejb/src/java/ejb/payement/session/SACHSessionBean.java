package ejb.payement.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.FastPayee;
import ejb.payment.entity.SACH;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SACHSessionBean implements SACHSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private DBSBankSessionBeanLocal dBSBankSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void SACHTransfer(String fromBankAccount, String toBankAccount, Double transferAmt) {

        Long sachId = Long.valueOf(1);
        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = Double.valueOf(sach.getDbsTotalCredit()) + transferAmt;
        Double merlionTotalCredit = Double.valueOf(sach.getDbsTotalCredit()) - transferAmt;

        sach.setDbsTotalCredit(dbsTotalCredit.toString());
        sach.setMerlionTotalCredit(merlionTotalCredit.toString());

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromBankAccount);
        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByAccNum(fromBankAccount);
        List<FastPayee> fastPayee = customerBasic.getFastPayee();
        Calendar cal = Calendar.getInstance();
        
        for (int i = 0; i < fastPayee.size(); i++) {
            if(fastPayee.get(i).getFastPayeeAccountNum().equals(toBankAccount)) {
                fastPayee.get(i).setLastTransactionDate(cal.getTime().toString());
            }
        }

        dBSBankSessionBeanLocal.actualTransfer(fromBankAccount, toBankAccount, transferAmt);
    }

    @Override
    public SACH retrieveSACHById(Long sachId) {
        SACH sach = new SACH();

        try {
            Query query = entityManager.createQuery("Select sach From SACH sach Where sach.sachId=:sachId");
            query.setParameter("sachId", sachId);

            if (query.getResultList().isEmpty()) {
                return new SACH();
            } else {
                sach = (SACH) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SACH();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return sach;
    }
}
