package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class StandingGIRO extends GIRO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String paymentLimit;
    private String customerName;
    private String customerMobile;
    private String standingGiroStatus;
    private String billingOrganizationName;
    private String billReference;

    public String getPaymentLimit() {
        return paymentLimit;
    }

    public void setPaymentLimit(String paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getStandingGiroStatus() {
        return standingGiroStatus;
    }

    public void setStandingGiroStatus(String standingGiroStatus) {
        this.standingGiroStatus = standingGiroStatus;
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }
}
