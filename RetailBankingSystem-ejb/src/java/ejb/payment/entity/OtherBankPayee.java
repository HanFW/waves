package ejb.payment.entity;

import ejb.deposit.entity.Payee;
import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class OtherBankPayee extends Payee implements Serializable {

    private static final long serialVersionUID = 1L;
    private String otherBankPayeeName;

    public String getOtherBankPayeeName() {
        return otherBankPayeeName;
    }

    public void setOtherBankPayeeName(String otherBankPayeeName) {
        this.otherBankPayeeName = otherBankPayeeName;
    }

}
