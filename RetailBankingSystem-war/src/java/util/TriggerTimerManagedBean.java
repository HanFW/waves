package util;

import ejb.common.util.EjbTimerSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "triggerTimerManagedBean")
@RequestScoped

public class TriggerTimerManagedBean {
    
    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    public TriggerTimerManagedBean() {
    }
    
    public void createOneDayTimer() {
        ejbTimerSessionBeanLocal.createTimer10000MS();
    }
    
    public void cancelOneDayTimer() {
        ejbTimerSessionBeanLocal.cancelTimer10000MS();
    }
    
    public void createOneMonthTimer() {
        ejbTimerSessionBeanLocal.createTimer300000MS();
    }
    
    public void cancelOneMonthTimer() {
        ejbTimerSessionBeanLocal.cancelTimer300000MS();
    }
    
    public void createDailyTransferTimer() {
        ejbTimerSessionBeanLocal.createTimer15000MS();
    }
    
    public void cancelDailyTransferTimer() {
        ejbTimerSessionBeanLocal.cancelTimer15000MS();
    }
}
