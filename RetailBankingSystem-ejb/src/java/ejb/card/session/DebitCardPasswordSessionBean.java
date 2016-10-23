/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.DebitCard;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class DebitCardPasswordSessionBean implements DebitCardPasswordSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private DebitCardPasswordSessionBeanLocal debitCardPasswordSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    //customer forgets debit catf pwd
    @Override
    public void setPassword(String debitCardPwd, String debitCardNum) {
        DebitCard findDebitCard = getCardByCardNum(debitCardNum);
        try {
            String hashedPwd = md5Hashing(debitCardPwd + findDebitCard.getCardNum().substring(0, 3));
            findDebitCard.setDebitCardPwd(hashedPwd);
            em.flush();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardPasswordSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    //customer changes debit card pwd
    @Override
    public String changePassword(String currentDebitCardPwd, String debitCardPwd, String debitCardNum){
        DebitCard findDebitCard = getCardByCardNum(debitCardNum);
        
        try{
        String hashedCurrentPwd =md5Hashing(currentDebitCardPwd + findDebitCard.getCardNum().substring(0, 3));
        if(!hashedCurrentPwd.equals(findDebitCard.getDebitCardPwd()))
            return "wrong current pwd";
        else{
            String hashedPwd = md5Hashing(debitCardPwd + findDebitCard.getCardNum().substring(0, 3));
            findDebitCard.setDebitCardPwd(hashedPwd);
            em.flush();
            return "success";
        }
        }catch(NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardPasswordSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private DebitCard getCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCard findDebitCard = (DebitCard) query.getResultList().get(0);
            return findDebitCard;
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
