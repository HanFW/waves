package ejb.loan.session;

import ejb.loan.entity.LoanPayableAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class LoanPayableAccountSessionBean implements LoanPayableAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LoanPayableAccount retrieveLoanPayableAccountById(Long loanPayableAccountId) {
        LoanPayableAccount loanPayableAccount = new LoanPayableAccount();

        try {
            Query query = entityManager.createQuery("Select l From LoanPayableAccount l Where l.id=:loanPayableAccountId");
            query.setParameter("loanPayableAccountId", loanPayableAccountId);

            if (query.getResultList().isEmpty()) {
                return new LoanPayableAccount();
            } else {
                loanPayableAccount = (LoanPayableAccount) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new LoanPayableAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return loanPayableAccount;
    }
    
    @Override
    public LoanPayableAccount retrieveLoanPayableAccountByNum(String loanPayableAccountNum) {
        LoanPayableAccount loanPayableAccount = new LoanPayableAccount();

        try {
            Query query = entityManager.createQuery("Select l From LoanPayableAccount l Where l.accountNumber=:loanPayableAccountNum");
            query.setParameter("loanPayableAccountNum", loanPayableAccountNum);

            if (query.getResultList().isEmpty()) {
                return new LoanPayableAccount();
            } else {
                loanPayableAccount = (LoanPayableAccount) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new LoanPayableAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return loanPayableAccount;
    }
}
