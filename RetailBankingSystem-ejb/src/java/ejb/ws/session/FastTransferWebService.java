package ejb.ws.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;

@WebService(serviceName = "FastTransferWebService")
@Stateless()

public class FastTransferWebService {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @WebMethod(operationName = "fastTransfer")
    public void fastTransfer(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum, @WebParam(name = "toBankAccountNum") String toBankAccountNum, @WebParam(name = "transferAmt") Double transferAmt) {

        OtherBankAccount otherBankAccount = retrieveBankAccountByNum(fromBankAccountNum);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccountNum);
        Double totalBalance = Double.valueOf(bankAccount.getTotalBankAccountBalance()) + transferAmt;
        Double availableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) + transferAmt;

        Calendar cal = Calendar.getInstance();
        String transactionCode = "ICT";
        String transactionRef = "Transfer from " + otherBankAccount.getOtherBankAccountType() + "-" + otherBankAccount.getOtherBankAccountNum();
        String accountDebit = " ";
        String accountCredit = transferAmt.toString();
        Long transactionDateMilis = cal.getTimeInMillis();

        Long transactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(),
                transactionCode, transactionRef, accountDebit, accountCredit,
                transactionDateMilis, bankAccount.getBankAccountId());

        bankAccount.setAvailableBankAccountBalance(availableBalance.toString());
        bankAccount.setTotalBankAccountBalance(totalBalance.toString());
    }

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
