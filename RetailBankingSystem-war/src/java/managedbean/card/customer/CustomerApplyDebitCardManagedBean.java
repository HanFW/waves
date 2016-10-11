/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerApplyDebitCardManagedBean")
@Dependent
public class CustomerApplyDebitCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerApplyDebitCardManagedBean
     */
    
    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;
    
    public CustomerApplyDebitCardManagedBean() {
    }
    
    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }
}
