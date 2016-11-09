package ejb.loan.session;

import ejb.loan.entity.LoanPayableAccount;
import javax.ejb.Local;

@Local
public interface LoanPayableAccountSessionBeanLocal {

    public LoanPayableAccount retrieveLoanPayableAccountById(Long loanPayableAccountId);

    public LoanPayableAccount retrieveLoanPayableAccountByNum(String loanPayableAccountNum);
}
