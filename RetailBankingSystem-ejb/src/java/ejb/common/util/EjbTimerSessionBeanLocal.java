package ejb.common.util;

import javax.ejb.Local;

@Local
public interface EjbTimerSessionBeanLocal {

    public void createTimer10000MS();

    public void cancelTimer10000MS();

    public void createTimer5000MS();

    public void cancelTimer5000MS();

    public void createTimer300000MS();

    public void cancelTimer300000MS();

    public void createTimer15000MS();

    public void cancelTimer15000MS();

    public void createTimer70000MS();

    public void cancelTimer70000MS();

    public void createTimer2000MS();

    public void cancelTimer2000MS();

    public void createTimer30000MS();

    public void cancelTimer30000MS();

    public void createTimer300000MSDashboard();

    public void cancelTimer300000MSDashboard();
}
