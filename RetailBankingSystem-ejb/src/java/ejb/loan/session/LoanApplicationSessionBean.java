/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoanApplicationSessionBean implements LoanApplicationSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void submitLoanApplication(Long customerBasicId, Long customerAdvancedId, ArrayList<CustomerDebt> debts, 
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType){
        System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, customerBasicId);
        
        //set debts to customerBasic (1-M uni)
        cb.setCustomerDebt(debts);
        
        //set on both side (1-1 bi)
        cp.setCustomerBasic(cb);
        cb.setCustomerProperty(cp);
        
        //set on both side (1-1 bi)
        CustomerAdvanced ca = em.find(CustomerAdvanced.class, customerAdvancedId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);
        
        //set on both side
        if(loanType.equals("purchase")){
            mortgage.setCustomerBasic(cb);
            cb.addLoanApplication(mortgage);
        }else{
            refinancing.setCustomerBasic(cb);
            cb.addLoanApplication(refinancing);
        }
        em.flush();
    }
    
    @Override
    public CustomerDebt addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment){
        System.out.println("****** loan/LoanApplicationSessionBean: addNewCustomerDebt() ******");
        CustomerDebt cb = new CustomerDebt();
        
        cb.setFacilityType(facilityType);
        cb.setFanancialInstitution(financialInstitution);
        cb.setTotalAmount(totalAmount);
        cb.setMonthlyInstalment(monthlyInstalment);
        
        return cb;
    }
    
    @Override
    public List<LoanApplication> getAllLoanApplications(){
        Query query = em.createQuery("SELECT la FROM LoanApplication la WHERE la.applicationStatus = :applicationStatus1 OR la.applicationStatus = :applicationStatus2");
        query.setParameter("applicationStatus1", "pending");
        query.setParameter("applicationStatus2", "in progress");
        return query.getResultList();
    }
}
