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

@Named(value = "employeeViewDashboardManagedBean")
@RequestScoped

public class EmployeeViewDashboardManagedBean {

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    private LineChartModel lineModel;

    public EmployeeViewDashboardManagedBean() {
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
        yAxis.setMax(0.1);

    }

    private LineChartModel initCategoryModel() {

        LineChartModel model = new LineChartModel();

        ChartSeries acquisition = new ChartSeries();
        acquisition.setLabel("Acquisition Rate");

        Rate janAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(1);
        if (janAcqRate.getRateId() != null) {
            acquisition.set("2016.01", janAcqRate.getRateValue());
        }

        Rate febAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(2);
        if (febAcqRate.getRateId() != null) {
            acquisition.set("2016.02", febAcqRate.getRateValue());
        }

        Rate marAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(3);
        if (marAcqRate.getRateId() != null) {
            acquisition.set("2016.03", marAcqRate.getRateValue());
        }

        Rate aprAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(4);
        if (aprAcqRate.getRateId() != null) {
            acquisition.set("2016.04", aprAcqRate.getRateValue());
        }

        Rate mayAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(5);
        if (mayAcqRate.getRateId() != null) {
            acquisition.set("2016.05", mayAcqRate.getRateValue());
        }

        Rate junAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(6);
        if (junAcqRate.getRateId() != null) {
            acquisition.set("2016.06", junAcqRate.getRateValue());
        }

        Rate julAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(7);
        if (julAcqRate.getRateId() != null) {
            acquisition.set("2016.07", julAcqRate.getRateValue());
        }

        Rate augAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(8);
        if (augAcqRate.getRateId() != null) {
            acquisition.set("2016.08", augAcqRate.getRateValue());
        }

        Rate sepAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(9);
        if (sepAcqRate.getRateId() != null) {
            acquisition.set("2016.09", sepAcqRate.getRateValue());
        }

        Rate octAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(10);
        if (octAcqRate.getRateId() != null) {
            acquisition.set("2016.10", octAcqRate.getRateValue());
        }

        Rate novAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(11);
        if (novAcqRate.getRateId() != null) {
            acquisition.set("2016.11", novAcqRate.getRateValue());
        }

        Rate decAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(12);
        if (decAcqRate.getRateId() != null) {
            acquisition.set("2016.12", decAcqRate.getRateValue());
        }

        ChartSeries attrition = new ChartSeries();
        attrition.setLabel("Attrition Rate");

        Rate janAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(1);
        if (janAttRate.getRateId() != null) {
            attrition.set("2016.01", janAttRate.getRateValue());
        }

        Rate febAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(2);
        if (febAttRate.getRateId() != null) {
            attrition.set("2016.02", febAttRate.getRateValue());
        }

        Rate marAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(3);
        if (marAttRate.getRateId() != null) {
            attrition.set("2016.03", marAttRate.getRateValue());
        }

        Rate aprAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(4);
        if (aprAttRate.getRateId() != null) {
            attrition.set("2016.04", aprAttRate.getRateValue());
        }

        Rate mayAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(5);
        if (mayAttRate.getRateId() != null) {
            attrition.set("2016.05", mayAttRate.getRateValue());
        }

        Rate junAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(6);
        if (junAttRate.getRateId() != null) {
            attrition.set("2016.06", junAttRate.getRateValue());
        }

        Rate julAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(7);
        if (julAttRate.getRateId() != null) {
            attrition.set("2016.07", julAttRate.getRateValue());
        }

        Rate augAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(8);
        if (augAttRate.getRateId() != null) {
            attrition.set("2016.08", augAttRate.getRateValue());
        }

        Rate sepAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(9);
        if (sepAttRate.getRateId() != null) {
            attrition.set("2016.09", sepAttRate.getRateValue());
        }

        Rate octAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(10);
        if (octAttRate.getRateId() != null) {
            attrition.set("2016.10", octAttRate.getRateValue());
        }

        Rate novAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(11);
        if (novAttRate.getRateId() != null) {
            attrition.set("2016.11", novAttRate.getRateValue());
        }

        Rate decAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(12);
        if (decAttRate.getRateId() != null) {
            attrition.set("2016.12", decAttRate.getRateValue());
        }

        model.addSeries(acquisition);
        model.addSeries(attrition);

        return model;
    }

}
