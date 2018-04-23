package xyz.codevomit.demostreamer.web.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author merka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SyncCommand extends BattlefieldCommand
{

	private static final long serialVersionUID = -3627218183939979310L;

	String type = "sync";
    
    Double velocityX;
    Double velocityY;
    Double velocityZ;
            
    Double x;
    Double y;
    Double z;
}
