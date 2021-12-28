package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LineMessageEncoderDecoder implements MessageEncoderDecoder<String> {

    private byte[] bytes =null;
    private int len = 0;
    private byte[] opcode = new byte[2];
    private int opPlace=0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (bytes==null){
            opcode[opPlace++]=nextByte;
            if (opPlace==2)
                bytes=new byte[1>>10]; //1k initialized
        }
        else if (nextByte == ';') {
            return popString();
        }
        else pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private Message popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        short opcode = getOpcode(this.opcode);
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        if (opcode==1){
            return buildRegister(result);
        }
        else if (opcode==2){
            return buildLogin(result);
        }
        else if (opcode==3){
            return buildLogout(result);
        }
        else if (opcode==4){
            return buildFollow(result);
        }
        else if (opcode==5){
            return buildPost(result);
        }
        else if (opcode==6){
            return buildPM(result);
        }
        else if (opcode==7){
            return buildLogstat(result);
        }
        else if (opcode==8){
            return buildStat(result);
        }
        else if (opcode==9){
            return buildNotification(result);
        }
        else if (opcode==10){
            return buildAck(result);
        }
        else if (opcode==11){
            return buildError(result);
        }
        else if (opcode==12){
            return buildBlock(result);
        }
        return null;
    }

    private Message buildRegister(byte[] result) {

    }

    private Message buildLogin(String result) {
    }

    private Message buildLogout(String result) {
    }

    private Message buildFollow(String result) {
    }

    private Message buildPost(String result) {
    }

    private Message buildPM(String result) {
    }

    private Message buildLogstat(String result) {
    }

    private Message buildStat(String result) {
    }

    private Message buildNotification(String result) {
    }

    private Message buildAck(String result) {
    }

    private Message buildError(String result) {
    }

    private Message buildBlock(String result) {
    }

    private Short getOpcode(byte[]  byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
