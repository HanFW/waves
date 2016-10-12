/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface DebitCardSessionBeanLocal {
    public String createDebitCard(String bankAccountNum,String cardHolderName, String applicationDate,String cardTypeName);
}
