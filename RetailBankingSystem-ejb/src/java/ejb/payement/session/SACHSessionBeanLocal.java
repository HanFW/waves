package ejb.payement.session;

import ejb.payment.entity.SACH;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SACHSessionBeanLocal {
    public void SACHTransfer(String fromBankAccount, String toBankAccount, Double transferAmt);
    public SACH retrieveSACHById(Long sachId);
   public Long addNewSACH(String dbsAccountBalance, String merlionAccountBalance, Double dbsTotalCredit,
            Double merlionTotalCredit, String updateDate, String bankNames);
    public List<SACH> getAllSACH(String bankNames);
}
