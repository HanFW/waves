package ejb.payement.session;

import ejb.deposit.session.TransactionSessionBeanLocal;
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
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private MEPSSessionBeanLocal mEPSSessionBeanLocal;

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void SACHTransferMTD(String fromBankAccount, String toBankAccount, Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long sachId = addNewSACH(0.0, 0.0, currentTime, "DBS&Merlion");
        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transferAmt;
        Double merlionTotalCredit = 0 - transferAmt;

        sach.setOtherBankTotalCredit(dbsTotalCredit);
        sach.setMerlionTotalCredit(merlionTotalCredit);

        otherBankSessionBeanLocal.actualTransfer(fromBankAccount, toBankAccount, transferAmt);
        mEPSSessionBeanLocal.MEPSSettlementMTD("88776655", "44332211", transferAmt);
    }

    @Override
    public SACH retrieveSACHById(Long sachId) {
        SACH sach = new SACH();

        try {
            Query query = entityManager.createQuery("Select s From SACH s Where s.sachId=:sachId");
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

    @Override
    public Long addNewSACH(Double otherTotalCredit, Double merlionTotalCredit, String updateDate, String bankNames) {

        SACH sach = new SACH();

        sach.setOtherBankTotalCredit(otherTotalCredit);
        sach.setMerlionTotalCredit(merlionTotalCredit);
        sach.setUpdateDate(updateDate);
        sach.setBankNames(bankNames);

        entityManager.persist(sach);
        entityManager.flush();

        return sach.getSachId();
    }

    @Override
    public List<SACH> getAllSACH(String bankNames) {

        Query query = entityManager.createQuery("SELECT s FROM SACH s Where s.bankNames=:bankNames");
        query.setParameter("bankNames", bankNames);

        return query.getResultList();
    }
    
    @Override
    public void SACHTransferDTM(String fromBankAccount, String toBankAccount, Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long sachId = addNewSACH(0.0, 0.0, currentTime, "DBS&Merlion");
        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 - transferAmt;
        Double merlionTotalCredit = 0 + transferAmt;

        sach.setOtherBankTotalCredit(merlionTotalCredit);
        sach.setMerlionTotalCredit(merlionTotalCredit);

        transactionSessionBeanLocal.fastTransfer(fromBankAccount, toBankAccount, transferAmt);
        mEPSSessionBeanLocal.MEPSSettlementDTM("44332211", "88776655", transferAmt);
    }
}
