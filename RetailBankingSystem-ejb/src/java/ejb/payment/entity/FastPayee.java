package ejb.payment.entity;

import ejb.deposit.entity.Payee;
import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class FastPayee extends Payee implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fastPayeeName;

    public String getFastPayeeName() {
        return fastPayeeName;
    }

    public void setFastPayeeName(String fastPayeeName) {
        this.fastPayeeName = fastPayeeName;
    }
}
