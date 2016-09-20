/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.customer.entity.CustomerBasic;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface CustomerAdminSessionBeanLocal {
    public String createOnlineBankingAccount(Long customerId);

    public String login(String customerAccount, String password);
    
    public CustomerBasic getCustomerByOnlineBankingAccount(String customerAccount);
    
    public String updateOnlineBankingAccount(String accountNum, String password, Long customerId);
    
    public CustomerBasic getCustomerByIdentificationNum(String identificationNum);
            
    public Boolean resetPassword(String identificationNum);
}
