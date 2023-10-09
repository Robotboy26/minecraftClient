package dev.vili.haiku.command.commands;

import dev.vili.haiku.command.Command;
import dev.vili.haiku.utils.HaikuLogger;

import java.util.Arrays;

public class ChatCmd extends Command {

    public ChatCmd() {
        super("chat", "Converts input string to binary/hexadecimal/decimal and opens chat menu", "chat <bin/hex/dec> <message>", "c");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length < 2) {
            HaikuLogger.error(syntax);
            return;
        }

        String mode = args[0];
        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageArgs.length; i++) {
            message.append(messageArgs[i]);
            if (i != messageArgs.length - 1) {
                message.append(" ");
            }
        }

        String outputMessage;
        switch (mode) {
            case "bin":
                outputMessage = toBinaryString(message.toString());
                break;
            case "hex":
                outputMessage = toHexString(message.toString());
                break;
            case "dec":
                outputMessage = toDecimalString(message.toString());
                break;
            default:
                HaikuLogger.error("Invalid mode. Use bin, hex, or dec.");
                return;
        }

        if (outputMessage.length() > 256) {
            int numMessages = (int) Math.ceil((double) outputMessage.length() / 256);

            for (int i = 0; i < numMessages; i++) {
                int startIndex = i * 256;
                int endIndex = Math.min(startIndex + 256, outputMessage.length());
                String subMessage = outputMessage.substring(startIndex, endIndex);

                mc.player.networkHandler.sendChatMessage(subMessage);
            }
        } else {
            // mc.player.networkHandler.sendChatMessage(outputMessage);
            HaikuLogger.info(outputMessage + " copied to clipboard.");
            mc.keyboard.setClipboard(outputMessage);
        }
    }

    private String toBinaryString(String message) {
        StringBuilder binaryMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            binaryMessage.append(Integer.toBinaryString(c)).append(" ");
        }
        return binaryMessage.toString();
    }

    private String toHexString(String message) {
        StringBuilder hexMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            hexMessage.append(Integer.toHexString(c)).append(" ");
        }
        return hexMessage.toString();
    }

    private String toDecimalString(String message) {
        StringBuilder decimalMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            decimalMessage.append((int) c).append(" ");
        }
        return decimalMessage.toString();
    }
}
