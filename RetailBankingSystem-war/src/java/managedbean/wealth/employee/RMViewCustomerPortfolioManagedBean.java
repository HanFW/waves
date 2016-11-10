/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.wealth.employee;

import ejb.wealth.entity.Portfolio;
import ejb.wealth.session.CreateFinancialGoalSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jingyuan
 */
@Named(value = "rMViewCustomerPortfolioManagedBean")
@RequestScoped
public class RMViewCustomerPortfolioManagedBean {

    /**
     * Creates a new instance of RMViewCustomerPortfolioManagedBean
     */
    @EJB
    private CreateFinancialGoalSessionBeanLocal createFinancialGoalSessionBeanLocal;

    private List<Portfolio> portfolios;

    public RMViewCustomerPortfolioManagedBean() {
    }

    public List<Portfolio> getPortfolios() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long id = (Long) context.getExternalContext().getSessionMap().get("customerBasicId");
        portfolios = createFinancialGoalSessionBeanLocal.getAllPortfoliosById(id);

        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
    
    public void redirectToAddAssetPage(Long portfolioId) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("portfolioId", portfolioId);
        
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/portfolio/RMAddPortfolio.xhtml?faces-redirect=true");
    }
    
    public void redirectToValuation(Long portfolioId) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("portfolioId", portfolioId);
        
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/wealth/RMPortfolioValuation.xhtml?faces-redirect=true");
    }

}
