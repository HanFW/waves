package ejb.payement.session;

import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.RecurrentGIRO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RecurrentGIROSessionBean implements RecurrentGIROSessionBeanLocal {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long addNewRecurrentGIRO(String billingOrganization, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentFrequency, String paymentAmt, 
            String giroType, Long customerBasicId) {
        
        RecurrentGIRO recurrentGiro = new RecurrentGIRO();
        
        recurrentGiro.setBankAccountNum(bankAccountNum);
        recurrentGiro.setBankAccountNumWithType(bankAccountNumWithType);
        recurrentGiro.setBillReference(billReference);
        recurrentGiro.setBillingOrganization(billingOrganization);
        recurrentGiro.setPaymentAmt(paymentAmt);
        recurrentGiro.setPaymentFrequency(paymentFrequency);
        recurrentGiro.setGiroType(giroType);
        recurrentGiro.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(recurrentGiro);
        entityManager.flush();
        
        return recurrentGiro.getGiroId();
    }
}
