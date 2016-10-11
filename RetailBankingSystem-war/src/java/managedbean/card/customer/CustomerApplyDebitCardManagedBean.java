/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerApplyDebitCardManagedBean")
@RequestScoped
public class CustomerApplyDebitCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerApplyDebitCardManagedBean
     */
    
    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;
    
    private CustomerBasic customer;
    
    public CustomerApplyDebitCardManagedBean() {
    }
    
    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }
    
    public CustomerBasic getCustomerViaSessionMap(){
        FacesContext context = FacesContext.getCurrentInstance();
        customer=(CustomerBasic)context.getExternalContext().getSessionMap().get("customer");
        
        return customer;
        
    }
    
    
}
