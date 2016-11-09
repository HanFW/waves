package ejb.loan.session;

import ejb.loan.entity.LoanStatement;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class LoanStatementSessionBean implements LoanStatementSessionBeanLocal {

    @EJB
    private LoanPayableAccountSessionBeanLocal loanPayableAccountSessionBeanLocal;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewLoanStatement(String statementType, String accountDetails) {
        LoanStatement loanStatement = new LoanStatement();
        
        loanStatement.setAccountDetails(accountDetails);
        loanStatement.setStatementType(statementType);
        
        entityManager.persist(loanStatement);
        entityManager.flush();
        
        return loanStatement.getLoanStatementId();
    }
}
