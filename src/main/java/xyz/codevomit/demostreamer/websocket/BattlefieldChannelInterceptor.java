package xyz.codevomit.demostreamer.websocket;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

/**
 * 
 * @author massa
 *
 */
@Slf4j
@Component
public class BattlefieldChannelInterceptor extends ChannelInterceptorAdapter {

	@Autowired @Lazy
    private BattlefieldMessageHelper battlefieldMessageHelper;
    
        
	/* (non-Javadoc)
	 * @see org.springframework.messaging.support.ChannelInterceptorAdapter#postSend(org.springframework.messaging.Message, org.springframework.messaging.MessageChannel, boolean)
	 */
	@Override
	public void postSend(Message<?> message, MessageChannel channel,
			boolean sent) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
		
		// ignore non-STOMP messages
		if(sha.getCommand() == null) {
			return;
		}		
		
		String sessionId = sha.getSessionId();
		String userName = sha.getUser().getName();
		String destination = sha.getDestination();
		
        switch(sha.getCommand()) {
        case SUBSCRIBE:
        	//
        	// add user to arena on topic subscribe
        	//
        	log.debug("STOMP Subscribe [sessionId: {}, userName: {}, destination: {}]", sessionId, userName, destination);
        	if(BattlefieldMessageHelper.BATTLE_TOPIC_ADDRESS.equalsIgnoreCase(destination)){
        		battlefieldMessageHelper.addUser(userName);
        	}
            break;
            
        case DISCONNECT:
        	//
        	// remove user from arena on socket disconnect
        	//
        	log.debug("STOMP Disconnect [sessionId: {}, userName: {}]", sessionId, userName);
        	battlefieldMessageHelper.removeUser(userName);
            break;

        default:
            break;
        } 
	}
    
    
	
}
