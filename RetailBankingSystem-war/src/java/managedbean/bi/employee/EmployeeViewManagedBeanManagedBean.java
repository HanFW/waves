package managedbean.bi.employee;

import ejb.bi.entity.Rate;
import ejb.bi.session.RateSessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

@Named(value = "employeeViewManagedBeanManagedBean")
@RequestScoped

public class EmployeeViewManagedBeanManagedBean {

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    private LineChartModel lineModel;

    public EmployeeViewManagedBeanManagedBean() {
    }

    @PostConstruct
    public void init() {
        createLineModels();
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    private void createLineModels() {

        lineModel = initCategoryModel();
        lineModel.setTitle("Customer Acquisition Rate & Customer Attrition Rate");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Rate");
        yAxis.setMin(0);
        yAxis.setMax(1);

    }

    private LineChartModel initCategoryModel() {

        LineChartModel model = new LineChartModel();

        ChartSeries acquisition = new ChartSeries();
        acquisition.setLabel("Acquisition Rate");

        Rate janAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate("2016.01");
        if (janAcqRate.getRateId() != null) {
            acquisition.set("2016.01", janAcqRate.getRateValue());
        }

        Rate febAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate("2016.02");
        if (febAcqRate.getRateId() != null) {
            acquisition.set("2016.02", febAcqRate.getRateValue());
        }

        Rate marAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate("2016.03");
        if (marAcqRate.getRateId() != null) {
            acquisition.set("2016.03", marAcqRate.getRateValue());
        }

        Rate aprAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate("2016.04");
        if (aprAcqRate.getRateId() != null) {
            acquisition.set("2016.04", aprAcqRate.getRateValue());
        }

        Rate mayAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate("2016.05");
        if (mayAcqRate.getRateId() != null) {
            acquisition.set("2016.05", mayAcqRate.getRateValue());
        }

        Rate junAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate("2016.06");
        if (junAcqRate.getRateId() != null) {
            acquisition.set("2016.06", junAcqRate.getRateValue());
        }

        ChartSeries attrition = new ChartSeries();
        attrition.setLabel("Attrition Rate");

        Rate janAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate("2016.01");
        if (janAttRate.getRateId() != null) {
            attrition.set("2016.01", janAttRate.getRateValue());
        }

        Rate febAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate("2016.02");
        if (febAttRate.getRateId() != null) {
            attrition.set("2016.02", febAttRate.getRateValue());
        }

        Rate marAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate("2016.03");
        if (marAttRate.getRateId() != null) {
            attrition.set("2016.03", marAttRate.getRateValue());
        }

        Rate aprAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate("2016.04");
        if (aprAttRate.getRateId() != null) {
            attrition.set("2016.04", aprAttRate.getRateValue());
        }

        Rate mayAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate("2016.05");
        if (mayAttRate.getRateId() != null) {
            attrition.set("2016.05", mayAttRate.getRateValue());
        }

        Rate junAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate("2016.05");
        if (junAttRate.getRateId() != null) {
            attrition.set("2016.06", junAttRate.getRateValue());
        }

        model.addSeries(acquisition);
        model.addSeries(attrition);

        return model;
    }

}
