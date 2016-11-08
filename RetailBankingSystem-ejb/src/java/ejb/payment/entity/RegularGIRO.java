package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class RegularGIRO extends GIRO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String paymentAmt;
    private String paymentFrequency;
    private String payeeBankName;
    private String payeeAccountNum;
    private String regularGIROStatus;
    private String payeeName;

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }

    public String getRegularGIROStatus() {
        return regularGIROStatus;
    }

    public void setRegularGIROStatus(String regularGIROStatus) {
        this.regularGIROStatus = regularGIROStatus;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    
}
