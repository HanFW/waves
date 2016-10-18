package ejb.payement.session;

import javax.ejb.Local;

@Local
public interface RecurrentGIROSessionBeanLocal {
    public Long addNewRecurrentGIRO(String billingOrganization, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentFrequency, String paymentAmt, 
            String giroType, Long customerBasicId);
}
