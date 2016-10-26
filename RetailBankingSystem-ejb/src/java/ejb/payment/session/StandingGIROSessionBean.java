package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.StandingGIRO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.bill.BillWebService_Service;

@Stateless
public class StandingGIROSessionBean implements StandingGIROSessionBeanLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/BillWebService/BillWebService.wsdl")
    private BillWebService_Service service_bill;

    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewStandingGIRO(String billingOrganizationName, String billReference, String paymemtLimit,
            String customerName, String customerMobile, String bankAccountNum, String standingGiroStatus,
            String bankAccountNumWithType, String giroType, Long customerBasicId) {

        StandingGIRO standingGiro = new StandingGIRO();

        standingGiro.setBankAccountNum(bankAccountNum);
        standingGiro.setBillReference(billReference);
        standingGiro.setBillingOrganizationName(billingOrganizationName);
        standingGiro.setCustomerMobile(customerMobile);
        standingGiro.setCustomerName(customerName);
        standingGiro.setPaymentLimit(paymemtLimit);
        standingGiro.setStandingGiroStatus(standingGiroStatus);
        standingGiro.setBankAccountNumWithType(bankAccountNumWithType);
        standingGiro.setGiroType(giroType);
        standingGiro.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        System.out.println("*******session bean" + billingOrganizationName);
        addNewBill(customerName, customerMobile, billReference, billingOrganizationName, "Merlion", bankAccountNum);

        entityManager.persist(standingGiro);
        entityManager.flush();

        return standingGiro.getGiroId();
    }

    @Override
    public String deleteStandingGIRO(Long giroId) {
        GIRO standingGiro = gIROSessionBeanLocal.retrieveGIROById(giroId);

        entityManager.remove(standingGiro);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public List<StandingGIRO> retrieveStandingGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<StandingGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select s From StandingGIRO s Where s.customerBasic=:customerBasic And s.giroType=:giroType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Standing");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<StandingGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<StandingGIRO>();
        }
    }

    private Long addNewBill(java.lang.String customerName, java.lang.String customerMobile, java.lang.String billReference, java.lang.String billingOrganizationName, java.lang.String debitBank, java.lang.String debitBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.bill.BillWebService port = service_bill.getBillWebServicePort();
        return port.addNewBill(customerName, customerMobile, billReference,
                billingOrganizationName, debitBank, debitBankAccountNum);
    }
}
