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
public class MEPSMasterBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long masterBankAccountId;
    private String bankName;
    private String masterBankAccountNum;
    private String masterBankAccountBalance;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "mepsMasterBankAccount")
    private List<MEPSMasterAccountTransaction> mepsMasterAccountTransaction;
    
    public Long getMasterBankAccountId() {
        return masterBankAccountId;
    }

    public void setMasterBankAccountId(Long masterBankAccountId) {
        this.masterBankAccountId = masterBankAccountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMasterBankAccountNum() {
        return masterBankAccountNum;
    }

    public void setMasterBankAccountNum(String masterBankAccountNum) {
        this.masterBankAccountNum = masterBankAccountNum;
    }

    public String getMasterBankAccountBalance() {
        return masterBankAccountBalance;
    }

    public void setMasterBankAccountBalance(String masterBankAccountBalance) {
        this.masterBankAccountBalance = masterBankAccountBalance;
    }

    public List<MEPSMasterAccountTransaction> getMepsMasterAccountTransaction() {
        return mepsMasterAccountTransaction;
    }

    public void setMepsMasterAccountTransaction(List<MEPSMasterAccountTransaction> mepsMasterAccountTransaction) {
        this.mepsMasterAccountTransaction = mepsMasterAccountTransaction;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (masterBankAccountId != null ? masterBankAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MEPSMasterBankAccount)) {
            return false;
        }
        MEPSMasterBankAccount other = (MEPSMasterBankAccount) object;
        if ((this.masterBankAccountId == null && other.masterBankAccountId != null) || (this.masterBankAccountId != null && !this.masterBankAccountId.equals(other.masterBankAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.MasterBankAccount[ id=" + masterBankAccountId + " ]";
    }
    
}
