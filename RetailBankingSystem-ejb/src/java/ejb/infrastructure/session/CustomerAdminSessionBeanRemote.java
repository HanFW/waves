/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.customer.entity.CustomerBasic;
import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface CustomerAdminSessionBeanRemote {
    public String createOnlineBankingAccount(Long customerId, String email);

    public String login(String customerAccount, String password);
    
    public CustomerBasic getCustomerByOnlineBankingAccount(String customerAccount);
    
    public String updateOnlineBankingAccount(String accountNum, String password, Long customerId);
    
    public void updateOnlineBankingPIN(String password, Long customerId);
    
    public CustomerBasic getCustomerByIdentificationNum(String identificationNum);
            
    public Boolean resetPassword(String identificationNum);
    
    public ArrayList<String> checkExistingService(Long customerId);
    
    public void deleteOnlineBankingAccount(Long customerId);
    
    public void recreateOnlineBankingAccount(Long customerId);
    
    public String lockCustomerOnlineBankingAccount(Long customerId);
    
    public String unlockCustomerOnlineBankingAccount(Long customerId);
}
