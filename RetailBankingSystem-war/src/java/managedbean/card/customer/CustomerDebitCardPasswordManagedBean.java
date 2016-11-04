/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerDebitCardPasswordManagedBean")
@ViewScoped
public class CustomerDebitCardPasswordManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerDebitCardPasswordManagedBean
     */
    private String debitCardPassword;

    public CustomerDebitCardPasswordManagedBean() {
    }

}
