package ejb.deposit.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class RegularPayee extends Payee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String payeeName;

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
}
