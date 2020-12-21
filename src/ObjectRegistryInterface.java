//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created interface for services communicate with ObjectRegistry*/

public interface ObjectRegistryInterface extends Remote {
    void addObject(String Id, String address) throws RemoteException;

    String getObject(String Id) throws RemoteException;

    public int getNextId(String typeOfServer) throws RemoteException;

    public int getNumServers(String typeOfServer) throws RemoteException;

}
