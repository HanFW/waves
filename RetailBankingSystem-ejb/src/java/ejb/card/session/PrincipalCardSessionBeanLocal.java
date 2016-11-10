package ejb.card.session;

import ejb.card.entity.PrincipalCard;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PrincipalCardSessionBeanLocal {

    public PrincipalCard retrieveCardById(Long cardId);
    
    public List<PrincipalCard> retrievePrincipalCardByCusIC(String customerIdentificationNum);
}
