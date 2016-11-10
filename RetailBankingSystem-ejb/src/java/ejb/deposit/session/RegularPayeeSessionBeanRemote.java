/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Payee;
import ejb.deposit.entity.RegularPayee;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface RegularPayeeSessionBeanRemote {
    
    public Long addNewRegularPayee(String payeeName, String payeeAccountNum, String payeeAccountType,
            String lastTransactionDate, String payeeType, Long customerBasicId);

    public List<RegularPayee> retrieveRegularPayeeByCusId(Long customerBasicId);
    
    public Payee retrieveRegularPayeeByName(String payeeName);
    
}
