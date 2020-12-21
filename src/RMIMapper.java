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

public class RMIMapper {

    public static Registry r = null;
    public static Mapper mapper;

    /**
     * Inicialize registry with given port for mapper
     * Inicialize mapper with given port and bind with registry
     * Alert client that mapper is running*/

    public RMIMapper() {}

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {

        int mapperId=0, numFiles=0, position=0;

        ObjectRegistryInterface ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
        String storageAddress=ori.getObject("2022");
        StorageInterface storageInterface= (StorageInterface)Naming.lookup(storageAddress);
        mapperId=ori.getNextId("mapper");
        position=mapperId-2025+1;
        numFiles=storageInterface.getNumFiles();


            try {
                r = LocateRegistry.createRegistry(mapperId);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                mapper = new Mapper(String.valueOf(mapperId), position);
                r.rebind("mapper", mapper);

                System.out.println("Mapper of id: " + mapperId + " ready with position "+ position);

                ori.addObject(String.valueOf(mapperId),"rmi://localhost:"+String.valueOf(mapperId)+"/mapper");


            } catch (Exception e) {
                System.out.println("Mapper server of id: " + mapperId + " main" + e.getMessage());
            }


    }
}
