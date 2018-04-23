package xyz.codevomit.demostreamer.web.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author merka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FireCommand extends BattlefieldCommand
{

	private static final long serialVersionUID = -8152494571704763169L;
	
	String type = "fire";
    Long x;
    Long y;
    Long z;
}
