package ejb.payment.entity;

import ejb.deposit.entity.Payee;
import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class SWIFTPayee extends Payee implements Serializable {

    private static final long serialVersionUID = 1L;
    private String swiftInstitution;
    private String swiftPayeeCode;
    private String swiftPayeeCountry;

    public String getSwiftInstitution() {
        return swiftInstitution;
    }

    public void setSwiftInstitution(String swiftInstitution) {
        this.swiftInstitution = swiftInstitution;
    }

    public String getSwiftPayeeCode() {
        return swiftPayeeCode;
    }

    public void setSwiftPayeeCode(String swiftPayeeCode) {
        this.swiftPayeeCode = swiftPayeeCode;
    }

    public String getSwiftPayeeCountry() {
        return swiftPayeeCountry;
    }

    public void setSwiftPayeeCountry(String swiftPayeeCountry) {
        this.swiftPayeeCountry = swiftPayeeCountry;
    }
}
