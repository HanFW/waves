package ejb.payement.session;

import ejb.payment.entity.MEPSMasterAccountTransaction;
import ejb.payment.entity.MEPSMasterBankAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MEPSMasterAccountTransactionSessionBean implements MEPSMasterAccountTransactionSessionBeanLocal {

    @EJB
    private MEPSMasterBankAccountSessionBeanLocal mEPSMasterBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MEPSMasterAccountTransaction> retrieveMEPSMasterAccountTransactionByAccNum(String mepsMasterAccountNum) {

        MEPSMasterBankAccount mepsMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(mepsMasterAccountNum);
        
        if (mepsMasterBankAccount == null) {
            return new ArrayList<MEPSMasterAccountTransaction>();
        }
        try {
            Query query = entityManager.createQuery("Select m From MEPSMasterAccountTransaction m Where m.mepsMasterBankAccount=:mepsMasterBankAccount");
            query.setParameter("mepsMasterBankAccount", mepsMasterBankAccount);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<MEPSMasterAccountTransaction>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return null;
        }
    }
    
    @Override
    public Long addNewMasterAccountTransaction(String transactionDate, String transactionRef,
            String accountDebit, String accountCredit, Long masterBankAccountId) {
        
        MEPSMasterAccountTransaction mepsMasterAccountTransaction = new MEPSMasterAccountTransaction();

        mepsMasterAccountTransaction.setTransactionDate(transactionDate);
        mepsMasterAccountTransaction.setTransactionRef(transactionRef);
        mepsMasterAccountTransaction.setAccountDebit(accountDebit);
        mepsMasterAccountTransaction.setAccountCredit(accountCredit);
        mepsMasterAccountTransaction.setMepsMasterBankAccount(mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountById(masterBankAccountId));

        entityManager.persist(mepsMasterAccountTransaction);
        entityManager.flush();

        return mepsMasterAccountTransaction.getTransactionId();
    }
}
