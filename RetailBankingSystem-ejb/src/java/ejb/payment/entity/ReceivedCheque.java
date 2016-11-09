package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class ReceivedCheque extends Cheque implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String receivedBankAccountNum;
    private String receivedCustomerName;
    private String receivedCustomerMobile;
    private String receivedChequeStatus;
    private String otherBankAccountNum;

    public String getReceivedBankAccountNum() {
        return receivedBankAccountNum;
    }

    public void setReceivedBankAccountNum(String receivedBankAccountNum) {
        this.receivedBankAccountNum = receivedBankAccountNum;
    }

    public String getReceivedCustomerName() {
        return receivedCustomerName;
    }

    public void setReceivedCustomerName(String receivedCustomerName) {
        this.receivedCustomerName = receivedCustomerName;
    }

    public String getReceivedCustomerMobile() {
        return receivedCustomerMobile;
    }

    public void setReceivedCustomerMobile(String receivedCustomerMobile) {
        this.receivedCustomerMobile = receivedCustomerMobile;
    }

    public String getReceivedChequeStatus() {
        return receivedChequeStatus;
    }

    public void setReceivedChequeStatus(String receivedChequeStatus) {
        this.receivedChequeStatus = receivedChequeStatus;
    }

    public String getOtherBankAccountNum() {
        return otherBankAccountNum;
    }

    public void setOtherBankAccountNum(String otherBankAccountNum) {
        this.otherBankAccountNum = otherBankAccountNum;
    }
}
