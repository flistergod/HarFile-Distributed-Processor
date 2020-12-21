import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface StorageInterface extends Remote {
    /**
     * Created interface, so that the client has access to Storage class*/

    public void addFile (String path, String fileName) throws RemoteException, MalformedURLException, NotBoundException;

    public int getNumFiles() throws RemoteException;

    public void storeHashMap(LinkedHashMap<String, ArrayList<ResourceInfo>> lhm, int filecount, int placeOfOrder) throws RemoteException;

    public int getFileCount() throws RemoteException;

    public int getHashMapSize()throws RemoteException;

    public LinkedHashMap<String, ArrayList<ResourceInfo>> getHashMap() throws RemoteException;

    public int getFilesOfMapper(String mapperId) throws RemoteException;

    public String getFilePath() throws RemoteException;

    public String getFileName() throws RemoteException;

    public void sendFilesToMapper(int comeco, int fim, MapperInterface mapperInterface) throws RemoteException;

    public int getCombinationsCount() throws RemoteException;

    public void incrementCombinationsCount(int combinationsCount) throws RemoteException;

    public int getCombinationsPerReducer(String reducerId) throws RemoteException;

    public void setCombinationsPerReducer(int combinations, int numReducers) throws RemoteException, MalformedURLException, NotBoundException;

    LinkedHashMap<String, ArrayList<ResourceInfo>>  getHashMapOfReducer(String reducerId) throws RemoteException;



}
