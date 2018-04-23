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
public class AddCommand extends BattlefieldCommand
{
	private static final long serialVersionUID = 4256304998868755578L;

	String type = "add";
    Double x;
    Double y;
}
