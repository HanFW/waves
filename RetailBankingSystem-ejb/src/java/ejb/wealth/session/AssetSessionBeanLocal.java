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
import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface AssetSessionBeanLocal {

    public List<Fund> getAllFunds();

    public List<Bond> getAllBonds();

    public List<Stock> getAllStocks();

    public AssetType getAssetTypeById(Long assetTypeId);

    public Stock getStockById(Long assetTypeId);

    public Fund getFundById(Long assetTypeId);

    public Bond getBondById(Long assetTypeId);
    
    public Portfolio getPortfolioById (Long portfolioId);
    
    public void savePurchaseInfo(Long portfolioId, ArrayList<String[]> purchaseList);
    
    public List<Asset> getPortfolioStocks (Long portfolioId);
    
    public List<Asset> getPortfolioFunds (Long portfolioId);
    
    public List<Asset> getPortfolioBonds (Long portfolioId);
            

}
