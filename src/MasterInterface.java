import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterInterface extends Remote {

    public void beginProcessingData() throws RemoteException, MalformedURLException, NotBoundException, MalformedURLException, NotBoundException, harreader.HarReaderException;


}
