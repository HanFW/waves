package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import ejb.payment.entity.OnHoldRecord;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;

@Stateless
public class MerlionBankSessionBean implements MerlionBankSessionBeanLocal {

    @EJB
    private ReceivedChequeSessionBeanLocal receivedChequeSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service_otherBank;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private CustomerAdminSessionBeanLocal adminSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @Override
    public void approveAccount(String customerIdentificationNum) {

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerIdentificationNum).get(0);

        customerBasic.setNewCustomer("No");

        if (bankAccount.getBankAccountType().equals("Monthly Savings Account")) {
            bankAccount.setBankAccountStatus("Active");
            bankAccount.setBankAccountMinSaving("Insufficient");
        } else {
            bankAccount.setBankAccountStatus("Inactive");
        }

        String onlineBankingAccount = adminSessionBeanLocal.createOnlineBankingAccount(customerBasic.getCustomerBasicId());
    }

    @Override
    public void debitBankAccount(String debitBankAccountNum, Double debitAmt) {

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(debitBankAccountNum);

        Double currentAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance());
        Double currentTotalBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance());
        Double totalAvailableBalance = currentAvailableBalance - debitAmt;

        bankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
        bankAccount.setTotalBankAccountBalance(currentTotalBalance.toString());
    }

    @Override
    public void sendEmailToRejectCustomer(String customerIdentificationNum) {

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        Map emailActions = new HashMap();
        customerEmailSessionBeanLocal.sendEmail(customerBasic, "rejectOpenAccount", emailActions);
    }

    @Override
    public void settleEachBankAccount() {

        Query query = entityManager.createQuery("SELECT o FROM OnHoldRecord o WHERE o.onHoldStatus = :onHoldStatus");
        query.setParameter("onHoldStatus", "New");
        List<OnHoldRecord> onHoldRecords = query.getResultList();

        for (OnHoldRecord onHoldRecord : onHoldRecords) {

            String bankAccountNum = onHoldRecord.getBankAccountNum();
            String bankName = onHoldRecord.getBankName();
            String paymentAmt = onHoldRecord.getPaymentAmt();
            String debitOrCredit = onHoldRecord.getDebitOrCredit();
            String debitOrCreditBankAccountNum = onHoldRecord.getDebitOrCreditBankAccountNum();
            String debitOrCreditBankName = onHoldRecord.getDebitOrCreditBankName();

            BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
            String currentAvailableBalance = bankAccount.getAvailableBankAccountBalance();
            String currenttTotalBalance = bankAccount.getTotalBankAccountBalance();

            if (debitOrCredit.equals("Debit") && debitOrCreditBankName.equals("DBS")) {

                Double totalBalance = Double.valueOf(currenttTotalBalance) - Double.valueOf(paymentAmt);

                bankAccount.setTotalBankAccountBalance(totalBalance.toString());

                onHoldRecord.setOnHoldStatus("Done");

                OtherBankAccount dbsBankAccount = retrieveBankAccountByNum(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "BILL";
                String transactionRef = dbsBankAccount.getOtherBankAccountType() + dbsBankAccount.getOtherBankAccountNum();

                Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                        transactionCode, transactionRef, " ", paymentAmt,
                        cal.getTimeInMillis(), bankAccount.getBankAccountId());

            } else if (debitOrCredit.equals("Credit") && debitOrCreditBankName.equals("DBS")) {

                if (onHoldRecord.getPaymentMethod().equals("Cheque")) {
                    Long chequeId = onHoldRecord.getChequeId();
                    receivedChequeSessionBeanLocal.updateReceivedChequeStatus(chequeId);
                }
                
                Double totalBalance = Double.valueOf(currenttTotalBalance) + Double.valueOf(paymentAmt);

                bankAccount.setTotalBankAccountBalance(totalBalance.toString());

                onHoldRecord.setOnHoldStatus("Done");

                OtherBankAccount dbsBankAccount = retrieveBankAccountByNum(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "BILL";
                String transactionRef = dbsBankAccount.getOtherBankAccountType() + dbsBankAccount.getOtherBankAccountNum();

                Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                        transactionCode, transactionRef, paymentAmt, " ",
                        cal.getTimeInMillis(), bankAccount.getBankAccountId());
            }
        }
    }

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBank.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
