package ejb.ws.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payment.entity.Cheque;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.entity.OnHoldRecord;
import ejb.payment.entity.ReceivedCheque;
import ejb.payment.entity.RegularGIRO;
import ejb.payment.entity.StandingGIRO;
import ejb.payment.session.IssuedChequeSessionBeanLocal;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
import ejb.payment.session.ReceivedChequeSessionBeanLocal;
import ejb.payment.session.RegularGIROSessionBeanLocal;
import ejb.payment.session.StandingGIROSessionBeanLocal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;

@WebService(serviceName = "MerlionBankWebService")
@Stateless()

public class MerlionBankWebService {

    @EJB
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;

    @EJB
    private StandingGIROSessionBeanLocal standingGIROSessionBeanLocal;

    @EJB
    private RegularGIROSessionBeanLocal regularGIROSessionBeanLocal;

    @EJB
    private IssuedChequeSessionBeanLocal issuedChequeSessionBeanLocal;

    @EJB
    private ReceivedChequeSessionBeanLocal receivedChequeSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service_otherBank;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;

    @WebMethod(operationName = "retrieveBankAccountByNum")
    public BankAccount retrieveBankAccountByNum(@WebParam(name = "bankAccountNum") String bankAccountNum) {
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
            query.setParameter("bankAccountNum", bankAccountNum);

            if (query.getResultList().isEmpty()) {
                return new BankAccount();
            } else {
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        entityManager.detach(bankAccount);
        bankAccount.setAccTransaction(null);
        bankAccount.setCustomerBasic(null);
        bankAccount.setInterest(null);
        bankAccount.setStatement(null);

        return bankAccount;
    }

    @WebMethod(operationName = "debitBankAccount")
//    @Oneway
    public void debitBankAccount(@WebParam(name = "debitBankAccountNum") String debitBankAccountNum,
            @WebParam(name = "debitAmt") Double debitAmt) {

        BankAccount bankAccount = retrieveBankAccountByNum(debitBankAccountNum);

        Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance());
        Double currentTotalBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance());
        Double totalAvailableBalance = currentAvailableBalance - debitAmt;

        bankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
        bankAccount.setTotalBankAccountBalance(currentTotalBalance.toString());
    }

    @WebMethod(operationName = "settleEachBankAccount")
//    @Oneway
    public void settleEachBankAccount() {

        DecimalFormat df = new DecimalFormat("#.00");

        Query query = entityManager.createQuery("SELECT o FROM OnHoldRecord o WHERE o.onHoldStatus = :onHoldStatus");
        query.setParameter("onHoldStatus", "New");
        List<OnHoldRecord> onHoldRecords = query.getResultList();

        for (OnHoldRecord onHoldRecord : onHoldRecords) {

            String bankAccountNum = onHoldRecord.getBankAccountNum();
            String paymentAmt = onHoldRecord.getPaymentAmt();
            String debitOrCredit = onHoldRecord.getDebitOrCredit();
            String debitOrCreditBankAccountNum = onHoldRecord.getDebitOrCreditBankAccountNum();
            String debitOrCreditBankName = onHoldRecord.getDebitOrCreditBankName();
            String paymentMethod = onHoldRecord.getPaymentMethod();

            BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
            String currenttTotalBalance = bankAccount.getTotalBankAccountBalance();

            if (debitOrCredit.equals("Debit") && debitOrCreditBankName.equals("DBS")) {

                if (onHoldRecord.getPaymentMethod().equals("Cheque")) {
                    String chequeNum = onHoldRecord.getChequeNum();
                    issuedChequeSessionBeanLocal.updateIssuedChequeStatus(chequeNum);
                }

                if (paymentAmt != null) {

                    Double totalBalance = Double.valueOf(currenttTotalBalance) - Double.valueOf(paymentAmt);
                    bankAccount.setTotalBankAccountBalance(df.format(totalBalance));

                    if (onHoldRecord.getPaymentMethod().equals("Cheque")) {
                        String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
                        Double totalAvailableBalance = Double.valueOf(currentAvailableBalance) - Double.valueOf(paymentAmt);
                        bankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
                    }

                    onHoldRecord.setOnHoldStatus("Done");

                    OtherBankAccount dbsBankAccount = retrieveBankAccountByNum_other(debitOrCreditBankAccountNum);
                    Calendar cal = Calendar.getInstance();
                    String transactionDate = cal.getTime().toString();
                    String transactionCode = "";
                    String transactionRef = "";

                    if (paymentMethod.equals("Non Standing GIRO")) {

                        transactionCode = "BILL";
                        transactionRef = "Pay bills to NTUC";
                        Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                                transactionCode, transactionRef, paymentAmt, " ",
                                cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    } else if (paymentMethod.equals("Standing GIRO")) {
                        transactionCode = "BILL";
                        transactionRef = "Pay bills to NTUC";
                    } else if (paymentMethod.equals("Cheque")) {

                        transactionCode = "CHQ";
                        transactionRef = dbsBankAccount.getOtherBankAccountType() + "-" + dbsBankAccount.getOtherBankAccountNum();
                        Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                                transactionCode, transactionRef, paymentAmt, " ",
                                cal.getTimeInMillis(), bankAccount.getBankAccountId());

                    } else if (paymentMethod.equals("Regular GIRO")) {
                        transactionCode = "GIRO";
                        transactionRef = dbsBankAccount.getOtherBankAccountType() + "-" + dbsBankAccount.getOtherBankAccountNum();
                    }

//                    Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
//                            transactionCode, transactionRef, paymentAmt, " ",
//                            cal.getTimeInMillis(), bankAccount.getBankAccountId());
                }

            } else if (debitOrCredit.equals("Credit") && debitOrCreditBankName.equals("DBS")) {

                if (onHoldRecord.getPaymentMethod().equals("Cheque")) {
                    String chequeNum = onHoldRecord.getChequeNum();
                    receivedChequeSessionBeanLocal.updateReceivedChequeStatus(chequeNum);
                }

                Double totalBalance = Double.valueOf(currenttTotalBalance) + Double.valueOf(paymentAmt);
                bankAccount.setTotalBankAccountBalance(df.format(totalBalance));

                if (onHoldRecord.getPaymentMethod().equals("Cheque")) {
                    String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
                    Double totalAvailableBalance = Double.valueOf(currentAvailableBalance) + Double.valueOf(paymentAmt);
                    bankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
                }

                onHoldRecord.setOnHoldStatus("Done");

                OtherBankAccount dbsBankAccount = retrieveBankAccountByNum_other(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "";

                if (paymentMethod.equals("Non Standing GIRO") || paymentMethod.equals("Standing GIRO")) {
                    transactionCode = "BILL";
                } else if (paymentMethod.equals("Cheque")) {
                    transactionCode = "CHQ";
                } else if (paymentMethod.equals("Regular GIRO")) {
                    transactionCode = "GIRO";
                }

                String transactionRef = dbsBankAccount.getOtherBankAccountType() + "-" + dbsBankAccount.getOtherBankAccountNum();

                Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                        transactionCode, transactionRef, paymentAmt, " ",
                        cal.getTimeInMillis(), bankAccount.getBankAccountId());

            } else if (debitOrCredit.equals("Debit") && debitOrCreditBankName.equals("Bank of Korea")) {

                Double totalBalance = Double.valueOf(currenttTotalBalance) - Double.valueOf(paymentAmt);
                bankAccount.setTotalBankAccountBalance(df.format(totalBalance));

                onHoldRecord.setOnHoldStatus("Done");

                OtherBankAccount koreaBankAccount = retrieveBankAccountByNum_other(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "SWIFT";
                String transactionRef = koreaBankAccount.getOtherBankAccountType() + "-" + koreaBankAccount.getOtherBankAccountNum();

//                Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
//                        transactionCode, transactionRef, paymentAmt, " ",
//                        cal.getTimeInMillis(), bankAccount.getBankAccountId());
            }
        }
    }

    @WebMethod(operationName = "addNewRecord")
    public Long addNewRecord(@WebParam(name = "bankName") String bankName,
            @WebParam(name = "bankAccountNum") String bankAccountNum,
            @WebParam(name = "debitOrCredit") String debitOrCredit,
            @WebParam(name = "paymentAmt") String paymentAmt,
            @WebParam(name = "onHoldStatus") String onHoldStatus,
            @WebParam(name = "debitOrCreditBankName") String debitOrCreditBankName,
            @WebParam(name = "debitOrCreditBankAccountNum") String debitOrCreditBankAccountNum,
            @WebParam(name = "paymentMethod") String paymentMethod) {

        OnHoldRecord onHoldRecord = new OnHoldRecord();

        onHoldRecord.setBankAccountNum(bankAccountNum);
        onHoldRecord.setBankName(bankName);
        onHoldRecord.setDebitOrCredit(debitOrCredit);
        onHoldRecord.setPaymentAmt(paymentAmt);
        onHoldRecord.setOnHoldStatus(onHoldStatus);
        onHoldRecord.setDebitOrCreditBankName(debitOrCreditBankName);
        onHoldRecord.setDebitOrCreditBankAccountNum(debitOrCreditBankAccountNum);
        onHoldRecord.setPaymentMethod(paymentMethod);

        entityManager.persist(onHoldRecord);
        entityManager.flush();

        return onHoldRecord.getOnHoldRecordId();
    }

    @WebMethod(operationName = "updateAvailableBalance")
//    @Oneway
    public void updateAvailableBalance(@WebParam(name = "bankAccountNum") String bankAccountNum,
            @WebParam(name = "paymentAmt") Double paymentAmt) {
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

        String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
        Double totalAvailableBalance = Double.valueOf(currentAvailableBalance) - paymentAmt;
        bankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
    }

    @WebMethod(operationName = "retrieveChequeById")
    public Cheque retrieveChequeById(@WebParam(name = "chequeId") Long chequeId) {

        Cheque cheque = new Cheque();

        try {
            Query query = entityManager.createQuery("Select c From Cheque c Where c.chequeId=:chequeId");
            query.setParameter("chequeId", chequeId);

            if (query.getResultList().isEmpty()) {
                return new Cheque();
            } else {
                cheque = (Cheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Cheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        entityManager.detach(cheque);
        cheque.setCustomerBasic(null);

        return cheque;
    }

    @WebMethod(operationName = "retrieveReceivedChequeById")
    public ReceivedCheque retrieveReceivedChequeById(@WebParam(name = "chequeId") Long chequeId) {
        ReceivedCheque receivedCheque = new ReceivedCheque();

        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.chequeId=:chequeId");
            query.setParameter("chequeId", chequeId);

            if (query.getResultList().isEmpty()) {
                return new ReceivedCheque();
            } else {
                receivedCheque = (ReceivedCheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ReceivedCheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        entityManager.detach(receivedCheque);
        receivedCheque.setCustomerBasic(null);

        return receivedCheque;
    }

    @WebMethod(operationName = "retrieveOnHoldRecordById")
    public OnHoldRecord retrieveOnHoldRecordById(@WebParam(name = "onHoldRecordId") Long onHoldRecordId) {
        OnHoldRecord onHoldRecord = new OnHoldRecord();

        try {
            Query query = entityManager.createQuery("Select o From OnHoldRecord o Where o.onHoldRecordId=:onHoldRecordId");
            query.setParameter("onHoldRecordId", onHoldRecordId);

            if (query.getResultList().isEmpty()) {
                return new OnHoldRecord();
            } else {
                onHoldRecord = (OnHoldRecord) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OnHoldRecord();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return onHoldRecord;
    }

    @WebMethod(operationName = "updateOnHoldChequeNum")
//    @Oneway
    public void updateOnHoldChequeNum(@WebParam(name = "onHoldRecordId") Long onHoldRecordId,
            @WebParam(name = "chequeNum") String chequeNum) {
        OnHoldRecord onHoldRecord = retrieveOnHoldRecordById(onHoldRecordId);
        onHoldRecord.setChequeNum(chequeNum);
    }

    @WebMethod(operationName = "updateReceivedChequeStatus")
//    @Oneway
    public void updateReceivedChequeStatus(@WebParam(name = "chequeId") Long chequeId) {
        ReceivedCheque receivedCheque = retrieveReceivedChequeById(chequeId);
        receivedCheque.setReceivedChequeStatus("Approved");
    }

    @WebMethod(operationName = "receiveChequeInformationFromSACH")
//    @Oneway
    public void receiveChequeInformationFromSACH(@WebParam(name = "chequeNum") String chequeNum,
            @WebParam(name = "transactionAmt") String transactionAmt,
            @WebParam(name = "bankAccountNum") String bankAccountNum) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByAccNum(bankAccountNum);
        Calendar cal = Calendar.getInstance();

        Long newIssuedChequeId = issuedChequeSessionBeanLocal.addNewIssuedCheque(cal.getTime().toString(), transactionAmt,
                chequeNum, "Pending", "Issued", customerBasic.getCustomerBasicId());
    }

    @WebMethod(operationName = "retrieveReceivedChequeByNum")
    public ReceivedCheque retrieveReceivedChequeByNum(@WebParam(name = "chequeNum") String chequeNum) {
        ReceivedCheque receivedCheque = new ReceivedCheque();

        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.chequeNum=:chequeNum And r.chequeType=:chequeType");
            query.setParameter("chequeNum", chequeNum);
            query.setParameter("chequeType", "Received");

            if (query.getResultList().isEmpty()) {
                return new ReceivedCheque();
            } else {
                receivedCheque = (ReceivedCheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ReceivedCheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        entityManager.detach(receivedCheque);
        receivedCheque.setCustomerBasic(null);

        return receivedCheque;
    }

    @WebMethod(operationName = "rejectRegularGIROTransaction")
//    @Oneway
    public void rejectRegularGIROTransaction(@WebParam(name = "bankAccountNum") String bankAccountNum,
            @WebParam(name = "transferAmt") Double transferAmt,
            @WebParam(name = "toBankAccountNum") String toBankAccountNum) {

        List<RegularGIRO> regularGIRO = regularGIROSessionBeanLocal.retrieveRegularGIROByNum(toBankAccountNum);
        for (int i = 0; i < regularGIRO.size(); i++) {
            regularGIRO.get(i).setRegularGIROStatus("Rejected");
        }

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
        Double totalAvailableBalance = Double.valueOf(currentAvailableBalance) + transferAmt;
        bankAccountSessionBeanLocal.updateBankAccountAvailableBalance(bankAccountNum, totalAvailableBalance.toString());

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();
        String transactionCode = "GIRO";
        String transactionRef = "Refund due to invalid bank account number";

        Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                transactionCode, transactionRef, " ", transferAmt.toString(),
                cal.getTimeInMillis(), bankAccount.getBankAccountId());
    }

    @WebMethod(operationName = "rejectStandingGIROTransaction")
//    @Oneway
    public void rejectStandingGIROTransaction(@WebParam(name = "billReference") String billReference,
            @WebParam(name = "creditBankAccountNum") String creditBankAccountNum,
            @WebParam(name = "debitBankAccountNum") String debitBankAccountNum) {

        StandingGIRO standingGIRO = standingGIROSessionBeanLocal.retrieveStandingGIROByBillRef(billReference);
        standingGIRO.setStandingGiroStatus("Rejected");
    }

    @WebMethod(operationName = "rejectNonStandingGIROTransaction")
//    @Oneway
    public void rejectNonStandingGIROTransaction(@WebParam(name = "billReference") String billReference,
            @WebParam(name = "creditBankAccountNum") String creditBankAccountNum,
            @WebParam(name = "debitBankAccountNum") String debitBankAccountNum) {

        NonStandingGIRO nonStandingGIRO = nonStandingGIROSessionBeanLocal.retrieveNonStandingByBillRef(billReference);
        nonStandingGIRO.setNonStandingStatus("Rejected");
        nonStandingGIRO.setButtonRender(true);
    }

    @WebMethod(operationName = "approveNonStandingGIROTransaction")
//    @Oneway
    public void approveNonStandingGIROTransaction(@WebParam(name = "billReference") String billReference) {
        NonStandingGIRO nonStandingGIRO = nonStandingGIROSessionBeanLocal.retrieveNonStandingByBillRef(billReference);
        nonStandingGIRO.setNonStandingStatus("Approved");
        nonStandingGIRO.setButtonRender(false);
    }

    private OtherBankAccount retrieveBankAccountByNum_other(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBank.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
