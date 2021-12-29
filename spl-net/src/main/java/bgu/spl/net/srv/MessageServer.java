package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class MessageServer implements Server<Message> {

    private final int port;
    private final Supplier<BidiMessagingProtocol<Message>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<Message>> encdecFactory;
    private ServerSocket sock;
    private ConnectionsImpl connections;


    public MessageServer(
            int port,
            Supplier<BidiMessagingProtocol<Message>> protocolFactory,
            Supplier<MessageEncoderDecoder<Message>> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
    }


    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();

                BidiMessagingProtocol newProtocol = protocolFactory.get();
                BlockingConnectionHandler handler = new BlockingConnectionHandler<Message>(
                        clientSock,
                        encdecFactory.get(),
                        newProtocol);
                int connid= connections.connect(handler);
                newProtocol.start(connid, connections);
                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<Message>  handler);

}