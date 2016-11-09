package ejb.loan.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class LoanStatement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanStatementId;
    private String statementType;
    private String accountDetails;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "loanStatement")
    private LoanRepaymentAccount loanRepaymentAccount;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "loanStatement")
    private LoanPayableAccount loanPayableAccount;

    public Long getLoanStatementId() {
        return loanStatementId;
    }

    public void setLoanStatementId(Long loanStatementId) {
        this.loanStatementId = loanStatementId;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public LoanRepaymentAccount getLoanRepaymentAccount() {
        return loanRepaymentAccount;
    }

    public void setLoanRepaymentAccount(LoanRepaymentAccount loanRepaymentAccount) {
        this.loanRepaymentAccount = loanRepaymentAccount;
    }

    public LoanPayableAccount getLoanPayableAccount() {
        return loanPayableAccount;
    }

    public void setLoanPayableAccount(LoanPayableAccount loanPayableAccount) {
        this.loanPayableAccount = loanPayableAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loanStatementId != null ? loanStatementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanStatement)) {
            return false;
        }
        LoanStatement other = (LoanStatement) object;
        if ((this.loanStatementId == null && other.loanStatementId != null) || (this.loanStatementId != null && !this.loanStatementId.equals(other.loanStatementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.LoanStatement[ id=" + loanStatementId + " ]";
    }

}
