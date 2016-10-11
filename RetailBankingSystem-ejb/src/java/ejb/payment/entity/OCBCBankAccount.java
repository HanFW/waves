package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class OCBCBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ocbcBankAccountId;
    private String ocbcBankAccountNum;
    private String ocbcBankAccountType;
    private String ocbcBankAccountBalance;

    public Long getOcbcBankAccountId() {
        return ocbcBankAccountId;
    }

    public void setOcbcBankAccountId(Long ocbcBankAccountId) {
        this.ocbcBankAccountId = ocbcBankAccountId;
    }

    public String getOcbcBankAccountNum() {
        return ocbcBankAccountNum;
    }

    public void setOcbcBankAccountNum(String ocbcBankAccountNum) {
        this.ocbcBankAccountNum = ocbcBankAccountNum;
    }

    public String getOcbcBankAccountType() {
        return ocbcBankAccountType;
    }

    public void setOcbcBankAccountType(String ocbcBankAccountType) {
        this.ocbcBankAccountType = ocbcBankAccountType;
    }

    public String getOcbcBankAccountBalance() {
        return ocbcBankAccountBalance;
    }

    public void setOcbcBankAccountBalance(String ocbcBankAccountBalance) {
        this.ocbcBankAccountBalance = ocbcBankAccountBalance;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof OCBCBankAccount)) {
//            return false;
//        }
//        OCBCBankAccount other = (OCBCBankAccount) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "ejb.payment.entity.OCBCBankAccount[ id=" + id + " ]";
//    }
    
}
