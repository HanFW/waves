/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.CustomerDebt;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoanApplicationSessionBean implements LoanApplicationSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void submitLoanApplication(Long customerBasicId, Long customerAdvancedId){
        System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, customerBasicId);
        CustomerAdvanced ca = em.find(CustomerAdvanced.class, customerAdvancedId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);
        em.flush();
    }
    
    @Override
    public void addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment){
        CustomerDebt cb = new CustomerDebt();
        
        cb.setFacilityType(facilityType);
        cb.setFanancialInstitution(financialInstitution);
        cb.setTotalAmount(totalAmount);
        cb.setMonthlyInstalment(monthlyInstalment);
    }
}
