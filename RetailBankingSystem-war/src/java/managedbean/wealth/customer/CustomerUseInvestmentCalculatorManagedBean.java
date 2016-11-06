/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.wealth.customer;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerUseInvestmentCalculatorManagedBean")
@RequestScoped
public class CustomerUseInvestmentCalculatorManagedBean {

    /**
     * Creates a new instance of CustomerUseInvestmentCalculatorManagedBean
     */
    private double startingBalance;
    private double rate;
    private double monthlyContribution;
    private int contributeDuration;
    private int worthYear;
    
    private PieChartModel pieModel1;
    
    public CustomerUseInvestmentCalculatorManagedBean() {
    }
    
    public void viewResult(ActionEvent event){
        System.out.println("view result");
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
    
        public void createPieModel1() {
        pieModel1 = new PieChartModel();

        pieModel1.set("Interest Earned", 0.8);
        pieModel1.set("Contributions", 0.2);

//        pieModel1.setTitle("Aggressive");
        pieModel1.setLegendPosition("w");
        pieModel1.setShowDataLabels(true);
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }
    
}
