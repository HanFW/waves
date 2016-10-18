package ejb.payement.session;

import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.OneTimeGIRO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OneTimeGIROSessionBean implements OneTimeGIROSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;
    
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @Override
    public Long addNewOneTimeGIRO (String billingOrganization, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentAmt, Long customerBasicId) {
        OneTimeGIRO oneTimeGiro = new OneTimeGIRO();
        
        oneTimeGiro.setBankAccountNum(bankAccountNum);
        oneTimeGiro.setBankAccountNumWithType(bankAccountNumWithType);
        oneTimeGiro.setBillReference(billReference);
        oneTimeGiro.setBillingOrganization(billingOrganization);
        oneTimeGiro.setPaymentAmt(paymentAmt);
        oneTimeGiro.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));
        
        entityManager.persist(oneTimeGiro);
        entityManager.flush();
        
        return oneTimeGiro.getGiroId();
    }
}
