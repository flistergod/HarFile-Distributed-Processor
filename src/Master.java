import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Master extends UnicastRemoteObject implements MasterInterface {

    /**
     * Master will monotorize and coordinate Mappers and reducers
     * Master will comunicate with client and execute the client tasks
     * Master will give the client the processed data of client's given file*/

    ObjectRegistryInterface l2 = null;
    private static final long serialVersionUID = 1L;
    ArrayList<String> mappers = new ArrayList();
    ArrayList<String> reducers = new ArrayList();

    protected Master() throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {

        ObjectRegistryInterface ori= (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
        int numMappers=ori.getNumServers("mapper");
        int numReducers=ori.getNumServers("reducer");

        int mapperId=0, reducerId=0;

        for (int i=0;i<numMappers;i++) {
            mapperId=2025+i;
            mappers.add(String.valueOf(mapperId));

        }

        for (int i=0;i<numReducers;i++) {
            reducerId=2025+numMappers+i;
            reducers.add(String.valueOf(reducerId));

        }
    }

    public void beginProcessingData() throws RemoteException, MalformedURLException, NotBoundException, harreader.HarReaderException {

        /**
         * Let's call mappers so they begin pre-processing data*/

        System.out.println("Your files are being processed...");
        MapperInterface mapperInterface=null;
        ReducerInterface reducerInterface=null;
        StorageInterface storageInterface=null;
        String mapperAddress=null;
        ObjectRegistryInterface ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
        String storageAddress= ori.getObject("2022");
        storageInterface= (StorageInterface) Naming.lookup(storageAddress);
        String filePath= storageInterface.getFilePath();
        String fileName= storageInterface.getFileName();


        System.out.println("Calling Mappers");



        for(int i=0;i<mappers.size();i++){

            int files=storageInterface.getFilesOfMapper(mappers.get(i));
            int comeco=storageInterface.getFileCount();
            int fim=comeco+files;


            mapperAddress=ori.getObject(mappers.get(i));
            mapperInterface= (MapperInterface) Naming.lookup(mapperAddress);
            storageInterface.sendFilesToMapper(comeco, fim,mapperInterface);
            mapperInterface.FillResourcesMap(filePath, fileName);


        }

        LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap= new LinkedHashMap<String, ArrayList<ResourceInfo>>();
        timeHarMap=storageInterface.getHashMap();

        ArrayList<String> resources= new ArrayList<>(timeHarMap.keySet());
        System.out.println("Número resources " + resources.size());
        Set<Set<String>> combinations = Sets.combinations(ImmutableSet.copyOf(resources), 50);

        int numCombinations=combinations.size();
        int numReducers= reducers.size();
        storageInterface.setCombinationsPerReducer(numCombinations, numReducers);
        int fileCount=storageInterface.getFileCount();


        System.out.println("Calling Reducers");

        for(int i=0;i<reducers.size();i++){


            reducerInterface= (ReducerInterface) Naming.lookup(ori.getObject(reducers.get(i)));

             LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMapOfReducer=storageInterface.getHashMapOfReducer(reducers.get(i));


            reducerInterface.combinations(50, fileCount, timeHarMapOfReducer);

        }

        System.out.println("All Data processed.\n");


    }

}
