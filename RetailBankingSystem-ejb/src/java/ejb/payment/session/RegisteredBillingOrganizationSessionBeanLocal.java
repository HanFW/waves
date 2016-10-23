package ejb.payment.session;

import ejb.payment.entity.RegisteredBillingOrganization;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RegisteredBillingOrganizationSessionBeanLocal {
    public Long addNewRegisteredBillingOrganization(String billingOrganizationName, String bankAccountNum, 
            String bankAccountType, String bankName);
    public List<RegisteredBillingOrganization> getAllRegisteredBillingOrganization();
    public RegisteredBillingOrganization retrieveRegisteredBillingOrganizationByName(String billingOrganizationName);
}
