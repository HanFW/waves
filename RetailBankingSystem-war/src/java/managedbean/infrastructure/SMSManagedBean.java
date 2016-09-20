/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import ejb.infrastructure.session.SMSSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named(value = "sMSManagedBean")
@RequestScoped
public class SMSManagedBean {
    @EJB
    private SMSSessionBeanLocal sMSSessionBeanLocal;

    /**
     * Creates a new instance of SMSManagedBean
     */
    public SMSManagedBean() {
    }
    
    public void sendOTP(ActionEvent event){
        System.out.println("*** SMSManagedBean: send SMS");
        sMSSessionBeanLocal.sendOTP("customer", "83114121");
    }
}
