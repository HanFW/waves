/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import ejb.customer.entity.CustomerBasic;
import ejb.wealth.entity.Portfolio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class CreateFinancialGoalSessionBean implements CreateFinancialGoalSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    @Override
    public void createPortfolio(Long id, double startingBalance, double monthlyContribution, int contributeDuration,
            int investmentPeriod, double amountExpected, String wealthPlan,double rate) {
        CustomerBasic customer = em.find(CustomerBasic.class, id);

        Calendar cal = Calendar.getInstance();
        String startingDate = cal.getTime().toString();
        
        Calendar nYearsLater=cal;
        nYearsLater.add(Calendar.YEAR, investmentPeriod);
        String endDate=nYearsLater.getTime().toString();

        Portfolio portfolio = new Portfolio();

        portfolio.setStartingBalance(startingBalance);
        portfolio.setMonthlyContribution(monthlyContribution);
        portfolio.setContributeDuration(contributeDuration);
        portfolio.setInvestmentPeriod(investmentPeriod);
        portfolio.setAmountExpected(amountExpected);
        portfolio.setStatus("created");
        portfolio.setCustomerBasic(customer);
        portfolio.setWealthPlan(wealthPlan);
        portfolio.setStartingDate(startingDate);
        portfolio.setEndDate(endDate);
        portfolio.setRate(rate);
        em.persist(portfolio);

        customer.addPortfolio(portfolio);
        em.flush();
        System.out.println("financial goal has been created!");
    }
    
    @Override
     public List<Portfolio> getAllPortfoliosById(Long id){
        List<Portfolio> portfolios= new ArrayList<> ();
        CustomerBasic customer= em.find(CustomerBasic.class, id);
        
        portfolios=customer.getPortfolios();
        return portfolios;
          
     }
}
