package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class IssuedCheque extends Cheque implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String issuedChequeStatus;

    public String getIssuedChequeStatus() {
        return issuedChequeStatus;
    }

    public void setIssuedChequeStatus(String issuedChequeStatus) {
        this.issuedChequeStatus = issuedChequeStatus;
    }
}
