import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    ArrayList<SingleConnection> connectionList = new ArrayList<>();
    List<Message> messageList = Collections.synchronizedList(new ArrayList<>());

    public Server() {
    }

    private boolean init() {
        try {
            this.serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void start() {
        boolean startInit = this.init();
        if (!startInit) {
            System.out.println("Server initialization failed...");
            return;
        }
        System.out.println("Server start");
        int i = 0;
        for (;;){
            try {
                Socket socket = this.serverSocket.accept();
                MessageProviderImpl messageProviderImpl = new MessageProviderImpl();
                messageProviderImpl.setSocket(socket);
                SingleConnection singleConnection = new SingleConnection(messageProviderImpl, connectionList, messageList, i++);
                Thread threadSingleConnection = new Thread(singleConnection);
                threadSingleConnection.setDaemon(true);
                threadSingleConnection.start();
            }
            catch (IOException e){
                System.out.println("Server stop");
                return;
            }
        }

    }

}








