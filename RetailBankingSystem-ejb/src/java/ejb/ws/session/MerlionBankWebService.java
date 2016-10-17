package ejb.ws.session;

import ejb.deposit.entity.BankAccount;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@WebService(serviceName = "MerlionBankWebService")
@Stateless()

public class MerlionBankWebService {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;

    @WebMethod(operationName = "retrieveBankAccountByNum")
    public BankAccount retrieveBankAccountByNum(@WebParam(name = "bankAccountNum") String bankAccountNum) {
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
            query.setParameter("bankAccountNum", bankAccountNum);

            if (query.getResultList().isEmpty()) {
                return new BankAccount();
            } else {
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        entityManager.detach(bankAccount);
        bankAccount.setAccTransaction(null);
        bankAccount.setCustomerBasic(null);
        bankAccount.setInterest(null);
        bankAccount.setStatement(null);

        return bankAccount;
    }

}
