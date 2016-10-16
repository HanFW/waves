/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author aaa
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;

    @PersistenceContext
    private EntityManager em;
}
