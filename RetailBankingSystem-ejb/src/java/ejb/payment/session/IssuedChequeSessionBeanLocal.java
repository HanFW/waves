package ejb.payment.session;

import ejb.payment.entity.IssuedCheque;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IssuedChequeSessionBeanLocal {

    public Long addNewIssuedCheque(String transactionDate, String transactionAmt, String chequeNum,
            String issuedChequeStatus, String chequeType, Long customerBasicId);

    public IssuedCheque retrieveIssuedChequeById(Long chequeId);

    public IssuedCheque retrieveIssuedChequeByNum(String chequeNum);

    public List<IssuedCheque> retrieveIssuedChequeByCusIC(String customerIdentificationNum);

    public void updateIssuedChequeStatus(String chequeNum);
}
