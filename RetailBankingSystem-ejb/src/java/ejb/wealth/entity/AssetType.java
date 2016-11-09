/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author aaa
 */
@Entity
public class AssetType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetTypeId;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "assetType")
    private List<Asset> asset;
    
    private String assetTypeName;
    private String assetIssuerName;
    private double risk;
    private double currentValue;
    

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public List<Asset> getAsset() {
        return asset;
    }

    public void setAsset(List<Asset> asset) {
        this.asset = asset;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }
    
    public void addAsset(Asset a) {
        asset.add(a);
    }

    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }


    public String getAssetIssuerName() {
        return assetIssuerName;
    }

    public void setAssetIssuerName(String assetIssuerName) {
        this.assetIssuerName = assetIssuerName;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
    
    



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetTypeId != null ? assetTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssetType)) {
            return false;
        }
        AssetType other = (AssetType) object;
        if ((this.assetTypeId == null && other.assetTypeId != null) || (this.assetTypeId != null && !this.assetTypeId.equals(other.assetTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.wealth.entity.AssetType[ id=" + assetTypeId + " ]";
    }
    
}
