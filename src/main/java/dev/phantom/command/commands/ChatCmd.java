package dev.phantom.command.commands;

import dev.phantom.command.Command;
import dev.phantom.utils.PhantomLogger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ChatCmd extends Command {

    public ChatCmd() {
        super("chat", "Converts input string to binary/hexadecimal/decimal and opens chat menu", "chat <bin/hex/dec> <message>", "c");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length < 2) {
            PhantomLogger.error(syntax);
            return;
        }

        String mode = args[0];
        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        String message = String.join(" ", messageArgs);

        String outputMessage;
        switch (mode) {
            case "bin":
                outputMessage = getByteString(message);
                break;
            case "hex":
                outputMessage = toHexString(message);
                break;
            case "dec":
                outputMessage = toDecimalString(message);
                break;
            default:
                PhantomLogger.error("Invalid mode. Use bin, hex, or dec.");
                return;
        }

        if (outputMessage != null) {
            // Split the message into chunks of 256 characters and send each chunk as a separate message
            int chunkSize = 256;
            int numChunks = (int) Math.ceil((double) outputMessage.length() / chunkSize);
            for (int i = 0; i < numChunks; i++) {
                int startIndex = i * chunkSize;
                int endIndex = Math.min((i + 1) * chunkSize, outputMessage.length());
                String chunk = outputMessage.substring(startIndex, endIndex);
                mc.getNetworkHandler().sendChatMessage(chunk);
            }
        }
    }

    private String toHexString(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        StringBuilder hexMessage = new StringBuilder();
        for (byte b : bytes) {
            hexMessage.append(String.format("%02X", b & 0xFF)).append(" ");
        }
        return hexMessage.toString();
    }

    private String toDecimalString(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        StringBuilder decimalMessage = new StringBuilder();
        for (byte b : bytes) {
            decimalMessage.append((int) (b & 0xFF)).append(" ");
        }
        return decimalMessage.toString();
    }
    private String getByteString(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        StringBuilder binaryMessage = new StringBuilder();
        for (byte b : bytes) {
            binaryMessage.append(Integer.toBinaryString(b & 0xFF)).append(" ");
        }
        return binaryMessage.toString();
    }
}
