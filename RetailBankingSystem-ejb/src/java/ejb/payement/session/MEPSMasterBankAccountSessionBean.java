package ejb.payement.session;

import ejb.payment.entity.MEPSMasterBankAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MEPSMasterBankAccountSessionBean implements MEPSMasterBankAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountByBankName(String bankName) {

        MEPSMasterBankAccount mepsMasterBankAccount = new MEPSMasterBankAccount();

        try {
            Query query = entityManager.createQuery("Select m From MEPSMasterBankAccount m Where m.bankName=:bankName");
            query.setParameter("bankName", bankName);

            if (query.getResultList().isEmpty()) {
                return new MEPSMasterBankAccount();
            } else {
                mepsMasterBankAccount = (MEPSMasterBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new MEPSMasterBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return mepsMasterBankAccount;
    }

    @Override
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountByAccNum(String masterBankAccountNum) {

        MEPSMasterBankAccount mepsMasterBankAccount = new MEPSMasterBankAccount();

        try {
            Query query = entityManager.createQuery("Select m From MEPSMasterBankAccount m Where m.masterBankAccountNum=:masterBankAccountNum");
            query.setParameter("masterBankAccountNum", masterBankAccountNum);

            if (query.getResultList().isEmpty()) {
                return new MEPSMasterBankAccount();
            } else {
                mepsMasterBankAccount = (MEPSMasterBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new MEPSMasterBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return mepsMasterBankAccount;
    }

    @Override
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountById(Long masterBankAccountId) {

        MEPSMasterBankAccount mepsMasterBankAccount = new MEPSMasterBankAccount();

        try {
            Query query = entityManager.createQuery("Select m From MEPSMasterBankAccount m Where m.masterBankAccountId=:masterBankAccountId");
            query.setParameter("masterBankAccountId", masterBankAccountId);

            if (query.getResultList().isEmpty()) {
                return new MEPSMasterBankAccount();
            } else {
                mepsMasterBankAccount = (MEPSMasterBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new MEPSMasterBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return mepsMasterBankAccount;
    }

    @Override
    public void maintainDailyBalance() {

        Query query = entityManager.createQuery("SELECT m FROM MEPSMasterBankAccount m");
        List<MEPSMasterBankAccount> mepsMasterBankAccounts = query.getResultList();

        for (MEPSMasterBankAccount mepsMasterBankAccount : mepsMasterBankAccounts) {

            String currentBalance = mepsMasterBankAccount.getMasterBankAccountBalance();
            Double currentBalanceDouble = Double.valueOf(currentBalance);

            if (currentBalanceDouble < 100000) {
                Double diffBalance = 100000 - Double.valueOf(currentBalance);
                Double totalBalance = Double.valueOf(currentBalance) + diffBalance;

                mepsMasterBankAccount.setMasterBankAccountBalance(totalBalance.toString());
            }
        }
    }
}
