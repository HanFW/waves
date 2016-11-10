/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import ejb.wealth.entity.AssetTypePriceHistory;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface AssetTypePriceSessionBeanLocal {
    public Long addNewPrice(Double unitprice, String status,
            Integer updateYear, Integer updateMonth, String currentYear);
    
    public void equityMonthlyTrend();
    
    public List<AssetTypePriceHistory> getAvailablePoints(Long assetTypeId);
}
