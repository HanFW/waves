/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.wealth.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.wealth.session.CreateCustomerRiskProfileSessionBeanLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author aaa
 */
@Named(value = "customerOnlineRiskToleranceTestManagedBean")
@ViewScoped
public class CustomerOnlineRiskToleranceTestManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerOnlineRiskToleranceTestManagedBean
     */
    private int answer1;
    private int answer2;
    private int answer3;
    private int answer4;
    private int answer5;
    private int answer6;
    private boolean risk1;
    private boolean risk2;
    private boolean risk3;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    private PieChartModel pieModel3;

    @EJB
    CreateCustomerRiskProfileSessionBeanLocal createCustomerRiskProfileSessionBeanLocal;

    public CustomerOnlineRiskToleranceTestManagedBean() {
    }

    @PostConstruct
    public void init() {
        createPieModel1();
        createPieModel2();
        createPieModel3();
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public void calculateRiskTestScore(ActionEvent event) {
        int score = answer1 + answer2 + answer3 + answer4 + answer5 + answer6;
        System.out.println("calculate risk tolerance test score: " + score);

        CustomerBasic customer = getCustomerViaSessionMap();
        Long id = customer.getCustomerBasicId();

        if (score > 0 && score < 8) {
            risk3 = true;
            createCustomerRiskProfileSessionBeanLocal.createRiskProfile(id, answer1, answer2, answer3, answer4, answer5, answer6, score, "conservative");
        } else if (score >= 8 && score < 15) {
            risk2 = true;
            createCustomerRiskProfileSessionBeanLocal.createRiskProfile(id, answer1, answer2, answer3, answer4, answer5, answer6, score, "moderate");
        } else {
            risk1 = true;
            createCustomerRiskProfileSessionBeanLocal.createRiskProfile(id, answer1, answer2, answer3, answer4, answer5, answer6, score, "agressive");
        }

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('riskToleranceTestWizard').next();");
    }

    public int getAnswer1() {
        return answer1;
    }

    public void setAnswer1(int answer1) {
        this.answer1 = answer1;
        System.out.println("answer 1: " + answer1);
    }

    public int getAnswer2() {
        return answer2;
    }

    public void setAnswer2(int answer2) {
        this.answer2 = answer2;
        System.out.println("answer 2: " + answer2);
    }

    public int getAnswer3() {
        return answer3;
    }

    public void setAnswer3(int answer3) {
        this.answer3 = answer3;
        System.out.println("answer 3: " + answer3);
    }

    public int getAnswer4() {
        return answer4;
    }

    public void setAnswer4(int answer4) {
        this.answer4 = answer4;
        System.out.println("answer 4: " + answer4);
    }

    public int getAnswer5() {
        return answer5;
    }

    public void setAnswer5(int answer5) {
        this.answer5 = answer5;
        System.out.println("answer 5: " + answer5);
    }

    public int getAnswer6() {
        return answer6;
    }

    public void setAnswer6(int answer6) {
        this.answer6 = answer6;
        System.out.println("answer 6: " + answer6);
    }

    public boolean isRisk1() {
        return risk1;
    }

    public void setRisk1(boolean risk1) {
        this.risk1 = risk1;
    }

    public boolean isRisk2() {
        return risk2;
    }

    public void setRisk2(boolean risk2) {
        this.risk2 = risk2;
    }

    public boolean isRisk3() {
        return risk3;
    }

    public void setRisk3(boolean risk3) {
        this.risk3 = risk3;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void createPieModel1() {
        pieModel1 = new PieChartModel();

        pieModel1.set("Equity", 0.8);
        pieModel1.set("Fiexed Income", 0.2);

        pieModel1.setTitle("Aggressive Portfolio");
        pieModel1.setLegendPosition("w");
        pieModel1.setShowDataLabels(true);
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void createPieModel2() {
        pieModel2 = new PieChartModel();

        pieModel2.set("Cash Equivalents", 0.15);
        pieModel2.set("Bonds", 0.75);
        pieModel2.set("Stocks", 0.1);

        pieModel2.setTitle("Moderate Portfolio");
        pieModel2.setLegendPosition("w");
        pieModel2.setShowDataLabels(true);
    }

    public PieChartModel getPieModel3() {
        return pieModel3;
    }

    public void createPieModel3() {
        pieModel3 = new PieChartModel();

        pieModel3.set("Equity", 0.3);
        pieModel3.set("Fiexed Income", 0.7);

        pieModel3.setTitle("Conservative Portfolio");
        pieModel3.setLegendPosition("w");
        pieModel3.setShowDataLabels(true);
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        CustomerBasic customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

}
