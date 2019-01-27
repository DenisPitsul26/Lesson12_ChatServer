import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleConnection implements Runnable {

    private MessageProvider messageProvider;
    private ArrayList<SingleConnection> connectionList;
    private List<Message> messageList;
    private int connectionId;

    public SingleConnection(MessageProvider messageProvider, ArrayList<SingleConnection> connectionList,
                            List<Message> messageList, int connectionId) {
        this.messageProvider = messageProvider;
        this.connectionList = connectionList;
        this.messageList = messageList;
        this.connectionId = connectionId;
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    private final void addToConnectionList() {
        this.connectionList.add(this);
    }

    private final void deleteFromConnectionList() {
        this.connectionList.remove(this);
    }

    @Override
    public void run() {
        addToConnectionList();
        Thread thread = Thread.currentThread();
        try {
            for (Message messageTemp : messageList){
                this.messageProvider.sendMassage(messageTemp);
            }
            while (!thread.isInterrupted()){
                Message message = this.messageProvider.readMassage();
                if (message == null){
                    throw new IOException();
                }
                if (message != null){
                    messageList.add(message);
                }
                for (SingleConnection singleConnection : connectionList) {
                    singleConnection.getMessageProvider().sendMassage(message);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            deleteFromConnectionList();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleConnection that = (SingleConnection) o;
        return connectionId == that.connectionId &&
                Objects.equals(messageProvider, that.messageProvider) &&
                Objects.equals(connectionList, that.connectionList) &&
                Objects.equals(messageList, that.messageList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageProvider, connectionList, messageList, connectionId);
    }
}







