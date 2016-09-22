package jsf.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;



public class CacheControlPhaseListener implements PhaseListener
{
    private static final long serialVersionUID = 1L;
    
    
    
    @Override
    public PhaseId getPhaseId()
    {        
        return PhaseId.RENDER_RESPONSE;
    }
    
    
    
    @Override
    public void beforePhase(PhaseEvent event)
    {   
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSession(true);
        
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Expires", "Mon, 1 Jan 1900 0:00:00 GMT");      
    }
    
    
    
    @Override
    public void afterPhase(PhaseEvent event) 
    {
    }    
}
