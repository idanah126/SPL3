package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.BidiObjectMessageEncoderDecoder;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;


public class MessageServerMain {

    public static void main(String[] args) {
// you can use any server...
//        Server.threadPerClient(
//                7777, //port
//                () ->new BidiMessagingProtocolImpl(), //protocol factory
//                () -> new BidiObjectMessageEncoderDecoder() //message encoder decoder factory
//        ).serve();

        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () ->  new BidiMessagingProtocolImpl(), //protocol factory
                () -> new BidiObjectMessageEncoderDecoder() //message encoder decoder factory
        ).serve();
    }
}
