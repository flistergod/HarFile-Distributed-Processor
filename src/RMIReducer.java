//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIReducer {

    public static Registry r = null;
    public static Reducer reducer;

    /**
     * Inicialize registry with given port for reducer
     * Inicialize reducer with given port and bind with registry
     * Alert client that reducer is running*/

    public RMIReducer() {}

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        int reducerId=0, numFiles=0, position=0;

        ObjectRegistryInterface ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
        String storageAddress=ori.getObject("2022");
        StorageInterface storageInterface= (StorageInterface)Naming.lookup(storageAddress);
        reducerId=ori.getNextId("reducer");
        position=(ori.getNumServers("reducer"))+1;
        numFiles=storageInterface.getNumFiles();


        try {
            r = LocateRegistry.createRegistry(reducerId);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            reducer = new Reducer(String.valueOf(reducerId), position);
            r.rebind("reducer", reducer);

            System.out.println("Reducer of id: " + reducerId + " ready with position "+ position);

            ori.addObject(String.valueOf(reducerId),"rmi://localhost:"+String.valueOf(reducerId)+"/reducer");



        } catch (Exception e) {
            System.out.println("Reducer server of id: " + reducerId + " main" + e.getMessage());
        }


    }
}
