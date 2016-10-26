package ejb.payment.session;

import ejb.payment.entity.ReceivedCheque;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ReceivedChequeSessionBeanLocal {

    public Long addNewReceivedCheque(String transactionDate, String transactionAmt,
            String receivedBankAccountNum, String receivedCustomerName, String receivedCustomerMobile,
            String receivedChequeStatus, Long customerBasicId);
    public List<ReceivedCheque> getAllNewReceivedCheque();
    public ReceivedCheque retrieveReceivedChequeById(Long chequeId);
    public List<ReceivedCheque> retrieveReceivedChequeByCusId(String customerIdentificationNum);
    public void updateReceivedChequeStatus(Long chequeId);
}

