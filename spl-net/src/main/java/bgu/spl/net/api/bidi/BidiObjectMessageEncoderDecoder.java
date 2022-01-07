package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.messages.Error;
import bgu.spl.net.srv.messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BidiObjectMessageEncoderDecoder implements MessageEncoderDecoder<Message> {

    private final byte[] opcode = new byte[2];
    private byte[] bytes = null;
    private int len = 0;
    private int opPlace = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (bytes == null) {
            opcode[opPlace++] = nextByte;
            if (opPlace == 2)
                bytes = new byte[1 >> 10]; //1k initialized
        } else if (nextByte == ';') {
            return popString();
        } else pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Message message) {
        if (message.getClass() == Notification.class){
            return encodeNotification((Notification) message);
        }
        else if (message.getClass() == Ack.class){
            return encodeAck((Ack) message);
        }
        else if (message.getClass() == Error.class){
            return encodeError((Error) message);
        }
        return null;
    }

    private byte[] encodeNotification(Notification notification) {
        List<Byte> bytes = new LinkedList<>();
        byte zero = 0;
        byte end = ';';
        short opcode = 9;
        byte notificationType;
        if (notification.isPublic)
            notificationType = 1;
        else
            notificationType = 0;
        byte[] opcodeByte = shortToBytes(opcode);
        bytes.add(opcodeByte[0]); bytes.add(opcodeByte[1]); bytes.add(notificationType);
        String postingUser = notification.postingUser;
        byte[] postingUserBytes = postingUser.getBytes(StandardCharsets.UTF_8);
        for (byte postingUserByte : postingUserBytes) {
            bytes.add(postingUserByte);
        }
        bytes.add(zero);
        String content = notification.content;
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        for (byte contentByte : contentBytes) {
            bytes.add(contentByte);
        }
        bytes.add(zero);
        bytes.add(end);
        byte[] returnBytes = new byte[bytes.size()];
        for (int i = 0; i < returnBytes.length; i++) {
            returnBytes[i] = bytes.get(i);
        }
        return returnBytes;
    }

    private byte[] encodeAck(Ack ack) {
        List<Byte> bytes = new LinkedList<>();
        byte zero = 0;
        byte end = ';';
        short opcode = 10;
        short messageOpcode = (short) ack.messageOpcode;
        byte[] opcodeBytes = shortToBytes(opcode);
        byte[] messageOpcodeBytes = shortToBytes(messageOpcode);
        bytes.add(opcodeBytes[0]); bytes.add(opcodeBytes[1]); bytes.add(messageOpcodeBytes[0]); bytes.add(messageOpcodeBytes[1]);
        if (messageOpcode == 4){
            String userName = ack.optional;
            byte[] userNameBytes = userName.getBytes(StandardCharsets.UTF_8);
            for (byte userNameByte : userNameBytes) {
                bytes.add(userNameByte);
            }
            bytes.add(zero);
        }
        else if (messageOpcode == 7 || messageOpcode == 8){
            String stats = ack.optional;
            short age = (short) Integer.parseInt(stats.substring(0, stats.indexOf(" ")));
            stats=stats.substring(stats.indexOf(" ")+1);
            short numPosts = (short) Integer.parseInt(stats.substring(0, stats.indexOf(" ")));
            stats=stats.substring(stats.indexOf(" ")+1);
            short numFollowers = (short) Integer.parseInt(stats.substring(0, stats.indexOf(" ")));
            stats=stats.substring(stats.indexOf(" ")+1);
            short numFollowing = (short) Integer.parseInt(stats);

            byte[] ageBytes = shortToBytes(age); byte[] numPostsBytes = shortToBytes(numPosts);
            byte[] numFollowersBytes = shortToBytes(numFollowers); byte[] numFollowingBytes = shortToBytes(numFollowing);
            bytes.add(ageBytes[0]); bytes.add(ageBytes[1]); bytes.add(numPostsBytes[0]); bytes.add(numPostsBytes[1]);
            bytes.add(numFollowersBytes[0]); bytes.add(numFollowersBytes[1]); bytes.add(numFollowingBytes[0]); bytes.add(numFollowingBytes[1]);
        }
        bytes.add(end);
        byte[] returnBytes = new byte[bytes.size()];
        for (int i = 0; i < returnBytes.length; i++) {
            returnBytes[i] = bytes.get(i);
        }
        return returnBytes;
    }

    private byte[] encodeError(Error error) {
        List<Byte> bytes = new LinkedList<>();
        byte end = ';';
        short opcode = 11;
        short messageOpcode = (short) error.msgOpcode;
        byte[] opcodeBytes = shortToBytes(opcode);
        byte[] messageOpcodeBytes = shortToBytes(messageOpcode);
        bytes.add(opcodeBytes[0]); bytes.add(opcodeBytes[1]); bytes.add(messageOpcodeBytes[0]); bytes.add(messageOpcodeBytes[1]);
        bytes.add(end);
        byte[] returnBytes = new byte[bytes.size()];
        for (int i = 0; i < returnBytes.length; i++) {
            returnBytes[i] = bytes.get(i);
        }
        return returnBytes;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, (len+1) * 2);
        }
        bytes[len++] = nextByte;
    }

    private Message popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        short opcode = bytesToShort(this.opcode);
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0; opPlace = 0;
        bytes= null;
        if (opcode == 1) {
            return buildRegister(result);
        } else if (opcode == 2) {
            return buildLogin(result);
        } else if (opcode == 3) {
            return buildLogout();
        } else if (opcode == 4) {
            return buildFollow(result);
        } else if (opcode == 5) {
            return buildPost(result);
        } else if (opcode == 6) {
            return buildPM(result);
        } else if (opcode == 7) {
            return buildLogstat();
        } else if (opcode == 8) {
            return buildStat(result);
        } else if (opcode == 9) {
            return buildNotification(result);
        } else if (opcode == 10) {
            return buildAck(result);
        } else if (opcode == 11) {
            return buildError(result);
        } else if (opcode == 12) {
            return buildBlock(result);
        }
        return null;
    }

    private Message buildRegister(String result) {
        StringBuilder username = new StringBuilder();
        int index = 0;
        while (result.charAt(index) != '\0') {
            username.append(result.charAt(index));
            index += 1;
        }
        StringBuilder password = new StringBuilder();
        index += 1;
        while (result.charAt(index) != '\0') {
            password.append(result.charAt(index));
            index += 1;
        }
        StringBuilder birthday = new StringBuilder();
        index += 1;
        while (result.charAt(index) != '\0') {
            birthday.append(result.charAt(index));
            index += 1;
        }
        return new Register(username.toString(), password.toString(), birthday.toString());
    }

    private Message buildLogin(String result) {
        StringBuilder username = new StringBuilder();
        int index = 0;
        while (result.charAt(index) != '\0') {
            username.append(result.charAt(index));
            index += 1;
        }
        StringBuilder password = new StringBuilder();
        index += 1;
        while (result.charAt(index) != '\0') {
            password.append(result.charAt(index));
            index += 1;
        }
        char captcha;
        index += 1;
        captcha = result.charAt(index);
        boolean captchaBool = captcha == '1';
        return new Login(username.toString(), password.toString(), captchaBool);
    }

    private Message buildLogout() {
        return new Logout();
    }

    private Message buildFollow(String result) {
        char unFollow = result.charAt(0);
        boolean unFollowBool = unFollow == '1';
        String username = result.substring(1);
        return new Follow(unFollowBool, username.toString());
    }

    private Message buildPost(String result) {
        StringBuilder content = new StringBuilder();
        int index = 0;
        while (result.charAt(index) != '\0') {
            content.append(result.charAt(index));
            index += 1;
        }
        return new Post(content.toString());
    }

    private Message buildPM(String result) {
        StringBuilder username = new StringBuilder();
        int index = 0;
        while (result.charAt(index) != '\0') {
            username.append(result.charAt(index));
            index += 1;
        }
        StringBuilder content = new StringBuilder();
        index += 1;
        while (result.charAt(index) != '\0') {
            content.append(result.charAt(index));
            index += 1;
        }
        StringBuilder dateAndTime = new StringBuilder();
        index += 1;
        while (result.charAt(index) != '\0') {
            dateAndTime.append(result.charAt(index));
            index += 1;
        }
        return new PM(username.toString(), content.toString(), dateAndTime.toString());
    }

    private Message buildLogstat() {
        return new Logstat();
    }

    private Message buildStat(String result) {
        List<String> usernames = new LinkedList<>();
        StringBuilder username = new StringBuilder();
        int index = 0;
        while (result.charAt(index) != '\0') {
            if (result.charAt(index) == '|') {
                usernames.add(username.toString());
                username = new StringBuilder();
            } else {
                username.append(result.charAt(index));
            }
            index += 1;
        }
        usernames.add(username.toString());
        return new Stat(usernames);
    }

    private Message buildNotification(String result) {
        char isPublic = result.charAt(0);
        boolean isPublicBool = isPublic == '1';
        StringBuilder postingUser = new StringBuilder();
        int index = 1;
        while (result.charAt(index) != '\0') {
            postingUser.append(result.charAt(index));
            index += 1;
        }
        StringBuilder content = new StringBuilder();
        index += 1;
        while (result.charAt(index) != '\0') {
            content.append(result.charAt(index));
            index += 1;
        }
        return new Notification(isPublicBool, postingUser.toString(), content.toString());
    }

    private Message buildAck(String result) {
        int messageOpcode = Integer.parseInt(result.substring(0, 2));
        if (result.length() > 2) {
            StringBuilder optional = new StringBuilder();
            int index = 2;
            while (result.length() > index) {
                optional.append(result.charAt(index));
                index += 1;
            }
            return new Ack(messageOpcode, optional.toString());
        }
        return new Ack(messageOpcode);
    }

    private Message buildError(String result) {
        int messageOpcode = Integer.parseInt(result.substring(0, 2));
        return new Error(messageOpcode);
    }

    private Message buildBlock(String result) {
        StringBuilder username = new StringBuilder();
        int index = 0;
        while (result.charAt(index) != '\0') {
            username.append(result.charAt(index));
            index += 1;
        }
        return new Block(username.toString());
    }

    private Short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    private byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
