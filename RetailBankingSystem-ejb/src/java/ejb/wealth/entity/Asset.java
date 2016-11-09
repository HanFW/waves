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
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Portfolio portfolio;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private AssetType assetType;

    private double unitsPurchased;
    private double unitPrice;
    private double gain;
    

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public double getUnitsPurchased() {
        return unitsPurchased;
    }

    public void setUnitsPurchased(double unitsPurchased) {
        this.unitsPurchased = unitsPurchased;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }


    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asset)) {
            return false;
        }
        Asset other = (Asset) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.wealth.entity.Asset[ id=" + assetId + " ]";
    }

}
