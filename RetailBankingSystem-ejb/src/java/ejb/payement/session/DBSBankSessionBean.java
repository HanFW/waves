package ejb.payement.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.DBSBankAccount;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class DBSBankSessionBean implements DBSBankSessionBeanLocal {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    
    @EJB
    private DBSTransactionSessionBeanLocal dBSTransactionSessionBeanLocal;
    
    @EJB
    private DBSBankAccountSessionBeanLocal dBSBankAccountSessionBeanLocal;

    @Override
    public void actualTransfer(String fromAccountNum, String toAccountNum, Double transferAmt) {
        
        DBSBankAccount dbsBankAccount = dBSBankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccountNum);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccountNum);
        Double balance = Double.valueOf(dbsBankAccount.getDbsBankAccountBalance()) + transferAmt;
        
        Calendar cal = Calendar.getInstance();
        String dbsTransactionCode = "ICT";
        String dbsTransactionRef = bankAccount.getBankAccountType()+"-"+bankAccount.getBankAccountNum();
        String dbsAccountDebit = " ";
        String dbsAccountCredit = transferAmt.toString();
        
        Long dbsTransactionId = dBSTransactionSessionBeanLocal.addNewDBSTransaction(cal.getTime().toString(),
                dbsTransactionCode,dbsTransactionRef,dbsAccountDebit,dbsAccountCredit,dbsBankAccount.getDbsBankAccountId());
        
        dbsBankAccount.setDbsBankAccountBalance(balance.toString());
    }
}
