//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIStorage {

    public static Registry r = null;
    public static Storage storage;
    public static Master master;

    /**
     * Inicialize registry with port 2022
     * Inicialize storage, rebind storage on registry and create the name of the service (storage)
     * Alert clientto that service Storage is ready*/

    public RMIStorage() {}

    public static void main(String[] args) {

        try {
            r = LocateRegistry.createRegistry(2022);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
             storage = new Storage();
            r.rebind("storage", storage);
            System.out.println("Storage ready");

            ObjectRegistryInterface ori= (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
            ori.addObject("2022","rmi://localhost:2022/storage");

        } catch (Exception e) {
            System.out.println("Storage server main " + e.getMessage());
        }





    }
}
