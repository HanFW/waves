package ejb.payement.session;

import ejb.payment.entity.MEPSMasterBankAccount;
import javax.ejb.Local;

@Local
public interface MEPSMasterBankAccountSessionBeanLocal {
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountByBankName(String bankName);
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountByAccNum(String masterBankAccountNum);
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountById(Long masterBankAccountId);
    public void maintainDailyBalance();
}
