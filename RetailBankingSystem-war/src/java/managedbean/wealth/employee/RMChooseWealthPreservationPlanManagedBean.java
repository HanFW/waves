/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.wealth.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.wealth.session.CreateFinancialGoalSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "rMChooseWealthPreservationPlanManagedBean")
@RequestScoped
public class RMChooseWealthPreservationPlanManagedBean {

    /**
     * Creates a new instance of RMChooseWealthPreservationPlanManagedBean
     */
    @EJB
    private CreateFinancialGoalSessionBeanLocal createFinancialGoalSessionBeanLocal;
    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private ExternalContext ec;
    private CustomerBasic customer;

    private double startingBalance;
    private double rate;
    private double monthlyContribution;
    private int contributeDuration;
    private int worthYear;
    private double amountExpected;

    public RMChooseWealthPreservationPlanManagedBean() {
    }

    public void chooseWealthPlan(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = null;

        Long id = getCustomerViaSessionMap().getCustomerBasicId();
        if (worthYear < contributeDuration) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Investment period cannot be shorter than contribute duration", null);
            context.addMessage(null, message);
        } else {
            createFinancialGoalSessionBeanLocal.createPortfolio(id, startingBalance, monthlyContribution, contributeDuration, worthYear, amountExpected, "wealth preservation plan");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer's portfolio has been succesfully created", null);
            context.addMessage(null, message);
        }
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(double monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    }

    public int getContributeDuration() {
        return contributeDuration;
    }

    public void setContributeDuration(int contributeDuration) {
        this.contributeDuration = contributeDuration;
    }

    public int getWorthYear() {
        return worthYear;
    }

    public void setWorthYear(int worthYear) {
        this.worthYear = worthYear;
    }

    public double getAmountExpected() {
        return amountExpected;
    }

    public void setAmountExpected(double amountExpected) {
        this.amountExpected = amountExpected;
    }

    public CustomerBasic getCustomerViaSessionMap() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        String customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        customer = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        return customer;
    }

}
