import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Storage extends UnicastRemoteObject implements StorageInterface, Serializable {

    /**
     * Storage saves the client's given file row by row in rowsList
     * Storage saves each process data by each reducer in arraylist output
     * Storage saves every structure created by each mapper in a LinkedHashMap mapperLists. Esch keyvalue is the mapper's Id
     * */

    private static final long serialVersionUID = 1L;
    private static LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap= new LinkedHashMap<String, ArrayList<ResourceInfo>>();
    private static LinkedHashMap<String, Integer> filesPerMapper= new LinkedHashMap<String, Integer>();
    private static LinkedHashMap<String, Integer> combinationsPerReducer= new LinkedHashMap<String, Integer>();
    private static int fileCount=0;
    private static ArrayList<File> files= new ArrayList<File>();
    private String filePath;
    private  String fileName;
    ObjectRegistryInterface ori=null;
    private int combinationsCount=0;

    public int getCombinationsCount() throws RemoteException {
        return combinationsCount;
    }

    public void incrementCombinationsCount(int combinationsCount) throws RemoteException {
        this.combinationsCount += combinationsCount;
    }

    protected Storage() throws RemoteException {}

    public void sendFilesToMapper(int comeco, int fim, MapperInterface mapperInterface) throws RemoteException{

        for(int i=comeco;i<fim;i++){

            try{
                File f1=files.get(i);
                FileInputStream in=new FileInputStream(f1);
                byte [] mydata=new byte[1024*1024];
                int mylen=in.read(mydata);
                while(mylen>0){
                mapperInterface.getDataFromStorage(f1.getName(), mydata, mylen);
                    mylen=in.read(mydata);
                }
            }catch(Exception e){
                e.printStackTrace();

            }

        }
    }

    public int getFilesOfMapper(String mapperId) throws RemoteException{
        return filesPerMapper.get(mapperId);
    }

    public String getFilePath() throws RemoteException {
        return filePath;

    }

    public String getFileName()throws RemoteException {
        return fileName;
    }

    public void addFile (String path, String fileName) throws RemoteException, MalformedURLException, NotBoundException {

        this.fileName=fileName;
        this.filePath=path;
        int fileCount = 0;

        ObjectRegistryInterface ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
        int numMappers=ori.getNumServers("mapper");

        try {

            File file = new File(path + fileName + ".har");

            while (file.exists()){

                files.add(file);
                file = new File(path + fileName+ "_" + ++fileCount +".har");
            }
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }


        int filePerMapper = (fileCount / numMappers);
        int remainingFiles = (fileCount % numMappers);
        int extra=0;


        for (int i=0;i<numMappers;i++) {

            extra = (i + 1 <= remainingFiles) ? 1 : 0;
            int id=i+2025;
            filesPerMapper.put(String.valueOf(id), filePerMapper+extra);

        }
    }

    public void setCombinationsPerReducer(int combinations, int numReducers) throws RemoteException, MalformedURLException, NotBoundException {

         ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
       int numMappers=  ori.getNumServers("mapper");
        int combinationsPerReducer = (combinations / numReducers);
        int remainingFiles = (combinations % numReducers);
        int extra=0;


        for (int i=0;i<numReducers;i++) {

            extra = (i + 1 <= remainingFiles) ? 1 : 0;
            int id=i+2025+numMappers;
            this.combinationsPerReducer.put(String.valueOf(id), combinationsPerReducer+extra);

        }
    }

    public int getCombinationsPerReducer(String reducerId) throws RemoteException{
        return this.combinationsPerReducer.get(reducerId);
    }

    public LinkedHashMap<String, ArrayList<ResourceInfo>>  getHashMapOfReducer(String reducerId) throws RemoteException{

        return this.timeHarMap;


    }

    public int getNumFiles() throws RemoteException{

      return files.size();
    }

    public void storeHashMap(LinkedHashMap<String, ArrayList<ResourceInfo>> lhm, int filecount, int placeOfOrder) throws RemoteException{

        if(placeOfOrder==1){this.fileCount=0;}
        timeHarMap.putAll(lhm);
        this.fileCount+=filecount;
    }

    public int getFileCount() throws RemoteException{

      return this.fileCount;
    }

    public int getHashMapSize()throws RemoteException {

        return this.timeHarMap.size();
    }

    public LinkedHashMap<String, ArrayList<ResourceInfo>> getHashMap() throws RemoteException {

        return this.timeHarMap;
    }


}
