package xyz.codevomit.demostreamer.web.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @author massa
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuitCommand 
	extends BattlefieldCommand {

	private static final long serialVersionUID = -6121716309781269629L;
	
	String type = "quit";
}
