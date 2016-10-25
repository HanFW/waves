package ejb.ws.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payment.entity.OnHoldRecord;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.Oneway;
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

        Query query = entityManager.createQuery("SELECT o FROM OnHoldRecord o WHERE o.onHoldStatus = :onHoldStatus");
        query.setParameter("onHoldStatus", "New");
        List<OnHoldRecord> onHoldRecords = query.getResultList();

        for (OnHoldRecord onHoldRecord : onHoldRecords) {

            String bankAccountNum = onHoldRecord.getBankAccountNum();
            String paymentAmt = onHoldRecord.getPaymentAmt();
            String debitOrCredit = onHoldRecord.getDebitOrCredit();
            String debitOrCreditBankAccountNum = onHoldRecord.getDebitOrCreditBankAccountNum();
            String debitOrCreditBankName = onHoldRecord.getDebitOrCreditBankName();

            BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
            String currenttTotalBalance = bankAccount.getTotalBankAccountBalance();

            if (debitOrCredit.equals("Debit") && debitOrCreditBankName.equals("DBS")) {

                Double totalBalance = Double.valueOf(currenttTotalBalance) - Double.valueOf(paymentAmt);

                bankAccount.setTotalBankAccountBalance(totalBalance.toString());

                onHoldRecord.setOnHoldStatus("Done");

                OtherBankAccount dbsBankAccount = retrieveBankAccountByNum_other(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "BILL";
                String transactionRef = "Pay bills to NTUC";

                Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                        transactionCode, transactionRef, paymentAmt, " ",
                        cal.getTimeInMillis(), bankAccount.getBankAccountId());
            }
        }
    }

    private OtherBankAccount retrieveBankAccountByNum_other(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBank.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }

    @WebMethod(operationName = "addNewRecord")
    public Long addNewRecord(@WebParam(name = "bankName") String bankName,
            @WebParam(name = "bankAccountNum") String bankAccountNum,
            @WebParam(name = "debitOrCredit") String debitOrCredit,
            @WebParam(name = "paymentAmt") String paymentAmt,
            @WebParam(name = "onHoldStatus") String onHoldStatus,
            @WebParam(name = "debitOrCreditBankName") String debitOrCreditBankName,
            @WebParam(name = "debitOrCreditBankAccountNum") String debitOrCreditBankAccountNum) {

        OnHoldRecord onHoldRecord = new OnHoldRecord();

        onHoldRecord.setBankAccountNum(bankAccountNum);
        onHoldRecord.setBankName(bankName);
        onHoldRecord.setDebitOrCredit(debitOrCredit);
        onHoldRecord.setPaymentAmt(paymentAmt);
        onHoldRecord.setOnHoldStatus(onHoldStatus);
        onHoldRecord.setDebitOrCreditBankName(debitOrCreditBankName);
        onHoldRecord.setDebitOrCreditBankAccountNum(debitOrCreditBankAccountNum);

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

}
