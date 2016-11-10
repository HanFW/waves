/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.portfolio.employee;

import ejb.wealth.entity.Bond;
import ejb.wealth.entity.Fund;
import ejb.wealth.entity.Portfolio;
import ejb.wealth.entity.Stock;
import ejb.wealth.session.AssetSessionBeanLocal;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author aaa
 */
@Named(value = "rMAddPorfolioManagedBean")
@ViewScoped
public class RMAddPorfolioManagedBean implements Serializable {

    /**
     * Creates a new instance of RMAddPorfolioManagedBean
     */
    private String riskLevel;
    private Long assetTypeId;
    private double unitPurchased;
    private double totalPurchase = 0.0;
    private String totalPurchaseStr;
    private double availableBalance;
    private String availableBalanceStr;
    private double startingBalance;
    private String startingBalanceStr;
    private String[] purchase = new String[5];
    private ArrayList<String[]> purchaseList = new ArrayList<String[]>();

    //Line Model
    private LineChartModel stockLineModel;
    private LineChartModel fundLineModel;
    private LineChartModel bondLineModel;

    @EJB
    private AssetSessionBeanLocal assetSessionBeanLocal;

    public RMAddPorfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Long portfolioId = (Long) ec.getSessionMap().get("portfolioId");
        Portfolio portfolio = assetSessionBeanLocal.getPortfolioById(portfolioId);
        startingBalance = portfolio.getStartingBalance();
        
        stockLineModel = new LineChartModel();
        fundLineModel = new LineChartModel();
        bondLineModel = new LineChartModel();
    }
    private void createStockLineModels(Long selectedId) {

        stockLineModel = initStockModel(selectedId);
        stockLineModel.setTitle("Stock Historical Trend");
        stockLineModel.setShowPointLabels(true);
        stockLineModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = stockLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(50);
    }

    private void createFundLineModels(Long selectedId) {

        fundLineModel = initFundModel(selectedId);
        fundLineModel.setTitle("Fund Historical Trend");
        fundLineModel.setShowPointLabels(true);
        fundLineModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = fundLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(50);
    }

    private void createBondLineModels(Long selectedId) {

        bondLineModel = initBondModel(selectedId);
        bondLineModel.setTitle("Bond Projected Value");
        bondLineModel.setShowPointLabels(true);
        bondLineModel.getAxes().put(AxisType.X, new CategoryAxis("Year"));
        Axis yAxis = bondLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
//        yAxis.setMax(1500);
    }

    private LineChartModel initStockModel(Long selectedId) {
        LineChartModel model = new LineChartModel();
        DecimalFormat df = new DecimalFormat("0.00");
        Stock stock = assetSessionBeanLocal.getStockById(selectedId);

        double[] price = new double[7];
        price[0] = stock.getCurrentValue();
        for (int i = 1; i < 7; i++) {
            price[i] = Double.valueOf(df.format(generateNextTrend(stock.getRisk(), price[i - 1])));
        }
        ChartSeries stockChart = new ChartSeries();
        stockChart.set("2016-11-06", price[6]);
        stockChart.set("2016-11-07", price[5]);
        stockChart.set("2016-11-08", price[4]);
        stockChart.set("2016-11-09", price[3]);
        stockChart.set("2016-11-10", price[2]);
        stockChart.set("2016-11-11", price[1]);
        stockChart.set("2016-11-12", price[0]);

        model.addSeries(stockChart);

        return model;
    }

    private LineChartModel initFundModel(Long selectedId) {
        LineChartModel model = new LineChartModel();
        DecimalFormat df = new DecimalFormat("0.00");
        Fund fund = assetSessionBeanLocal.getFundById(selectedId);

        double[] price = new double[7];
        price[0] = fund.getCurrentValue();
        for (int i = 1; i < 7; i++) {
            price[i] = Double.valueOf(df.format(generateNextTrend(fund.getRisk(), price[i - 1])));
        }

        ChartSeries fundChart = new ChartSeries();
        fundChart.set("2016-11-06", price[6]);
        fundChart.set("2016-11-07", price[5]);
        fundChart.set("2016-11-08", price[4]);
        fundChart.set("2016-11-09", price[3]);
        fundChart.set("2016-11-10", price[2]);
        fundChart.set("2016-11-11", price[1]);
        fundChart.set("2016-11-12", price[0]);

        model.addSeries(fundChart);

        return model;
    }

    private LineChartModel initBondModel(Long selectedId) {
        Bond bond = assetSessionBeanLocal.getBondById(selectedId);
        DecimalFormat df = new DecimalFormat("0.00");
        LineChartModel model = new LineChartModel();

        double[] price = new double[5];
        price[0] = bond.getCurrentValue();
        for (int i = 1; i < 5; i++) {
            price[i] = Double.valueOf(df.format(price[i - 1] * (1 + bond.getCouponRate()/100)));
        }
        ChartSeries bondChart = new ChartSeries();
        bondChart.set("2016", price[0]);
        bondChart.set("2017", price[1]);
        bondChart.set("2018", price[2]);
        bondChart.set("2019", price[3]);
        bondChart.set("2020", price[4]);

        model.addSeries(bondChart);

        return model;
    }

    public List<Fund> getAllFunds() {
        return assetSessionBeanLocal.getAllFunds();
    }

    public List<Bond> getAllBonds() {
        return assetSessionBeanLocal.getAllBonds();
    }

    public List<Stock> getAllStocks() {
        return assetSessionBeanLocal.getAllStocks();
    }

    public String showRiskLevel(double risk) {
        if (risk < 2.0) {
            riskLevel = "Low";
        } else if (risk == 2.0) {
            riskLevel = "Midium";
        } else {
            riskLevel = "High";
        }
        return riskLevel;
    }

    public void viewStockDetails(Long selectedId) {
        RequestContext rc = RequestContext.getCurrentInstance();

        rc.execute("PF('stockDetailDialog').show();");

        createStockLineModels(selectedId);
        assetTypeId = selectedId;
    }

    public void viewFundDetails(Long selectedId) {
        RequestContext rc = RequestContext.getCurrentInstance();

        rc.execute("PF('fundDetailDialog').show();");
        createFundLineModels(selectedId);
        assetTypeId = selectedId;
    }

    public void viewBondDetails(Long selectedId) {
        RequestContext rc = RequestContext.getCurrentInstance();

        rc.execute("PF('bondDetailDialog').show();");
        createBondLineModels(selectedId);
        assetTypeId = selectedId;
    }

    public void createStockPurchaseList() { 
        DecimalFormat df = new DecimalFormat("0.00");
        double currentValue = assetSessionBeanLocal.getStockById(assetTypeId).getCurrentValue();
        purchase[0] = assetTypeId.toString();
        purchase[1] = assetSessionBeanLocal.getStockById(assetTypeId).getAssetIssuerName();
        purchase[2] = df.format(currentValue);
        purchase[3] = df.format(unitPurchased);
        purchase[4] = df.format(currentValue * unitPurchased);

        purchaseList.add(purchase);
        totalPurchase += currentValue * unitPurchased;
        purchase = new String[5];
        unitPurchased = 0;
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('stockDetailDialog').hide();");
    }

    public void createFundPurchaseList() {
        DecimalFormat df = new DecimalFormat("0.00");
        double currentValue = assetSessionBeanLocal.getFundById(assetTypeId).getCurrentValue();
        purchase[0] = assetTypeId.toString();
        purchase[1] = assetSessionBeanLocal.getFundById(assetTypeId).getAssetIssuerName();
        purchase[2] = df.format(currentValue);
        purchase[3] = df.format(unitPurchased);
        purchase[4] = df.format(currentValue * unitPurchased);

        purchaseList.add(purchase);
        totalPurchase += (currentValue * unitPurchased);
        purchase = new String[5];
        unitPurchased = 0;
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('fundDetailDialog').hide();");
    }

    public void createBondPurchaseList() {
        double currentValue = assetSessionBeanLocal.getBondById(assetTypeId).getCurrentValue();
        purchase[0] = assetTypeId.toString();
        purchase[1] = assetSessionBeanLocal.getBondById(assetTypeId).getAssetIssuerName();
        purchase[2] = String.valueOf(currentValue);
        purchase[3] = String.valueOf(unitPurchased);
        purchase[4] = String.valueOf(currentValue * unitPurchased);

        purchaseList.add(purchase);
        totalPurchase += (currentValue * unitPurchased);
        purchase = new String[5];
        unitPurchased = 0;
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('bondDetailDialog').hide();");
    }

    public void removePurchase(String[] purchase) {
        double purchaseAmt = Double.valueOf(purchase[4]);
        totalPurchase -= purchaseAmt;
        purchaseList.remove(purchase);
    }

    public void confirmPurchase() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        Long portfolioId = (Long) context.getExternalContext().getSessionMap().get("portfolioId");
        if (portfolioId == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No portfolio is selected!", null);
            context.addMessage(null, message);
        } else if (purchaseList.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No purchase has been made!", null);
            context.addMessage(null, message);
        } else if (!purchaseList.isEmpty()) {

            assetSessionBeanLocal.savePurchaseInfo(portfolioId, purchaseList);

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Purchase Successful!", null);
            context.addMessage(null, message);
            purchase = null;
        }

    }

    private double generateNextTrend(double risk, double currentValue) {
        Random rdm = new Random();
        double range = (risk * 2) * 100;
        double newChange = rdm.nextInt((int) range) - risk * 100;
        double changeWithoutPercentSign = newChange / 100;

        double newValue = (changeWithoutPercentSign / 100 + 1) * currentValue;

        return newValue;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public AssetSessionBeanLocal getAssetSessionBeanLocal() {
        return assetSessionBeanLocal;
    }

    public void setAssetSessionBeanLocal(AssetSessionBeanLocal assetSessionBeanLocal) {
        this.assetSessionBeanLocal = assetSessionBeanLocal;
    }

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public double getUnitPurchased() {
        return unitPurchased;
    }

    public void setUnitPurchased(double unitPurchased) {
        this.unitPurchased = unitPurchased;
    }

    public String[] getPurchase() {
        return purchase;
    }

    public void setPurchase(String[] purchase) {
        this.purchase = purchase;
    }

    public ArrayList<String[]> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(ArrayList<String[]> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public LineChartModel getStockLineModel() {
        return stockLineModel;
    }

    public void setStockLineModel(LineChartModel stockLineModel) {
        this.stockLineModel = stockLineModel;
    }

    public LineChartModel getFundLineModel() {
        return fundLineModel;
    }

    public void setFundLineModel(LineChartModel fundLineModel) {
        this.fundLineModel = fundLineModel;
    }

    public LineChartModel getBondLineModel() {
        return bondLineModel;
    }

    public void setBondLineModel(LineChartModel bondLineModel) {
        this.bondLineModel = bondLineModel;
    }

    public double getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(double totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public double getAvailableBalance() {
        return (startingBalance - totalPurchase);
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
    }

    public String getTotalPurchaseStr() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(totalPurchase);
    }

    public void setTotalPurchaseStr(String totalPurchaseStr) {
        this.totalPurchaseStr = totalPurchaseStr;
    }

    public String getAvailableBalanceStr() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(startingBalance - totalPurchase);
    }

    public void setAvailableBalanceStr(String availableBalanceStr) {
        this.availableBalanceStr = availableBalanceStr;
    }

    public String getStartingBalanceStr() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(startingBalance);
    }

    public void setStartingBalanceStr(String startingBalanceStr) {
        this.startingBalanceStr = startingBalanceStr;
    }
    
    

}
