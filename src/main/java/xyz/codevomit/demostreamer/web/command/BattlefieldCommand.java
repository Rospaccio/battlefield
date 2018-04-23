package xyz.codevomit.demostreamer.web.command;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author merka
 */
@Data
public class BattlefieldCommand implements Serializable
{	
	private static final long serialVersionUID = -1132085458242370463L;

	String username;
}
