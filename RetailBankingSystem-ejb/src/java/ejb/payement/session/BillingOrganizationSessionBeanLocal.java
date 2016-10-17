package ejb.payement.session;

import ejb.payment.entity.BillingOrganization;
import java.util.List;
import javax.ejb.Local;

@Local
public interface BillingOrganizationSessionBeanLocal {
    public Long addNewBillingOrganization(String billingOrganizationName, String bankAccountNum, 
            String bankAccountType, String bankName);
    public List<BillingOrganization> getAllBillingOrganization();
}
