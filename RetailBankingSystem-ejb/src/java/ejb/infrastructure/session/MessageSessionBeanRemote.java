/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.MessageBox;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface MessageSessionBeanRemote {

    public List<MessageBox> retrieveMessageBoxByCusIC(String customerIdentificationNum);

    public MessageBox addNewMessage(String fromWhere, String messageType, String subject,
            String receivedDate, String messageContent, Long customerBasicId);

    public MessageBox retrieveMessageBoxById(Long messageBoxId);

    public String deleteMessage(Long messageId);

    public void sendMessage(String fromWhere, String messageType, String subject, String receivedDate, String messageContent, Long customerBasicId);
}
