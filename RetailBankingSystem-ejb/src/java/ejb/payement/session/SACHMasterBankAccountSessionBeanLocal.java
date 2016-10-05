package ejb.payement.session;

import ejb.payment.entity.SACHMasterBankAccount;
import javax.ejb.Local;

@Local
public interface SACHMasterBankAccountSessionBeanLocal {
    public SACHMasterBankAccount retrieveSACHMasterBankAccountByBankName(String bankName);
    public SACHMasterBankAccount retrieveSACHMasterBankAccountByAccNum(String masterBankAccountNum);
    public SACHMasterBankAccount retrieveSACHMasterBankAccountById(Long masterBankAccountId);
}
