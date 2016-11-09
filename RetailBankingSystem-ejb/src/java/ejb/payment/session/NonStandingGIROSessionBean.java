package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.entity.RegisteredBillingOrganization;
import ejb.payment.entity.RegularGIRO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;
import ws.client.sach.SACHWebService_Service;

@Stateless
public class NonStandingGIROSessionBean implements NonStandingGIROSessionBeanLocal {

    @EJB
    private OtherBankPayeeSessionBeanLocal otherBankPayeeSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service_otherBank;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/SACHWebService/SACHWebService.wsdl")
    private SACHWebService_Service service_sach;

    @EJB
    private RegisteredBillingOrganizationSessionBeanLocal registeredBillingOrganizationSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public Long addNewNonStandingGIRO(String billingOrganizationName, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentFrequency, String paymentAmt,
            String giroType, String nonStandingStatus, boolean buttonRender, Long customerBasicId) {

        NonStandingGIRO nonStandingGIRO = new NonStandingGIRO();

        nonStandingGIRO.setBankAccountNum(bankAccountNum);
        nonStandingGIRO.setBankAccountNumWithType(bankAccountNumWithType);
        nonStandingGIRO.setBillReference(billReference);
        nonStandingGIRO.setBillingOrganizationName(billingOrganizationName);
        nonStandingGIRO.setPaymentAmt(paymentAmt);
        nonStandingGIRO.setPaymentFrequency(paymentFrequency);
        nonStandingGIRO.setGiroType(giroType);
        nonStandingGIRO.setNonStandingStatus(nonStandingStatus);
        nonStandingGIRO.setButtonRender(buttonRender);
        nonStandingGIRO.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        String debitBank = "Merlion";
        String billStatus = "Pending";
        RegisteredBillingOrganization billOrg = registeredBillingOrganizationSessionBeanLocal.retrieveRegisteredBillingOrganizationByName(billingOrganizationName);

        passNonStandingGIROToSACH(" ", " ", billReference, billingOrganizationName,
                billOrg.getBankName(), billOrg.getBankAccountNum(), debitBank,
                bankAccountNum, " ", billStatus, paymentFrequency);

        entityManager.persist(nonStandingGIRO);
        entityManager.flush();

        return nonStandingGIRO.getGiroId();
    }

    @Override
    public List<NonStandingGIRO> retrieveNonStandingGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<NonStandingGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select g From NonStandingGIRO g Where g.customerBasic=:customerBasic And g.giroType=:giroType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Non Standing");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<NonStandingGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<NonStandingGIRO>();
        }
    }

    @Override
    public NonStandingGIRO retrieveNonStandingGIROById(Long giroId) {
        NonStandingGIRO nonStandingGIRO = new NonStandingGIRO();

        try {
            Query query = entityManager.createQuery("Select n From NonStandingGIRO n Where n.giroId=:giroId");
            query.setParameter("giroId", giroId);

            if (query.getResultList().isEmpty()) {
                return new NonStandingGIRO();
            } else {
                nonStandingGIRO = (NonStandingGIRO) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new NonStandingGIRO();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return nonStandingGIRO;
    }

    @Override
    public void updatePaymentAmt(Long giroId, String paymentAmt) {
        NonStandingGIRO nonStandingGiro = retrieveNonStandingGIROById(giroId);

        nonStandingGiro.setPaymentAmt(paymentAmt);
        nonStandingGiro.setButtonRender(true);
    }

    @Override
    public void weeklyRecurrentPayment() {

        Query query = entityManager.createQuery("SELECT n FROM NonStandingGIRO n WHERE n.paymentFrequency = :paymentFrequency And n.giroType=:giroType");
        query.setParameter("paymentFrequency", "Weekly");
        query.setParameter("giroType", "Non Standing");
        List<NonStandingGIRO> nonStandingGiros = query.getResultList();

        if (nonStandingGiros.isEmpty()) {
            System.out.println("No weekly recurrent giro");
        } else {

            for (NonStandingGIRO nonStandingGiro : nonStandingGiros) {
                String billingOrganizationName = nonStandingGiro.getBillingOrganizationName();
                String bankAccountNum = nonStandingGiro.getBankAccountNum();

                BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

                RegisteredBillingOrganization billOrg = registeredBillingOrganizationSessionBeanLocal.retrieveRegisteredBillingOrganizationByName(billingOrganizationName);
                String bankName = billOrg.getBankName();
                String billOrgBankAccountNum = billOrg.getBankAccountNum();
                String paymentAmt = nonStandingGiro.getPaymentAmt();

                if (paymentAmt != null) {

                    Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(currentAvailableBalance.toString());

                    Calendar cal = Calendar.getInstance();
                    String currentTime = cal.getTime().toString();
                    String transactionCode = "BILL";
                    String transactionRef = "Pay bills to " + billingOrganizationName;
                    String accountDebit = paymentAmt;

                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(currentTime, transactionCode,
                            transactionRef, accountDebit, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    if (bankName.equals("DBS")) {
                        sachNonStandingGIROTransferMTD(bankAccount.getBankAccountNum(), billOrgBankAccountNum, Double.valueOf(paymentAmt));
                    }
                }
            }
        }
    }

    @Override
    public void dailyRecurrentPayment() {

        Query query = entityManager.createQuery("SELECT n FROM NonStandingGIRO n WHERE n.paymentFrequency = :paymentFrequency And n.giroType=:giroType");
        query.setParameter("paymentFrequency", "Daily");
        query.setParameter("giroType", "Non Standing");
        List<NonStandingGIRO> nonStandingGiros = query.getResultList();

        if (nonStandingGiros.isEmpty()) {
            System.out.println("No daily recurrent giro");
        } else {

            for (NonStandingGIRO nonStandingGiro : nonStandingGiros) {

                String billingOrganizationName = nonStandingGiro.getBillingOrganizationName();
                String bankAccountNum = nonStandingGiro.getBankAccountNum();

                BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

                RegisteredBillingOrganization billOrg = registeredBillingOrganizationSessionBeanLocal.retrieveRegisteredBillingOrganizationByName(billingOrganizationName);
                String bankName = billOrg.getBankName();
                String billOrgBankAccountNum = billOrg.getBankAccountNum();
                String paymentAmt = nonStandingGiro.getPaymentAmt();

                if (paymentAmt != null) {

                    Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(currentAvailableBalance.toString());

                    Calendar cal = Calendar.getInstance();
                    String currentTime = cal.getTime().toString();
                    String transactionCode = "BILL";
                    String transactionRef = "Pay bills to " + billingOrganizationName;
                    String accountDebit = paymentAmt;

                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(currentTime, transactionCode,
                            transactionRef, accountDebit, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    if (bankName.equals("DBS")) {
                        sachNonStandingGIROTransferMTD(bankAccount.getBankAccountNum(), billOrgBankAccountNum, Double.valueOf(paymentAmt));
                    }
                }
            }
        }
    }

    @Override
    public void monthlyRecurrentPayment() {

        Query query = entityManager.createQuery("SELECT n FROM NonStandingGIRO n WHERE n.paymentFrequency = :paymentFrequency And n.giroType=:giroType");
        query.setParameter("paymentFrequency", "Monthly");
        query.setParameter("giroType", "Non Standing");
        List<NonStandingGIRO> nonStandingGiros = query.getResultList();

        if (nonStandingGiros.isEmpty()) {
            System.out.println("No monthly recurrent giro");
        } else {

            for (NonStandingGIRO nonStandingGiro : nonStandingGiros) {

                String billingOrganizationName = nonStandingGiro.getBillingOrganizationName();
                String bankAccountNum = nonStandingGiro.getBankAccountNum();

                BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

                RegisteredBillingOrganization billOrg = registeredBillingOrganizationSessionBeanLocal.retrieveRegisteredBillingOrganizationByName(billingOrganizationName);
                String bankName = billOrg.getBankName();
                String billOrgBankAccountNum = billOrg.getBankAccountNum();
                String paymentAmt = nonStandingGiro.getPaymentAmt();

                if (paymentAmt != null) {

                    Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(currentAvailableBalance.toString());

                    Calendar cal = Calendar.getInstance();
                    String currentTime = cal.getTime().toString();
                    String transactionCode = "BILL";
                    String transactionRef = "Pay bills to " + billingOrganizationName;
                    String accountDebit = paymentAmt;

                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(currentTime, transactionCode,
                            transactionRef, accountDebit, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    if (bankName.equals("DBS")) {
                        sachNonStandingGIROTransferMTD(bankAccount.getBankAccountNum(), billOrgBankAccountNum, Double.valueOf(paymentAmt));
                    }
                }
            }
        }
    }

    @Override
    public List<NonStandingGIRO> retrieveOneTimeGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<NonStandingGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select g From NonStandingGIRO g Where g.customerBasic=:customerBasic And g.giroType=:giroType And g.paymentFrequency=:paymentFrequency");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Non Standing");
            query.setParameter("paymentFrequency", "One Time");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<NonStandingGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<NonStandingGIRO>();
        }
    }

    @Override
    public List<NonStandingGIRO> retrieveRecurrentGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<NonStandingGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select g From NonStandingGIRO g Where g.customerBasic=:customerBasic And g.giroType=:giroType And g.paymentFrequency<>:paymentFrequency");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Non Standing");
            query.setParameter("paymentFrequency", "One Time");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<NonStandingGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<NonStandingGIRO>();
        }
    }

    @Override
    public void dailyRecurrentRegularGIROTransfer() {

        Query query = entityManager.createQuery("SELECT r FROM RegularGIRO r WHERE r.paymentFrequency = :paymentFrequency And r.giroType=:giroType And r.regularGIROStatus=:regularGIROStatus");
        query.setParameter("paymentFrequency", "Daily");
        query.setParameter("giroType", "Regular GIRO");
        query.setParameter("regularGIROStatus", "Active");
        List<RegularGIRO> regularGIROs = query.getResultList();

        if (regularGIROs.isEmpty()) {
            System.out.println("No daily recurrent regular giro");
        } else {

            for (RegularGIRO regularGIRO : regularGIROs) {

                String bankAccountNum = regularGIRO.getBankAccountNum();
                String payeeAccountNum = regularGIRO.getPayeeAccountNum();

                BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
                OtherBankAccount otherBankAccount = retrieveBankAccountByNum(payeeAccountNum);

                String paymentAmt = regularGIRO.getPaymentAmt();
                String payeeBankName = regularGIRO.getPayeeBankName();

                if (paymentAmt != null) {

                    Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(currentAvailableBalance.toString());

                    otherBankPayeeSessionBeanLocal.updateLastTransactionDate(payeeAccountNum);

                    Calendar cal = Calendar.getInstance();
                    String currentTime = cal.getTime().toString();
                    String transactionCode = "GIRO";
                    String transactionRef = otherBankAccount.getOtherBankAccountType() + otherBankAccount.getOtherBankAccountNum();
                    String accountDebit = paymentAmt;

                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(currentTime, transactionCode,
                            transactionRef, accountDebit, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    if (payeeBankName.equals("DBS")) {
                        sachRegularGIROTransferMTD(bankAccountNum, payeeAccountNum, Double.valueOf(paymentAmt));
                    }
                }
            }
        }
    }

    @Override
    public void monthlyRecurrentRegularGIROTransfer() {

        Query query = entityManager.createQuery("SELECT r FROM RegularGIRO r WHERE r.paymentFrequency = :paymentFrequency And r.giroType=:giroType And r.regularGIROStatus=:regularGIROStatus");
        query.setParameter("paymentFrequency", "Monthly");
        query.setParameter("giroType", "Regular GIRO");
        query.setParameter("regularGIROStatus", "Active");
        List<RegularGIRO> regularGIROs = query.getResultList();

        if (regularGIROs.isEmpty()) {
            System.out.println("No daily recurrent regular giro");
        } else {

            for (RegularGIRO regularGIRO : regularGIROs) {

                String bankAccountNum = regularGIRO.getBankAccountNum();
                String payeeAccountNum = regularGIRO.getPayeeAccountNum();

                BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
                OtherBankAccount otherBankAccount = retrieveBankAccountByNum(payeeAccountNum);

                String paymentAmt = regularGIRO.getPaymentAmt();
                String payeeBankName = regularGIRO.getPayeeBankName();

                if (paymentAmt != null) {

                    Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(currentAvailableBalance.toString());

                    otherBankPayeeSessionBeanLocal.updateLastTransactionDate(payeeAccountNum);

                    Calendar cal = Calendar.getInstance();
                    String currentTime = cal.getTime().toString();
                    String transactionCode = "GIRO";
                    String transactionRef = otherBankAccount.getOtherBankAccountType() + otherBankAccount.getOtherBankAccountNum();
                    String accountDebit = paymentAmt;

                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(currentTime, transactionCode,
                            transactionRef, accountDebit, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    if (payeeBankName.equals("DBS")) {
                        sachRegularGIROTransferMTD(bankAccountNum, payeeAccountNum, Double.valueOf(paymentAmt));
                    }
                }
            }
        }
    }

    @Override
    public void weeklyRecurrentRegularGIROTransfer() {

        Query query = entityManager.createQuery("SELECT r FROM RegularGIRO r WHERE r.paymentFrequency = :paymentFrequency And r.giroType=:giroType And r.regularGIROStatus=:regularGIROStatus");
        query.setParameter("paymentFrequency", "Weekly");
        query.setParameter("giroType", "Regular GIRO");
        query.setParameter("regularGIROStatus", "Active");
        List<RegularGIRO> regularGIROs = query.getResultList();

        if (regularGIROs.isEmpty()) {
            System.out.println("No daily recurrent regular giro");
        } else {

            for (RegularGIRO regularGIRO : regularGIROs) {

                String bankAccountNum = regularGIRO.getBankAccountNum();
                String payeeAccountNum = regularGIRO.getPayeeAccountNum();

                BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
                OtherBankAccount otherBankAccount = retrieveBankAccountByNum(payeeAccountNum);

                String paymentAmt = regularGIRO.getPaymentAmt();
                String payeeBankName = regularGIRO.getPayeeBankName();

                if (paymentAmt != null) {

                    Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(currentAvailableBalance.toString());

                    otherBankPayeeSessionBeanLocal.updateLastTransactionDate(payeeAccountNum);

                    Calendar cal = Calendar.getInstance();
                    String currentTime = cal.getTime().toString();
                    String transactionCode = "GIRO";
                    String transactionRef = otherBankAccount.getOtherBankAccountType() + otherBankAccount.getOtherBankAccountNum();
                    String accountDebit = paymentAmt;

                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(currentTime, transactionCode,
                            transactionRef, accountDebit, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    if (payeeBankName.equals("DBS")) {
                        sachRegularGIROTransferMTD(bankAccountNum, payeeAccountNum, Double.valueOf(paymentAmt));
                    }
                }
            }
        }
    }

    @Override
    public NonStandingGIRO retrieveNonStandingByBillRef(String billingReference) {
        NonStandingGIRO nonStandingGIRO = new NonStandingGIRO();

        try {
            Query query = entityManager.createQuery("Select n From NonStandingGIRO n Where n.billReference=:billReference And n.giroType=:giroType");
            query.setParameter("billReference", billingReference);
            query.setParameter("giroType", "Non Standing");

            if (query.getResultList().isEmpty()) {
                return new NonStandingGIRO();
            } else {
                nonStandingGIRO = (NonStandingGIRO) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new NonStandingGIRO();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return nonStandingGIRO;
    }

    @Override
    public void updateNonStandingPaymentFrequency(Long giroId, String paymentFrequency) {
        NonStandingGIRO nonStandingGiro = retrieveNonStandingGIROById(giroId);
        nonStandingGiro.setPaymentFrequency(paymentFrequency);
    }

    @Override
    public void updateButtonRender(Long giroId) {
        NonStandingGIRO nonStandingGiro = retrieveNonStandingGIROById(giroId);
        nonStandingGiro.setButtonRender(true);
    }

    private void sachNonStandingGIROTransferMTD(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
        port.sachNonStandingGIROTransferMTD(fromBankAccountNum, toBankAccountNum, transferAmt);
    }

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBank.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }

    private void sachRegularGIROTransferMTD(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
//        port.sachRegularGIROTransferMTD(fromBankAccountNum, toBankAccountNum, transferAmt);
    }

    private void passNonStandingGIROToSACH(java.lang.String customerName, java.lang.String customerMobile, java.lang.String billReference, java.lang.String billingOrganizationName, java.lang.String creditBank, java.lang.String creditBankAccountNum, java.lang.String debitBank, java.lang.String debitBankAccountNum, java.lang.String paymemtLimit, java.lang.String billStatus, java.lang.String paymentFrequency) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
        port.passNonStandingGIROToSACH(customerName, customerMobile, billReference, billingOrganizationName, creditBank, creditBankAccountNum, debitBank, debitBankAccountNum, paymemtLimit, billStatus, paymentFrequency);
    }
}
