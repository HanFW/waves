/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.wealth.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.wealth.session.CreateFinancialGoalSessionBeanLocal;
import java.io.Serializable;
import java.text.DecimalFormat;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerUseInvestmentCalculatorManagedBean")
@ViewScoped
public class CustomerUseInvestmentCalculatorManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerUseInvestmentCalculatorManagedBean
     */
    @EJB
    CreateFinancialGoalSessionBeanLocal createFinancialGoalSessionBeanLocal;

    private double startingBalance;
    private double rate;
    private double monthlyContribution;
    private int contributeDuration;
    private int worthYear;
    private double amountExpected;

    private double interestEarnedPercentage = 0.0;
    private double contributionsPercentage = 0.0;

    private double nYearValue;
    private double totalInterest;
    private double totalContributions;

    private PieChartModel pieModel1;

    private BarChartModel barModel;
    private ChartSeries previousBalances = new ChartSeries();
    private ChartSeries contributions = new ChartSeries();
    private ChartSeries interestEarned = new ChartSeries();
    private double minValue = 0.0;
    private double maxValue;
    private Axis xAxis;
    private Axis yAxis;
    
    private CustomerBasic customer;

    @PostConstruct
    public void init() {
        barModel = new BarChartModel();

        barModel.setTitle("Investment Bar Chart");
        barModel.setLegendPosition("e");
        barModel.setStacked(true);

        previousBalances.setLabel("Previous Balance");
        contributions.setLabel("Contributions");
        interestEarned.setLabel("Interest Earned");

        xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Year");

        yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Money");
    }

    public CustomerUseInvestmentCalculatorManagedBean() {
    }

    public void viewResult(ActionEvent event) {

        DecimalFormat df = new DecimalFormat("0.00");

        double thisYearInterest = 0.0;
        double totalPrincipal = startingBalance;

        previousBalances.set(2016, startingBalance);
        contributions.set(2016, 0);
        interestEarned.set(2016, 0);

        int year = 2017;

//        System.out.println("view result"); 
        if (contributeDuration >= worthYear) {
            totalContributions = monthlyContribution * 12 * worthYear;

            for (int i = 0; i < worthYear; i++) {
                previousBalances.set(year + i, totalPrincipal);
                contributions.set(year + i, monthlyContribution * 12);

                thisYearInterest = (totalPrincipal + monthlyContribution * 12) * rate;
                totalPrincipal = (totalPrincipal + monthlyContribution * 12) * (1 + rate);
                totalInterest += thisYearInterest;

                interestEarned.set(year + i, thisYearInterest);
//                year++;

            }

        }//contributeDuration>=worthYear
        else {
            totalContributions = monthlyContribution * 12 * contributeDuration;

            for (int j = 0; j < worthYear; j++) {
                if (j >= contributeDuration) {
                    monthlyContribution = 0;
                }

                previousBalances.set(year + j, totalPrincipal);
                contributions.set(year + j, monthlyContribution * 12);

                thisYearInterest = (totalPrincipal + monthlyContribution * 12) * rate;
                totalPrincipal = (totalPrincipal + monthlyContribution * 12) * (1 + rate);
                totalInterest += thisYearInterest;

                interestEarned.set(year + j, thisYearInterest);
//                year++;
            }
        }//contributeDuration < worthYear

        setnYearValue(totalPrincipal);
        setTotalInterest(Double.valueOf(df.format(totalInterest)));
        setTotalContributions(Double.valueOf(df.format(totalContributions)));
        setInterestEarnedPercentage(Double.valueOf(df.format(totalInterest / totalPrincipal)));
        contributionsPercentage = (totalContributions + startingBalance) / totalPrincipal;
        setContributionsPercentage(Double.valueOf(df.format(contributionsPercentage)));

        //create pie chart
        createPieModel1();

        //bar chart
        barModel.addSeries(previousBalances);
        barModel.addSeries(contributions);
        barModel.addSeries(interestEarned);
        yAxis.setMin(minValue);
        yAxis.setMax(maxValue);

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('calculatorWizard').next();");

    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
        maxValue = startingBalance * 20;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate * 0.01;
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

    public double getnYearValue() {
        return nYearValue;
    }

    public void setnYearValue(double nYearValue) {
        this.nYearValue = nYearValue;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterst) {
        this.totalInterest = totalInterst;
    }

    public double getTotalContributions() {
        return totalContributions;
    }

    public void setTotalContributions(double totalContributions) {
        this.totalContributions = totalContributions;
    }

    public double getInterestEarnedPercentage() {
        return interestEarnedPercentage;
    }

    public void setInterestEarnedPercentage(double interestEarnedPercentage) {
        this.interestEarnedPercentage = interestEarnedPercentage;
    }

    public double getContributionsPercentage() {
        return contributionsPercentage;
    }

    public void setContributionsPercentage(double contributionsPercentage) {
        this.contributionsPercentage = contributionsPercentage;
    }

    public double getAmountExpected() {
        return amountExpected;
    }

    public void setAmountExpected(double amountExpected) {
        this.amountExpected = amountExpected;
    }

    public void createPieModel1() {
        pieModel1 = new PieChartModel();

        pieModel1.set("Interest Earned", interestEarnedPercentage);
        pieModel1.set("Contributions", contributionsPercentage);

        pieModel1.setLegendPosition("w");
        pieModel1.setShowDataLabels(true);
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void declareFinancialGoal() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;
        
        Long id= getCustomerViaSessionMap().getCustomerBasicId();

        createFinancialGoalSessionBeanLocal.createPortfolio(id,startingBalance, monthlyContribution, contributeDuration, worthYear, amountExpected);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your financial goal has been successfully saved!", null);
        context.addMessage(null, message);
    }
    
    
    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    } 

}
