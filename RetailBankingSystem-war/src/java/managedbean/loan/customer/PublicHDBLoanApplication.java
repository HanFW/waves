/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "publicHDBLoanApplication")
@ViewScoped
public class PublicHDBLoanApplication implements Serializable{

    /**
     * Creates a new instance of PublicHDBLoanApplication
     */
    public PublicHDBLoanApplication() {
    }
    
}
