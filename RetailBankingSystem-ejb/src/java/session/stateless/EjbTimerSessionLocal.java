
package session.stateless;

import javax.ejb.Local;

@Local
public interface EjbTimerSessionLocal {
    public void createTimer10000MS();
    public void cancelTimer10000MS();
    public void createTimer300000MS();
    public void cancelTimer300000MS();
}
