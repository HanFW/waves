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
    public String createDebitCard(String bankAccountNum,String cardHolderName, String applicationDate,String cardTypeName);
    
    public String checkDebitCardTypeForDepositAccount(String bankAccountNum, String cardTypeName);
    
    public String debitCardNumValiadation(String debitCardNum, String cardHolderName, String debitCardSecurityCode);
    
    public List<String> getAllActivatedDebitCards(CustomerBasic customer);
    
    public List<String> getAllNonActivatedDebitCards(CustomerBasic customer);
    
    public List<String> getAllDebitCards(CustomerBasic customer);
    
}
