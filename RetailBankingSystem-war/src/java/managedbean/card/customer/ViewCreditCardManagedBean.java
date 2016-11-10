package managedbean.card.customer;

import ejb.card.entity.PrincipalCard;
import ejb.card.session.PrincipalCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "viewCreditCardManagedBean")
@RequestScoped

public class ViewCreditCardManagedBean {

    @EJB
    private PrincipalCardSessionBeanLocal principalCardSessionBeanLocal;

    private ExternalContext ec;

    public ViewCreditCardManagedBean() {
    }

    public List<PrincipalCard> getCreditCard() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<PrincipalCard> principalCards = principalCardSessionBeanLocal.retrievePrincipalCardByCusIC(customerBasic.getCustomerIdentificationNum());

        return principalCards;
    }

}
