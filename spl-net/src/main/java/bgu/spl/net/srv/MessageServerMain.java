package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiLineMessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;


public class MessageServerMain {

    public static void main(String[] args) {
// you can use any server...
        Server.threadPerClient(
                7777, //port
                () ->new BidiMessagingProtocolImpl(), //protocol factory
                () -> new BidiLineMessageEncoderDecoder() //message encoder decoder factory
        ).serve();

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();
    }
}
