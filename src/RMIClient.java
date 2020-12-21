//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * index of ports:
 * 2022 - storage
 * 2024 - registry
 * 2025 - master
 * 2026 - mapper
 * 2027 - reducer
 * */

public class RMIClient {
    static Thread t;
    public RMIClient() {}

    /**
     * Inicialize RMIRegistry, RMIStorage and RMIMaster in method runServer()
     * Look for service storage and save the client's given file
     * Make master iniciate the Processing data
     * */

    public static void main(String[] args) throws InterruptedException {

        runServer();
        StorageInterface storageInterface = null;
        MasterInterface masterInterface=null;
        String fileName="www_nytimes_com";
        String filePath="C:\\Users\\nelso\\OneDrive\\Desktop\\Projecto_final_SD_20-21\\out\\dados\\";


        try {
            ObjectRegistryInterface ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
            String storageAddress=ori.getObject("2022");
            String masterAddress= ori.getObject("2024");
            storageInterface= (StorageInterface)Naming.lookup(storageAddress);
            masterInterface= (MasterInterface) Naming.lookup(masterAddress);

            storageInterface.addFile(filePath, fileName);
            System.out.println("\n\nThe har files will be proccesssed\n\n");

            masterInterface.beginProcessingData();


        } catch (RemoteException e) {

            System.out.println(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @runServer - metho to inicialize RMIRegistry, RMIStorage and RMIMaster
     * The String given in RMIMaster is the number of mappers and reducers
     * example: "3" - 3 mappers and 3 reducers
     * */
    public static void runServer() throws InterruptedException {
        int numMappers=2;
        int numReducers=4;


          t = new Thread() {
            public void run() {
                try {

                RMIRegistry.main(new String[0]);
                RMIStorage.main(new String[0]);
                for(int i=0;i<numMappers;i++){ RMIMapper.main(new String[0]); }
                for(int i=0;i<numReducers;i++){RMIReducer.main(new String[0]);}
                RMIMaster.main(new String[0]);

                } catch (RemoteException |MalformedURLException |NotBoundException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        Thread.sleep(1000L);
    }


}
