package ejb.payement.session;

import ejb.payment.entity.DBSBankAccount;
import java.util.List;
import javax.ejb.Local;

@Local
public interface DBSBankAccountSessionBeanLocal {
    public List<DBSBankAccount> getAllDBSBankAccount();
    public DBSBankAccount retrieveBankAccountByNum(String dbsBankAccountNum);
    public DBSBankAccount retrieveBankAccountById(Long dbsBankAccountId);
    public void updateBankAccountBalance(String dbsBankAccountNum,String dbsBankAccountBalance);
}
