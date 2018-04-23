package xyz.codevomit.demostreamer.web;

import java.security.Principal;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.codevomit.demostreamer.conf.JavaScriptConstants;
import xyz.codevomit.demostreamer.websocket.BattlefieldMessageHelper;

/**
 *
 * @author merka / massa
 */
@Controller
@Slf4j
public class HomePageController
{
    
    @Autowired
    JavaScriptConstants javaScriptConstants;

    @Autowired
    BattlefieldMessageHelper battlefieldMessageHelper;
    
    
    @RequestMapping(value = "/conf/application_constants.js")
    @ResponseBody
    public String javaScriptConstants() throws JsonProcessingException {
    	
    	ObjectMapper mapper = new  ObjectMapper();
    	String jsonValue = mapper.writeValueAsString(javaScriptConstants);
    	
    	log.debug("javaScriptConstants: {}", jsonValue);
    	
    	return "(function(){"+ 
    		"window.BATTLEFIELD_CONSTANTS="+jsonValue + 
    	"})();";
    }
    
    @RequestMapping(value={"", "/", "/index.html", "/login"})
    public String land(Principal user)
    {
    	if(user != null){
    		return "redirect:/battlefield";
    	}
        return "index";
    }

    @RequestMapping(value={"/battlefield", "/battlefield.html"})
    public String battle(Principal user, Model model)
    {
    	if(battlefieldMessageHelper.isUserConnected(user.getName())){
    		model.addAttribute("userUnavailable", "*");
    		return "index";
    	}
        return "battlefield";
    }    
}
