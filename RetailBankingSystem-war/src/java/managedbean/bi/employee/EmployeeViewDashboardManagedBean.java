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
import org.primefaces.model.chart.PieChartModel;

@Named(value = "employeeViewDashboardManagedBean")
@RequestScoped

public class EmployeeViewDashboardManagedBean {

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    private LineChartModel acqAttLineModel;
    private LineChartModel netAcqAttLineModel;
    private PieChartModel accountClosureReasonPieModel;

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

    private LineChartModel initNetAcqAttModel() {

        LineChartModel model = new LineChartModel();

        ChartSeries netRate = new ChartSeries();
        netRate.setLabel("Net Acquisition Rate");

        Rate janAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(1);
        Rate janAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(1);
        if (janAcqRate.getRateId() != null && janAttRate.getRateId() != null) {
            Double janNetValue = janAcqRate.getRateValue() - janAttRate.getRateValue();
            netRate.set("2016.01", janNetValue);
        }

        Rate febAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(2);
        Rate febAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(2);
        if (febAcqRate.getRateId() != null && febAttRate.getRateId() != null) {
            Double febNetValue = febAcqRate.getRateValue() - febAttRate.getRateValue();
            netRate.set("2016.02", febNetValue);
        }

        Rate marAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(3);
        Rate marAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(3);
        if (marAcqRate.getRateId() != null && marAttRate.getRateId() != null) {
            Double marNetValue = marAcqRate.getRateValue() - marAttRate.getRateValue();
            netRate.set("2016.03", marNetValue);
        }

        Rate aprAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(4);
        Rate aprAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(4);
        if (aprAcqRate.getRateId() != null && aprAttRate.getRateId() != null) {
            Double aprNetValue = aprAcqRate.getRateValue() - aprAttRate.getRateValue();
            netRate.set("2016.04", aprNetValue);
        }

        Rate mayAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(5);
        Rate mayAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(5);
        if (mayAcqRate.getRateId() != null && mayAttRate.getRateId() != null) {
            Double mayNetValue = mayAcqRate.getRateValue() - mayAttRate.getRateValue();
            netRate.set("2016.05", mayNetValue);
        }

        Rate junAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(6);
        Rate junAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(6);
        if (junAcqRate.getRateId() != null && junAttRate.getRateId() != null) {
            Double junNetValue = junAcqRate.getRateValue() - junAttRate.getRateValue();
            netRate.set("2016.06", junNetValue);
        }

        Rate julAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(7);
        Rate julAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(7);
        if (julAcqRate.getRateId() != null && julAttRate.getRateId() != null) {
            Double julNetValue = julAcqRate.getRateValue() - julAttRate.getRateValue();
            netRate.set("2016.07", julNetValue);
        }

        Rate augAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(8);
        Rate augAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(8);
        if (augAcqRate.getRateId() != null && augAttRate.getRateId() != null) {
            Double augNetValue = augAcqRate.getRateValue() - augAttRate.getRateValue();
            netRate.set("2016.08", augNetValue);
        }

        Rate sepAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(9);
        Rate sepAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(9);
        if (sepAcqRate.getRateId() != null && sepAttRate.getRateId() != null) {
            Double sepNetValue = sepAcqRate.getRateValue() - sepAttRate.getRateValue();
            netRate.set("2016.09", sepNetValue);
        }

        Rate octAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(10);
        Rate octAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(10);
        if (octAcqRate.getRateId() != null && octAttRate.getRateId() != null) {
            Double octNetValue = octAcqRate.getRateValue() - octAttRate.getRateValue();
            netRate.set("2016.10", octNetValue);
        }

        Rate novAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(11);
        Rate novAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(11);
        if (novAcqRate.getRateId() != null && novAttRate.getRateId() != null) {
            Double novNetValue = novAcqRate.getRateValue() - novAttRate.getRateValue();
            netRate.set("2016.11", novNetValue);
        }

        Rate decAcqRate = rateSessionBeanLocal.retrieveAcquisitionRateByDate(12);
        Rate decAttRate = rateSessionBeanLocal.retrieveAttritionRateByDate(12);
        if (decAcqRate.getRateId() != null && decAttRate.getRateId() != null) {
            Double decNetValue = decAcqRate.getRateValue() - decAttRate.getRateValue();
            netRate.set("2016.12", decNetValue);
        }

        model.addSeries(netRate);

        return model;
    }
}
