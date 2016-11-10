package ejb.common.util;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

@Singleton
@LocalBean

public class demoSessionBean {

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionLocal;
    
    public void create10000Timer() {
        ejbTimerSessionLocal.createTimer10000MS();
    }
    
    public void cancel10000Timer() {
        ejbTimerSessionLocal.cancelTimer10000MS();
    }
    
    public void create300000Timer() {
        ejbTimerSessionLocal.createTimer300000MS();
    }
    
    public void cancel300000Timer() {
        ejbTimerSessionLocal.cancelTimer300000MS();
    }
    
    public void create25000Timer() {
        ejbTimerSessionLocal.createTimer15000MS();
    }
    
    public void cancel25000Timer() {
        ejbTimerSessionLocal.cancelTimer15000MS();
    }
    
    public void create70000Timer() {
        ejbTimerSessionLocal.createTimer70000MS();
    }
    
    public void cancel70000Timer() {
        ejbTimerSessionLocal.cancelTimer70000MS();
    }
    
    public void create2000Timer() {
        ejbTimerSessionLocal.createTimer2000MS();
    }
    
    public void cancel2000Timer() {
        ejbTimerSessionLocal.cancelTimer2000MS();
    }
    
    public void create5000Timer() {
        ejbTimerSessionLocal.createTimer5000MS();
    }
    
    public void cancel5000Timer() {
        ejbTimerSessionLocal.cancelTimer5000MS();
    }
    
    public void create30000Timer() {
        ejbTimerSessionLocal.createTimer30000MS();
    }
    
    public void cancel30000Timer() {
        ejbTimerSessionLocal.cancelTimer30000MS();
    }
    
    public void create300000DashboardTimer() {
        ejbTimerSessionLocal.createTimer300000MSDashboard();
    }
    
    public void cancel300000DashboardTimer() {
        ejbTimerSessionLocal.cancelTimer300000MSDashboard();
    }
}
