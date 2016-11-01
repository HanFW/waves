/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.customer.entity.CustomerBasic;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface DebitCardSessionBeanLocal {
    public void createDebitCard(String bankAccountNum,String cardHolderName, String applicationDate,String cardTypeName);
    
    public void replaceDebitCard(String bankAccountNum, String cardHolderName, String expDate, int remainingMonths, int transactionLimit, String cardTypeName, Long predecessorId);
    
    public void lostDebitCardRecreate(String bankAccountNum, String cardHolderName, String expDate, int remainingMonths, int transactionLimit, String cardTypeName);
    
    public void issueDebitCardForCardReplacement(String bankAccountNum,String cardHolderName, String applicationDate,String cardTypeName, Long predecessorId);
    
    public String checkDebitCardTypeForDepositAccount(String bankAccountNum, String cardTypeName);
    
    public String debitCardNumValiadation(String debitCardNum, String cardHolderName, String debitCardSecurityCode);
    
    public List<String> getAllDepositAccounts(Long customerId);
    
    public List<String> getAllActivatedDebitCards(Long customerId);
    
    public List<String> getAllNonActivatedDebitCards(Long customerId);
    
    public List<String> getAllDebitCards(Long customerId);
    
}
