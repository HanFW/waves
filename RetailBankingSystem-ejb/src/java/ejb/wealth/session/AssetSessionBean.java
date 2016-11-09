/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import ejb.wealth.entity.Asset;
import ejb.wealth.entity.AssetType;
import ejb.wealth.entity.Bond;
import ejb.wealth.entity.Fund;
import ejb.wealth.entity.Portfolio;
import ejb.wealth.entity.Stock;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author aaa
 */
@Stateless
public class AssetSessionBean implements AssetSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    
    @Override 
    public List<Fund> getAllFunds (){   
        Query query = em.createQuery("SELECT f FROM Fund f WHERE f.assetTypeName = :assetTypeName");
        query.setParameter("assetTypeName", "Fund");
        return query.getResultList();
    }
    
     @Override 
    public List<Bond> getAllBonds (){
        Query query = em.createQuery("SELECT a FROM Bond a");
        return query.getResultList();
    }
    
     @Override 
    public List<Stock> getAllStocks (){
        Query query = em.createQuery("SELECT a FROM Stock a");
        return query.getResultList();
    }
    
    @Override
    public AssetType getAssetTypeById (Long assetTypeId){
        AssetType assetType = em.find(AssetType.class, assetTypeId);
        return assetType;
    }
    
    @Override
    public Stock getStockById (Long assetTypeId){
        Stock stock = em.find(Stock.class, assetTypeId);
        return stock;
    }
    
    @Override
    public Fund getFundById (Long assetTypeId){
        Fund fund= em.find(Fund.class, assetTypeId);
        return fund;
    }
    
    @Override
    public Bond getBondById (Long assetTypeId){
        Bond bond = em.find(Bond.class, assetTypeId);
        return bond;
    }
    
    @Override
    public Portfolio getPortfolioById (Long portfolioId) {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        return portfolio;
    }
    
    @Override
    public void savePurchaseInfo(Long portfolioId, ArrayList<String[]> purchaseList) {
        int purchaseNum = purchaseList.size();
        for (int i = 0; i < purchaseNum; i++) {
            String[] array = new String[5];
            array = purchaseList.get(i);
            Long typeId = Long.valueOf(array[0]);
            double unitsPurchased = Double.valueOf(array[3]);
            double unitPrice = Double.valueOf(array[2]);
            Asset asset = new Asset ();
            
            asset.setUnitPrice(unitPrice);
            asset.setUnitsPurchased(unitsPurchased);
            
            AssetType assetType = em.find(AssetType.class, typeId);
            asset.setAssetType(assetType);
            assetType.addAsset(asset);
            
            Portfolio portfolio = em.find(Portfolio.class, portfolioId);
            asset.setPortfolio(portfolio);
            portfolio.addAsset(asset);
            
            em.flush();
        }
    }
    
   
    
}
