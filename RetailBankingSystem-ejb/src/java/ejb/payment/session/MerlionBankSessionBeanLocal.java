package ejb.payment.session;

import javax.ejb.Local;

@Local
public interface MerlionBankSessionBeanLocal {

    public void approveAccount(String customerIdentificationNum);

    public void debitBankAccount(String debitBankAccountNum, Double debitAmt);

    public void sendEmailToRejectCustomer(String customerIdentificationNum);

    public void sendSWIFTMessage(String swiftMessage, String transactionDate, String swiftCodeA,
            String swiftCodeB, String organizationA, String organizationB, String countryA,
            String countryB, String paymentAmt, String transferedBankAccountNum);
}
