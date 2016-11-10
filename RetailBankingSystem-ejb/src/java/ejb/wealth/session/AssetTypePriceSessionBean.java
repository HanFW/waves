/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import ejb.wealth.entity.AssetTypePriceHistory;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author aaa
 */
@Stateless
public class AssetTypePriceSessionBean implements AssetTypePriceSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewPrice(Double unitprice, String status,
            Integer updateYear, Integer updateMonth, String currentYear) {

        AssetTypePriceHistory price = new AssetTypePriceHistory();
        price.setCurrentYear(currentYear);
        price.setStatus(status);
        price.setUnitPrice(unitprice);
        price.setUpdateMonth(updateMonth);
        price.setUpdateYear(updateYear);
        

        entityManager.persist(price);
        entityManager.flush();

        return price.getId();
    }
    
    private double generateNextTrend(double risk, double currentValue) {
        Random rdm = new Random();
        double range = (risk * 2) * 100;
        double newChange = rdm.nextInt((int) range) - risk * 100;
        double changeWithoutPercentSign = newChange / 100;

        double newValue = (changeWithoutPercentSign / 100 + 1) * currentValue;

        return newValue;
    }
    
    @Override
    public List<AssetTypePriceHistory> getAvailablePoints(Long assetTypeId){
        Query query = entityManager.createQuery("SELECT a FROM AssetTypePriceHistory a Where a.currentYear=:currentYear AND a.assetType.assetTypeId =:assetTypeId");
        query.setParameter("currentYear", "Yes");
        query.setParameter("assetTypeId", assetTypeId);
        return query.getResultList();
    }
    
    @Override 
    public void equityMonthlyTrend(){
        Query query = entityManager.createQuery("SELECT a FROM AssetTypePriceHistory a Where a.currentYear=:currentYear AND a.status =:status");
        query.setParameter("currentYear", "Yes");
        query.setParameter("status", "New");
        List <AssetTypePriceHistory> prices;
        prices = query.getResultList();
        
        for (int i = 0; i < prices.size(); i++){
            prices.get(i).setStatus("Done");
            
            AssetTypePriceHistory newPrice = new AssetTypePriceHistory();
            
            newPrice.setAssetType(prices.get(i).getAssetType());
            newPrice.setCurrentYear("Yes");
            newPrice.setStatus("New");
            newPrice.setUpdateYear(prices.get(i).getUpdateYear());
            newPrice.setUpdateMonth(prices.get(i).getUpdateMonth() + 1);
            
            //predict new value
            Double nextPrice = generateNextTrend(prices.get(i).getAssetType().getRisk(), prices.get(i).getUnitPrice());
            newPrice.setUnitPrice(nextPrice);
            
            entityManager.persist(newPrice);
            entityManager.flush();
        }
    }
}
