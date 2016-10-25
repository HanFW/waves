/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoanApplicationSessionBeanLocal {
    public void submitLoanApplication(Long customerBasicId, Long customerAdvancedId);
    public void addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment);
}
