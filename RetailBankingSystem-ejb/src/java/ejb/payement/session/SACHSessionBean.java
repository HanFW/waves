package ejb.payement.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.DBSBankAccount;
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
    private DBSBankAccountSessionBeanLocal dBSBankAccountSessionBeanLocal;
    
    @EJB
    private SACHMasterAccountTransactionSessionBeanLocal sACHMasterAccountTransactionSessionBean;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private DBSBankSessionBeanLocal dBSBankSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void SACHTransfer(String fromBankAccount, String toBankAccount, Double transferAmt) {

        List<SACH> sachs = getAllSACH("DBS&Merlion");
        int size = sachs.size();
        Long sachId = sachs.get(size - 1).getSachId();
        Calendar cal = Calendar.getInstance();

        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = sach.getDbsTotalCredit() + transferAmt;
        Double merlionTotalCredit = sach.getMerlionTotalCredit() - transferAmt;

        sach.setDbsTotalCredit(dbsTotalCredit);
        sach.setMerlionTotalCredit(merlionTotalCredit);
        
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromBankAccount);
        DBSBankAccount dbsBankAccount = dBSBankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccount);
        
        String dbsTransactionRef = bankAccount.getBankAccountNum()+"-"+bankAccount.getBankAccountType()+" Transfer To "+dbsBankAccount.getDbsBankAccountNum()+"-"+dbsBankAccount.getDbsBankAccountType();
        
        Long dbsTransactionId = sACHMasterAccountTransactionSessionBean.addNewMasterAccountTransaction(cal.getTime().toString(), dbsTransactionRef, " ", transferAmt.toString(), Long.valueOf(1));
        Long merlionTransactionId = sACHMasterAccountTransactionSessionBean.addNewMasterAccountTransaction(cal.getTime().toString(), dbsTransactionRef, transferAmt.toString(), " ", Long.valueOf(2));

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByAccNum(fromBankAccount);
        List<FastPayee> fastPayee = customerBasic.getFastPayee();

        for (int i = 0; i < fastPayee.size(); i++) {
            if (fastPayee.get(i).getFastPayeeAccountNum().equals(toBankAccount)) {
                fastPayee.get(i).setLastTransactionDate(cal.getTime().toString());
            }
        }

        dBSBankSessionBeanLocal.actualTransfer(fromBankAccount, toBankAccount, transferAmt);
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
    public Long addNewSACH(String dbsAccountBalance, String merlionAccountBalance, Double dbsTotalCredit,
            Double merlionTotalCredit, String updateDate, String bankNames) {

        SACH sach = new SACH();

        sach.setDbsAccountBalance(dbsAccountBalance);
        sach.setDbsTotalCredit(dbsTotalCredit);
        sach.setMerlionAccountBalance(merlionAccountBalance);
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
}
