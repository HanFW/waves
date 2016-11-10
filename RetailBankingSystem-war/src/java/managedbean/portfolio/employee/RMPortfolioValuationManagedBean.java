/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.portfolio.employee;

import ejb.wealth.entity.Asset;
import ejb.wealth.entity.AssetTypePriceHistory;
import ejb.wealth.entity.Portfolio;
import ejb.wealth.session.AssetSessionBeanLocal;
import ejb.wealth.session.AssetTypePriceSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Named(value = "rMPortfolioValuationManagedBean")
@ViewScoped
public class RMPortfolioValuationManagedBean implements Serializable{

    /**
     * Creates a new instance of RMPortfolioValuationManagedBean
     */
    
    private double startingBalance;
    private Long portfolioId;
    
    private String riskLevel;
    //Line Model
    private LineChartModel stockLineModel;
    private LineChartModel fundLineModel;
    private LineChartModel bondLineModel;
    
    @EJB
    private AssetSessionBeanLocal assetSessionBeanLocal;
    
    @EJB
    private AssetTypePriceSessionBeanLocal assetTypePriceSessionBeanLocal;
    
    public RMPortfolioValuationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        portfolioId = (Long) ec.getSessionMap().get("portfolioId");
        
        stockLineModel = new LineChartModel();
        fundLineModel = new LineChartModel();
    }
    
    public List<Asset> getAllMyStocks(){
        return assetSessionBeanLocal.getPortfolioStocks(portfolioId);
    }
    
    public List<Asset> getAllMyBonds(){
        return assetSessionBeanLocal.getPortfolioBonds(portfolioId);
    }
    
    public List<Asset> getAllMyFunds(){
        return assetSessionBeanLocal.getPortfolioFunds(portfolioId);
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
    
    private void createStockLineModels(Long selectedId) {

        stockLineModel = initStockModel(selectedId);
        stockLineModel.setTitle("Stock Trend");
        stockLineModel.setShowPointLabels(true);
        stockLineModel.getAxes().put(AxisType.X, new CategoryAxis("No. of Month Since the Start of Portfolio"));
        Axis yAxis = stockLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(50);
    }

    private void createFundLineModels(Long selectedId) {

        fundLineModel = initFundModel(selectedId);
        fundLineModel.setTitle("Fund Trend");
        fundLineModel.setShowPointLabels(true);
        fundLineModel.getAxes().put(AxisType.X, new CategoryAxis("No. of Month Since the Start of Portfolio"));
        Axis yAxis = fundLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(50);
    }
    
    private LineChartModel initStockModel(Long selectedId) {
        LineChartModel model = new LineChartModel();
        List<AssetTypePriceHistory> points = assetTypePriceSessionBeanLocal.getAvailablePoints(selectedId);
        
        ChartSeries stockChart = new ChartSeries();
        for (int i = 0; i < points.size(); i++) {
            String numOfMonth = String.valueOf(i);
            stockChart.set(numOfMonth, points.get(i).getUnitPrice());
        }

        model.addSeries(stockChart);

        return model;
    }

    private LineChartModel initFundModel(Long selectedId) {
        LineChartModel model = new LineChartModel();
        List<AssetTypePriceHistory> points = assetTypePriceSessionBeanLocal.getAvailablePoints(selectedId);
        
        ChartSeries stockChart = new ChartSeries();
        for (int i = 0; i < points.size(); i++) {
            String numOfMonth = String.valueOf(i);
            stockChart.set(numOfMonth, points.get(i).getUnitPrice());
        }

        model.addSeries(stockChart);

        return model;
    }
    
        public void viewStockDetails(Long selectedId) {
        RequestContext rc = RequestContext.getCurrentInstance();

        rc.execute("PF('stockDetailDialog').show();");

        createStockLineModels(selectedId);
    }

    public void viewFundDetails(Long selectedId) {
        RequestContext rc = RequestContext.getCurrentInstance();

        rc.execute("PF('fundDetailDialog').show();");
        createFundLineModels(selectedId);
    }
    
    public double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
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

    public AssetSessionBeanLocal getAssetSessionBeanLocal() {
        return assetSessionBeanLocal;
    }

    public void setAssetSessionBeanLocal(AssetSessionBeanLocal assetSessionBeanLocal) {
        this.assetSessionBeanLocal = assetSessionBeanLocal;
    }
    
}
