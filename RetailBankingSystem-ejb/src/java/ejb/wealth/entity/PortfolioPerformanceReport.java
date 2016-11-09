/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author aaa
 */
@Entity
public class PortfolioPerformanceReport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioPerformanceReportId;
    
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Portfolio portfolio;
    
    private double stockPercentage;
    private double bondPercentage;
    private double equityPercentage;
    private double cashPercentage;
    private double fixedIncomePercentage;
    private double portfolioValuation;
    private String reportDate;

    public Long getPortfolioPerformanceReportId() {
        return portfolioPerformanceReportId;
    }

    public void setPortfolioPerformanceReportId(Long portfolioPerformanceReportId) {
        this.portfolioPerformanceReportId = portfolioPerformanceReportId;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public double getStockPercentage() {
        return stockPercentage;
    }

    public void setStockPercentage(double stockPercentage) {
        this.stockPercentage = stockPercentage;
    }

    public double getBondPercentage() {
        return bondPercentage;
    }

    public void setBondPercentage(double bondPercentage) {
        this.bondPercentage = bondPercentage;
    }

    public double getEquityPercentage() {
        return equityPercentage;
    }

    public void setEquityPercentage(double equityPercentage) {
        this.equityPercentage = equityPercentage;
    }

    public double getCashPercentage() {
        return cashPercentage;
    }

    public void setCashPercentage(double cashPercentage) {
        this.cashPercentage = cashPercentage;
    }

    public double getFixedIncomePercentage() {
        return fixedIncomePercentage;
    }

    public void setFixedIncomePercentage(double fixedIncomePercentage) {
        this.fixedIncomePercentage = fixedIncomePercentage;
    }

    public double getPortfolioValuation() {
        return portfolioValuation;
    }

    public void setPortfolioValuation(double portfolioValuation) {
        this.portfolioValuation = portfolioValuation;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (portfolioPerformanceReportId != null ? portfolioPerformanceReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PortfolioPerformanceReport)) {
            return false;
        }
        PortfolioPerformanceReport other = (PortfolioPerformanceReport) object;
        if ((this.portfolioPerformanceReportId == null && other.portfolioPerformanceReportId != null) || (this.portfolioPerformanceReportId != null && !this.portfolioPerformanceReportId.equals(other.portfolioPerformanceReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.wealth.entity.PortfolioPerformanceReport[ id=" + portfolioPerformanceReportId + " ]";
    }
    
}
