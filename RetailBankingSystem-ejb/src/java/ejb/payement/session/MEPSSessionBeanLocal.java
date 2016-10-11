package ejb.payement.session;

import ejb.payment.entity.MEPS;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MEPSSessionBeanLocal {

    public Long addNewMEPS(String settlementRef, String settlementDate, String bankNames);

    public void MEPSSettlementMTD(String fromMasterBankAccountNum, String toMasterBankAccountNum, Double transferAmt);
    
    public List<MEPS> getAllMEPS(String bankNames);
    
    public void MEPSSettlementDTM(String fromMasterBankAccountNum, String toMasterBankAccountNum, Double transferAmt);
}
