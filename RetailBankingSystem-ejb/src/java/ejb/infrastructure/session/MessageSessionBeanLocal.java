package ejb.infrastructure.session;

import ejb.deposit.entity.MessageBox;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MessageSessionBeanLocal {
    public List<MessageBox> retrieveMessageBoxByCusIC(String customerIdentificationNum);
    public MessageBox addNewMessage(String fromWhere,String messageType,String subject,
            String receivedDate,String messageContent,Long customerBasicId);
    public MessageBox retrieveMessageBoxById(Long messageBoxId);
    public String deleteMessage(Long messageId);
    public void sendMessage(String fromWhere, String messageType, String subject, String receivedDate, String messageContent, Long customerBasicId);
}
