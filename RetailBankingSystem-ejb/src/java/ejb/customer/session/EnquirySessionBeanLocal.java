/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import javax.ejb.Local;
import ejb.customer.entity.EnquiryCase;
import java.util.List;

/**
 *
 * @author aaa
 */
@Local
public interface EnquirySessionBeanLocal {
    
    public List <EnquiryCase> getCustomerEnquiry(String onlineBankingAccountNum);
    
    public List <EnquiryCase> getAllEnquiry();
    
    public String addNewCase(String onlineBankingAccountNum, String type, String detail);
    
    public String updateStatus(Long caseId, String caseStatus);
    
    public String addFollowUp(Long caseId, String caseFollowUp);
   
    
    
}
