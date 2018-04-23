package xyz.codevomit.demostreamer.websocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

import xyz.codevomit.demostreamer.web.command.AddCommand;
import xyz.codevomit.demostreamer.web.command.QuitCommand;

/**
 * 
 * @author massa
 *
 */
@Slf4j
@Component
public class BattlefieldMessageHelper {

	private final Set<String> connectedUser;
	
	private final MessageSendingOperations<String> messagingTemplate;

	@Autowired
	public BattlefieldMessageHelper(MessageSendingOperations<String> messagingTemplate) {
		connectedUser = new CopyOnWriteArraySet<String>();
		this.messagingTemplate = messagingTemplate;
	}
		

	public boolean addUser(String userName, Double x, Double y){
		
		// check if user is already added
		if(isUserConnected(userName)){
			return false;
		}

		AddCommand cmd = new AddCommand();
		cmd.setUsername(userName);
		cmd.setX(x);
		cmd.setY(x);
		send(cmd);

		connectedUser.add(userName.toUpperCase());
		return true;
	}
	
	

	public void addUser(String userName){
		addUser(userName, 0d, 0d);
	}
	

	public void removeUser(String userName){
		
		if(isUserConnected(userName)){
			connectedUser.remove(userName.toUpperCase());
		}
		
		QuitCommand cmd = new QuitCommand();
		cmd.setUsername(userName);
		send(cmd);
	}
	

	public boolean isUserConnected(String userName){
		return connectedUser.contains(userName.toUpperCase());
	}
	

	
	/**
	 * Send message to battlefield topic
	 * 
	 * @param payload
	 * 		message to send
	 */
	protected void send(Object payload) {
		log.debug("Sending message to {}: {}", BATTLE_TOPIC_ADDRESS, payload);
		messagingTemplate.convertAndSend(BATTLE_TOPIC_ADDRESS, payload);
	}
	
	/**
	 * battlefield topic address
	 */
	public final static String BATTLE_TOPIC_ADDRESS = "/topic/battlefield";		
}
