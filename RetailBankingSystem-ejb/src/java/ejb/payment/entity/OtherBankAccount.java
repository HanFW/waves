package ejb.payment.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OtherBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherBankAccountId;
    private String otherBankAccountNum;
    private String otherBankAccountType;
    private String otherBankAccountBalance;
    private String bankName;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "otherBankAccount")
    private List<OtherBankAccountTransaction> otherBankAccountTransaction;

    public Long getOtherBankAccountId() {
        return otherBankAccountId;
    }

    public void setOtherBankAccountId(Long otherBankAccountId) {
        this.otherBankAccountId = otherBankAccountId;
    }

    public String getOtherBankAccountNum() {
        return otherBankAccountNum;
    }

    public void setOtherBankAccountNum(String otherBankAccountNum) {
        this.otherBankAccountNum = otherBankAccountNum;
    }

    public String getOtherBankAccountType() {
        return otherBankAccountType;
    }

    public void setOtherBankAccountType(String otherBankAccountType) {
        this.otherBankAccountType = otherBankAccountType;
    }

    public String getOtherBankAccountBalance() {
        return otherBankAccountBalance;
    }

    public void setOtherBankAccountBalance(String otherBankAccountBalance) {
        this.otherBankAccountBalance = otherBankAccountBalance;
    }

    public List<OtherBankAccountTransaction> getOtherBankAccountTransaction() {
        return otherBankAccountTransaction;
    }

    public void setOtherBankAccountTransaction(List<OtherBankAccountTransaction> otherBankAccountTransaction) {
        this.otherBankAccountTransaction = otherBankAccountTransaction;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otherBankAccountId != null ? otherBankAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtherBankAccount)) {
            return false;
        }
        OtherBankAccount other = (OtherBankAccount) object;
        if ((this.otherBankAccountId == null && other.otherBankAccountId != null) || (this.otherBankAccountId != null && !this.otherBankAccountId.equals(other.otherBankAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.DBSBankAccount[ id=" + otherBankAccountId + " ]";
    }
    
}
