//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Inicialize registry with port 2023
 * Inicialize Objectregistry  and bind with registry
 * Alert client that Objectregistry is running*/

public class RMIRegistry {
    public RMIRegistry() {}

    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(2023);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            ObjectRegistry objectRegistry = new ObjectRegistry();
            r.rebind("registry", objectRegistry);
            System.out.println("Object registry ready");
        } catch (Exception e) {
            System.out.println("Object registry main " + e.getMessage());
        }

    }
}
