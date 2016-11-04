package managedbean.bi.employee;

import ejb.bi.entity.Rate;
import ejb.bi.session.RateSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

@Named(value = "employeeViewDashboardManagedBean")
@RequestScoped

public class EmployeeViewDashboardManagedBean {

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    private LineChartModel acqAttLineModel;
    private LineChartModel netAcqAttLineModel;
    private PieChartModel accountClosureReasonPieModel;
    private String updateDate;

    public EmployeeViewDashboardManagedBean() {
    }

    @PostConstruct
    public void init() {
        createLineModels();
        createPieModels();
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

        accountClosureReasonPieModel.set("Low interest rate", 540);
        accountClosureReasonPieModel.set("High service charge for transaction", 325);
        accountClosureReasonPieModel.set("Unsatisfied customer service", 702);
        accountClosureReasonPieModel.set("I do not need deposit account anymore", 421);
        accountClosureReasonPieModel.set("I have applied for another deposit account", 421);
        accountClosureReasonPieModel.set("Others", 421);

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
}
