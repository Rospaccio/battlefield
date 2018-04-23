package xyz.codevomit.demostreamer.web;

import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import xyz.codevomit.demostreamer.web.command.FireCommand;
import xyz.codevomit.demostreamer.web.command.MoveCommand;
import xyz.codevomit.demostreamer.web.command.SyncCommand;

/**
 *
 * @author merka
 */
@Controller
@Slf4j
public class BattlefieldController
{

    @MessageMapping("/probe-command")
    @SendTo("/topic/battlefield")
    public String manageProbeCommand(String inputMessage)
    {
        log.info("Received input message: {}", inputMessage);
        return "Output message for [" + inputMessage + "]";
    }

    @MessageMapping("/move")
    @SendTo("/topic/battlefield")
    public MoveCommand handleMoveCommandMessage(MoveCommand moveCommand)
    {
        log.debug("Move command received: {}", moveCommand);
        return moveCommand;
    }

    @MessageMapping("/fire")
    @SendTo("/topic/battlefield")
    public FireCommand handleFireCommandMessage(FireCommand fireCommand)
    {
        log.debug("Fire commnad received: {}", fireCommand);
        return fireCommand;
    }

    @MessageMapping("/sync")
    @SendTo("/topic/battlefield")
    public SyncCommand handleFireCommandMessage(SyncCommand syncCommand,
            MessageHeaders headers)
    {
        log.debug("Sync commnad received: {}", syncCommand);
        log.debug("Headers: {}:", headers);

        return syncCommand;
    }
}
