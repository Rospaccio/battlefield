package xyz.codevomit.demostreamer.web.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author merka
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MoveCommand extends BattlefieldCommand
{

	private static final long serialVersionUID = -3071745272790486676L;
	
	String type = "move";
    Double positionX;
    Double positionY;            
    Double x;
    Double y;
}
