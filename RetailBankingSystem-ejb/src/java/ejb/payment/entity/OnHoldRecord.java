package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OnHoldRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long onHoldRecordId;
    private String bankName;
    private String bankAccountNum;
    private String debitOrCredit;
    private String paymentAmt;
    private String onHoldStatus;
    private String debitOrCreditBankName;
    private String debitOrCreditBankAccountNum;
    private String paymentMethod;
    private Long chequeId;

    public Long getOnHoldRecordId() {
        return onHoldRecordId;
    }

    public void setOnHoldRecordId(Long onHoldRecordId) {
        this.onHoldRecordId = onHoldRecordId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getOnHoldStatus() {
        return onHoldStatus;
    }

    public void setOnHoldStatus(String onHoldStatus) {
        this.onHoldStatus = onHoldStatus;
    }

    public String getDebitOrCreditBankName() {
        return debitOrCreditBankName;
    }

    public void setDebitOrCreditBankName(String debitOrCreditBankName) {
        this.debitOrCreditBankName = debitOrCreditBankName;
    }

    public String getDebitOrCreditBankAccountNum() {
        return debitOrCreditBankAccountNum;
    }

    public void setDebitOrCreditBankAccountNum(String debitOrCreditBankAccountNum) {
        this.debitOrCreditBankAccountNum = debitOrCreditBankAccountNum;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getChequeId() {
        return chequeId;
    }

    public void setChequeId(Long chequeId) {
        this.chequeId = chequeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (onHoldRecordId != null ? onHoldRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OnHoldRecord)) {
            return false;
        }
        OnHoldRecord other = (OnHoldRecord) object;
        if ((this.onHoldRecordId == null && other.onHoldRecordId != null) || (this.onHoldRecordId != null
                && !this.onHoldRecordId.equals(other.onHoldRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.OnHoldRecordId[ id=" + onHoldRecordId + " ]";
    }

}
