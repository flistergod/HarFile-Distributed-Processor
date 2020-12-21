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

public class RMIMaster {

    public static Registry r = null;
    public static Master master;


    /**
     * Inicialize registry with port 2023
     * Inicialize master with port 2024 and given number of mappers and reducers
     * Alert client that master is running*
     * */

    public RMIMaster() {}

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {


        try {
            r = LocateRegistry.createRegistry(2024);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {

            master = new Master();
            r.rebind("master", master);
            System.out.println("Master ready");

            ObjectRegistryInterface ori= (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
            ori.addObject("2024","rmi://localhost:2024/master");


        } catch (Exception e) {
            System.out.println("Master server main " + e.getMessage());
        }

    }
}
