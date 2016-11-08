package ejb.payment.session;

import ejb.payment.entity.ReceivedCheque;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ReceivedChequeSessionBeanLocal {

    public Long addNewReceivedCheque(String transactionDate, String transactionAmt,
            String receivedBankAccountNum, String receivedCustomerName, String receivedCustomerMobile,
            String receivedChequeStatus, String chequeNum, String chequeType,
            String otherBankAccountNum, Long customerBasicId);

    public List<ReceivedCheque> getAllNewReceivedCheque();

    public ReceivedCheque retrieveReceivedChequeById(Long chequeId);

    public List<ReceivedCheque> retrieveReceivedChequeByCusIC(String customerIdentificationNum);

    public void updateReceivedChequeStatus(String chequeNum);

    public ReceivedCheque retrieveReceivedChequeByNum(String chequeNum);
}
