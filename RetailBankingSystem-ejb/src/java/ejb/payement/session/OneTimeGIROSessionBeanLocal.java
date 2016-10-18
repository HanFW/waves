package ejb.payement.session;

import javax.ejb.Local;

@Local
public interface OneTimeGIROSessionBeanLocal {
    public Long addNewOneTimeGIRO (String billingOrganization, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentAmt, String giroType, Long customerBasicId);
}
