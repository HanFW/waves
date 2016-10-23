package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class NonStandingGIRO extends GIRO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String paymentAmt;
    private String paymentFrequency;

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
    
}
