package ejb.infrastructure.session;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.infrastructure.entity.MessageBox;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MessageSessionBean implements MessageSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MessageBox addNewMessage(String fromWhere, String messageType, String subject, String receivedDate, String messageContent, Long customerBasicId) {

        MessageBox messageBox = new MessageBox();

        messageBox.setFromWhere(fromWhere);
        messageBox.setMessageType(messageType);
        messageBox.setSubject(subject);
        messageBox.setMessageContent(messageContent);
        messageBox.setReceivedDate(receivedDate);
        messageBox.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(messageBox);
        entityManager.flush();

        return messageBox;
    }

    @Override
    public List<MessageBox> retrieveMessageBoxByCusIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<MessageBox>();
        }
        try {
            Query query = entityManager.createQuery("Select m From MessageBox m Where m.customerBasic =:customerBasic");
            query.setParameter("customerBasic", customerBasic);
            System.out.println("////////////list size = " + query.getResultList().size());
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new ArrayList<MessageBox>();
        }
    }

    @Override
    public MessageBox retrieveMessageBoxById(Long messageId) {
        MessageBox messageBox = new MessageBox();

        try {
            Query query = entityManager.createQuery("Select m From MessageBox m Where m.messageId=:messageId");
            query.setParameter("messageId", messageId);

            if (query.getResultList().isEmpty()) {
                return new MessageBox();
            } else {
                messageBox = (MessageBox) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new MessageBox();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return messageBox;
    }

    @Override
    public String deleteMessage(Long messageId) {
        MessageBox message = retrieveMessageBoxById(messageId);

        entityManager.remove(message);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public void sendMessage(String fromWhere, String messageType, String subject, String receivedDate, String messageContent, Long customerBasicId) {
        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        MessageBox messageBox = addNewMessage(fromWhere, messageType, subject, receivedDate, messageContent, customerBasicId);
        customerBasic.getMessageBox().add(messageBox);
    }
}
