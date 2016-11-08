package managedbean.bi.employee;

import ejb.bi.entity.AccountClosureReason;
import ejb.bi.entity.NumOfExistingCustomer;
import ejb.bi.entity.Rate;
import ejb.bi.session.AccountClosureReasonSessionBeanLocal;
import ejb.bi.session.NumOfExistingCustomerSessionBeanLocal;
import ejb.bi.session.RateSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

@Named(value = "employeeViewDashboardManagedBean")
@RequestScoped

public class EmployeeViewDashboardManagedBean {

    @EJB
    private NumOfExistingCustomerSessionBeanLocal numOfExistingCustomerSessionBeanLocal;

    @EJB
    private AccountClosureReasonSessionBeanLocal accountClosureReasonSessionBeanLocal;

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    private LineChartModel acqAttLineModel;
    private LineChartModel netAcqAttLineModel;
    private PieChartModel accountClosureReasonPieModel;
    private BarChartModel barModel;

    private String updateDate;

    public EmployeeViewDashboardManagedBean() {
    }

    @PostConstruct
    public void init() {
        createLineModels();
        createPieModels();
        createBarModel();
    }

    public LineChartModel getAcqAttLineModel() {
        return acqAttLineModel;
    }

    public void setAcqAttLineModel(LineChartModel acqAttLineModel) {
        this.acqAttLineModel = acqAttLineModel;
    }

    public LineChartModel getNetAcqAttLineModel() {
        return netAcqAttLineModel;
    }

    public void setNetAcqAttLineModel(LineChartModel netAcqAttLineModel) {
        this.netAcqAttLineModel = netAcqAttLineModel;
    }

    public PieChartModel getAccountClosureReasonPieModel() {
        return accountClosureReasonPieModel;
    }

    public void setAccountClosureReasonPieModel(PieChartModel accountClosureReasonPieModel) {
        this.accountClosureReasonPieModel = accountClosureReasonPieModel;
    }

    public String getUpdateDate() {
        Rate rate = rateSessionBeanLocal.getAcqRate();
        updateDate = rate.getUpdateYear() + "." + rate.getUpdateMonth();

        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    private void createLineModels() {

        acqAttLineModel = initAcqAttModel();
        acqAttLineModel.setTitle("Customer Acquisition Rate & Customer Attrition Rate");
        acqAttLineModel.setLegendPosition("e");
        acqAttLineModel.setShowPointLabels(true);
        acqAttLineModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = acqAttLineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Rate");
        yAxis.setMin(0);
        yAxis.setMax(0.1);

        netAcqAttLineModel = initNetAcqAttModel();
        netAcqAttLineModel.setTitle("Customer Net Acquisition Rate");
        netAcqAttLineModel.setLegendPosition("e");
        netAcqAttLineModel.setShowPointLabels(true);
        netAcqAttLineModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        yAxis = netAcqAttLineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Rate");
        yAxis.setMin(-0.1);
        yAxis.setMax(0.1);

    }

    private void createPieModels() {
        createAccountClosureReasonPieModel1();
    }

    private void createAccountClosureReasonPieModel1() {

        accountClosureReasonPieModel = new PieChartModel();

        AccountClosureReason interest = accountClosureReasonSessionBeanLocal.retrieveAccountClosureReasonByName("Interest");
        AccountClosureReason serviceCharge = accountClosureReasonSessionBeanLocal.retrieveAccountClosureReasonByName("Service Charge");
        AccountClosureReason customerService = accountClosureReasonSessionBeanLocal.retrieveAccountClosureReasonByName("Customer Service");
        AccountClosureReason dontNeed = accountClosureReasonSessionBeanLocal.retrieveAccountClosureReasonByName("Dont Need");
        AccountClosureReason appliedAnother = accountClosureReasonSessionBeanLocal.retrieveAccountClosureReasonByName("Applied Another");
        AccountClosureReason others = accountClosureReasonSessionBeanLocal.retrieveAccountClosureReasonByName("Other Reasons");

        accountClosureReasonPieModel.set("Low interest rate", Double.valueOf(interest.getRateValue()));
        accountClosureReasonPieModel.set("High service charge for transaction", Double.valueOf(serviceCharge.getRateValue()));
        accountClosureReasonPieModel.set("Unsatisfied customer service", Double.valueOf(customerService.getRateValue()));
        accountClosureReasonPieModel.set("I do not need deposit account anymore", Double.valueOf(dontNeed.getRateValue()));
        accountClosureReasonPieModel.set("I have applied for another deposit account", Double.valueOf(appliedAnother.getRateValue()));
        accountClosureReasonPieModel.set("Others", Double.valueOf(others.getRateValue()));

        accountClosureReasonPieModel.setTitle("Deposit Account Closure Reasons");
        accountClosureReasonPieModel.setLegendPosition("w");
        accountClosureReasonPieModel.setShowDataLabels(true);

    }

    private LineChartModel initAcqAttModel() {

        LineChartModel model = new LineChartModel();

        ChartSeries acquisition = new ChartSeries();
        acquisition.setLabel("Acquisition Rate");

        List<Rate> acqRates = rateSessionBeanLocal.getCurrentYearAcqRate();

        int i;
        for (i = 0; i < acqRates.size(); i++) {
            Integer updateMonth = acqRates.get(i).getUpdateMonth();
            Integer updateYear = acqRates.get(i).getUpdateYear();
            Rate acqRate = rateSessionBeanLocal.retrieveAcquisitionRateByMonth(updateMonth);
            String date = updateYear + "." + updateMonth;
            acquisition.set(date, acqRate.getRateValue());
            if (i == 11) {
                i = 0;
            }
        }

        ChartSeries attrition = new ChartSeries();
        attrition.setLabel("Attrition Rate");

        List<Rate> attRates = rateSessionBeanLocal.getCurrentYearAttRate();

        int j;
        for (j = 0; j < attRates.size(); j++) {
            Integer updateMonth = attRates.get(j).getUpdateMonth();
            Integer updateYear = attRates.get(j).getUpdateYear();
            Rate attRate = rateSessionBeanLocal.retrieveAttritionRateByMonth(updateMonth);
            String date = updateYear + "." + updateMonth;
            attrition.set(date, attRate.getRateValue());
            if (j == 11) {
                j = 0;
            }
        }

        model.addSeries(acquisition);
        model.addSeries(attrition);

        return model;
    }

    private LineChartModel initNetAcqAttModel() {

        LineChartModel model = new LineChartModel();

        ChartSeries netRate = new ChartSeries();
        netRate.setLabel("Net Acquisition Rate");

        List<Rate> acqRates = rateSessionBeanLocal.getCurrentYearAcqRate();
        List<Rate> attRates = rateSessionBeanLocal.getCurrentYearAttRate();

        int i;
        for (i = 0; i < attRates.size(); i++) {
            Integer updateMonth = attRates.get(i).getUpdateMonth();
            Integer updateYear = attRates.get(i).getUpdateYear();
            Double netValue = acqRates.get(i).getRateValue() - attRates.get(i).getRateValue();
            String date = updateYear + "." + updateMonth;
            netRate.set(date, netValue);
            if (i == 11) {
                i = 0;
            }
        }

        model.addSeries(netRate);

        return model;
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Number of Closing/Openning Accounts");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Date");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Numbers");
        yAxis.setMin(0);
        yAxis.setMax(30);
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries openAccount = new ChartSeries();
        openAccount.setLabel("Number of Opening Accounts");

        ChartSeries closeAccount = new ChartSeries();
        closeAccount.setLabel("Number of Closing Accounts");

        List<NumOfExistingCustomer> numOfExistingCustomers = numOfExistingCustomerSessionBeanLocal.getCurrentYearNumOfExistingCustomer();

        int i;
        for (i = 0; i < numOfExistingCustomers.size(); i++) {

            Integer updateMonth = numOfExistingCustomers.get(i).getUpdateMonth();
            Integer updateYear = numOfExistingCustomers.get(i).getUpdateYear();

            String numOfOpeningAccounts = numOfExistingCustomers.get(i).getNumOfOpeningAccounts();
            String numOfClosingAccounts = numOfExistingCustomers.get(i).getNumOfClosingAccounts();
            String date = updateYear + "." + updateMonth;

            openAccount.set(date, Integer.valueOf(numOfOpeningAccounts));
            closeAccount.set(date, Integer.valueOf(numOfClosingAccounts));

            if (i == 11) {
                i = 0;
            }
        }

        model.addSeries(openAccount);
        model.addSeries(closeAccount);

        return model;
    }
}
