package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.messages.Message;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.function.Supplier;

public interface Server<T> extends Closeable {

    /**
     * The main loop of the server, Starts listening and handling new clients.
     */
    void serve();

    /**
     *This function returns a new instance of a thread per client pattern server
     * @param port The port for the server socket
     * @param protocolFactory A factory that creats new MessagingProtocols
     * @param encoderDecoderFactory A factory that creats new MessageEncoderDecoder
     * @return A new Thread per client server
     */
    public static Server<Message>  threadPerClient(
            Supplier<LinkedList<Integer>> a,
            int port,
            Supplier<BidiMessagingProtocolImpl> protocolFactory,
            Supplier<LineMessageEncoderDecoder> encoderDecoderFactory) {

        return new BaseServer(a, port, protocolFactory, encoderDecoderFactory) {
            @Override
            protected void execute(BlockingConnectionHandler<Message>  handler) {
                new Thread(handler).start();
            }
        };

    }

    /**
     * This function returns a new instance of a reactor pattern server
     * @param nthreads Number of threads available for protocol processing
     * @param port The port for the server socket
     * @param protocolFactory A factory that creats new MessagingProtocols
     * @param encoderDecoderFactory A factory that creats new MessageEncoderDecoder
     * @param <T> The Message Object for the protocol
     * @return A new reactor server
     */
    public static <T> Server<T> reactor(
            int nthreads,
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encoderDecoderFactory) {
        return new Reactor<T>(nthreads, port, protocolFactory, encoderDecoderFactory);
    }

}
