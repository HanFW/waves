package ejb.payement.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.OtherBankAccount;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class OtherBankSessionBean implements OtherBankSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @Override
    public void actualTransfer(String fromAccountNum, String toAccountNum, Double transferAmt) {

        OtherBankAccount otherBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccountNum);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccountNum);
        Double balance = Double.valueOf(otherBankAccount.getOtherBankAccountBalance()) + transferAmt;

        Calendar cal = Calendar.getInstance();
        String otherTransactionCode = "ICT";
        String otherTransactionRef = "Transfer from " + bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();
        String otherAccountDebit = " ";
        String otherAccountCredit = transferAmt.toString();

        Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(cal.getTime().toString(),
                otherTransactionCode, otherTransactionRef, otherAccountDebit, otherAccountCredit, otherBankAccount.getOtherBankAccountId());

        otherBankAccount.setOtherBankAccountBalance(balance.toString());
    }
}
