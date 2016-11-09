package ejb.loan.session;

import javax.ejb.Local;

@Local
public interface LoanStatementSessionBeanLocal {

    public Long addNewLoanStatement(String statementType, String accountDetails);
}
