/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "publicCheckRenovationExistingManagedBean")
@RequestScoped
public class PublicCheckRenovationExistingManagedBean {
    
    private String hasMerlionAcc;
    private boolean existingCustomerVisible;
    private boolean newCustomerVisible;
    

    /**
     * Creates a new instance of PublicCheckRenovationExistingManagedBean
     */
    public PublicCheckRenovationExistingManagedBean() {
    }
    
    public void showPanel(){
        existingCustomerVisible = hasMerlionAcc.equals("Yes");
        newCustomerVisible = hasMerlionAcc.equals("No");
    }

    public String getHasMerlionAcc() {
        return hasMerlionAcc;
    }

    public void setHasMerlionAcc(String hasMerlionAcc) {
        this.hasMerlionAcc = hasMerlionAcc;
    }

    public boolean isExistingCustomerVisible() {
        return existingCustomerVisible;
    }

    public void setExistingCustomerVisible(boolean existingCustomerVisible) {
        this.existingCustomerVisible = existingCustomerVisible;
    }

    public boolean isNewCustomerVisible() {
        return newCustomerVisible;
    }

    public void setNewCustomerVisible(boolean newCustomerVisible) {
        this.newCustomerVisible = newCustomerVisible;
    }
    
    
}
