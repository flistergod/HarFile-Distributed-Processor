import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface MapperInterface extends Remote {


    public void FillResourcesMap(String path, String fileName)
            throws harreader.HarReaderException, RemoteException, MalformedURLException, NotBoundException;

    public void getDataFromStorage(String Name,  byte [] mydata, int mylen) throws RemoteException;

}
