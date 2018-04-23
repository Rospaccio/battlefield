package xyz.codevomit.demostreamer.conf;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import xyz.codevomit.demostreamer.websocket.BattlefieldChannelInterceptor;
import xyz.codevomit.demostreamer.websocket.BattlefieldHandshakeInterceptor;

/**
 *
 * @author merka / massa
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
{

    @Value("${server.contextPath}")
    String serverContextPath;

    @Autowired
    @Getter @Setter private BattlefieldChannelInterceptor battlefieldChannelInterceptor;
    
    @Autowired
    @Getter @Setter private BattlefieldHandshakeInterceptor battlefieldHandshakeInterceptor;

            
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry)
    {
        stompEndpointRegistry.addEndpoint("/battlefield").withSockJS()
                .setInterceptors(battlefieldHandshakeInterceptor);
        
        log.info("Registration of endpoit '/battlefield': DONE");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry)
    {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes(serverContextPath + "/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
    	log.debug("battlefieldChannelInterceptor: {}", battlefieldChannelInterceptor);
    	registration.setInterceptors(battlefieldChannelInterceptor);
    }    
}
