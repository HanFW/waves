package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.ws.WebServiceRef;
import ws.client.chips.CHIPSWebService_Service;
import ws.client.swift.SWIFTWebService_Service;

@Stateless
public class MerlionBankSessionBean implements MerlionBankSessionBeanLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/CHIPSWebService/CHIPSWebService.wsdl")
    private CHIPSWebService_Service service_chips;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/SWIFTWebService/SWIFTWebService.wsdl")
    private SWIFTWebService_Service service_swift;

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

        String onlineBankingAccount = adminSessionBeanLocal.createOnlineBankingAccount(customerBasic.getCustomerBasicId(), "openAccount");
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
    public void sendSWIFTMessage(String swiftMessage, String transactionDate, String swiftCodeA,
            String swiftCodeB, String organizationA, String organizationB, String countryA,
            String countryB, String paymentAmt, String transferedBankAccountNum) {

        DecimalFormat df = new DecimalFormat("#.00");

        Double paymentAmtDouble = Double.valueOf(paymentAmt);
        Long newSwiftId = addNewSWIFT(swiftMessage, transactionDate, swiftCodeA, swiftCodeB,
                organizationA, organizationB, countryA, countryB, df.format(paymentAmtDouble),
                transferedBankAccountNum);

        clearSWIFTTransferMTK(newSwiftId);
    }

    private void clearSWIFTTransferMTK(java.lang.Long swiftId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.chips.CHIPSWebService port = service_chips.getCHIPSWebServicePort();
        port.clearSWIFTTransferMTK(swiftId);
    }

    private Long addNewSWIFT(java.lang.String swiftMessage, java.lang.String transactionDate, java.lang.String swiftCodeA, java.lang.String swiftCodeB, java.lang.String organizationA, java.lang.String organizationB, java.lang.String countryA, java.lang.String countryB, java.lang.String paymentAmt, java.lang.String transferedBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.swift.SWIFTWebService port = service_swift.getSWIFTWebServicePort();
        return port.addNewSWIFT(swiftMessage, transactionDate, swiftCodeA, swiftCodeB, organizationA, organizationB, countryA, countryB, paymentAmt, transferedBankAccountNum);
    }
}
