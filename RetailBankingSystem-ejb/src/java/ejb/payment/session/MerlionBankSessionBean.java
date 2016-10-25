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

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBank.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
