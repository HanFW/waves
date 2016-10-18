/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "publicDebitCardCustomerIdentityManagedBean")
@RequestScoped
public class PublicDebitCardCustomerIdentityManagedBean {

    /**
     * Creates a new instance of PublicDebitCardCustomerIdentityManagedBean
     */
    private String hasMerlionAcc;
    private boolean existingCustomerVisible;
    private boolean newCustomerVisible;

    public PublicDebitCardCustomerIdentityManagedBean() {
    }
    
     
    public void showPanel() {
        if (hasMerlionAcc.equals("Yes")) {
            existingCustomerVisible = true;
        }
        if (hasMerlionAcc.equals("No")) {
            newCustomerVisible = true;
        }
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


