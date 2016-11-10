/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Payee;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface PayeeSessionBeanRemote {

    public String deletePayee(String payeeAccountNum);

    public Payee retrievePayeeById(Long payeeId);

    public List<Payee> retrievePayeeByCusId(Long customerBasicId);

    public Payee retrievePayeeByNum(String payeeAccountNum);

    public void updateLastTransactionDate(String bankAccountNum);
    
}
