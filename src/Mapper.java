import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mapper extends UnicastRemoteObject implements MapperInterface {

    /**
     * Mapper will be coordinated by master
     * and will prepare the data for the reducer*/

    private static final long serialVersionUID = 1L;
    StorageInterface storageInterface=null;
    ObjectRegistryInterface ori=null;
    String Id;
    int placeOfOrder;

    protected Mapper(String Id,  int placeOfOrder) throws RemoteException{
        this.Id=Id;
        this.placeOfOrder=placeOfOrder;
    }

   public void getDataFromStorage(String Name,  byte [] mydata, int mylen) throws RemoteException{
       try{
           File f=new File("C:\\Users\\nelso\\OneDrive\\Desktop\\Projecto_final_SD_20-21\\out\\dadosMapper\\"+Name);
           f.createNewFile();
           FileOutputStream out=new FileOutputStream(f,true);
           out.write(mydata,0,mylen);
           out.flush();
           out.close();
       }catch(Exception e){
           e.printStackTrace();
       }
   }

    public void FillResourcesMap(String path, String fileName) throws harreader.HarReaderException, RemoteException, MalformedURLException, NotBoundException {

        ObjectRegistryInterface ori = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
        String storageAddress= ori.getObject("2022");

        storageInterface= (StorageInterface) Naming.lookup(storageAddress);
        LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap=new LinkedHashMap<String, ArrayList<ResourceInfo>>();

        int[] count = new int[]{0};
        int fileCount = 0;
        int tam=0;
        File file = null;
        int files= storageInterface.getFilesOfMapper(Id);

        try {
            harreader.HarReader harReader = new harreader.HarReader();

            if(placeOfOrder==1) {

                 file= new File(path + fileName + ".har");
                 tam=files-1;

            }
            else{

                fileCount=storageInterface.getFileCount();
                tam=fileCount+files-1;
                file = new File(path + fileName + "_" + fileCount +".har");}


            while (file.exists() && fileCount<tam){

                harreader.model.Har otherHar = harReader.readFromFile(file);

                for (harreader.model.HarEntry otherEntry : otherHar.getLog().getEntries()) {

                    if (!otherEntry.getResponse().getHeaders().get(0).getValue().contains("no-cache")) {

                        ResourceInfo resourceInfo = new ResourceInfo();
                        resourceInfo.resourceTime = (float) otherEntry.getTime();
                        resourceInfo.resourceType = otherEntry.get_resourceType();
                        resourceInfo.cachedResource = otherEntry.getResponse().getHeaders().get(0).getValue();
                        resourceInfo.resourceLength = otherEntry.getResponse().getBodySize();
                        resourceInfo.harRun = fileCount;

                        if (timeHarMap.containsKey(otherEntry.getRequest().getUrl())) {

                            ArrayList<ResourceInfo> list = timeHarMap.get(otherEntry.getRequest().getUrl());
                            AtomicBoolean repeatedCall = new AtomicBoolean(false);

                            list.forEach(value -> {
                                if (value.resourceTime.equals(resourceInfo.resourceTime)) {
                                    repeatedCall.set(true);
                                    return;
                                }
                            });

                            if (!repeatedCall.get())

                                timeHarMap.get(otherEntry.getRequest().getUrl()).add(resourceInfo);

                        } else {

                            ArrayList<ResourceInfo> l = new ArrayList<>();
                            l.add(resourceInfo);
                            timeHarMap.put(otherEntry.getRequest().getUrl(), l);
                        }
                    }
                }
               // System.out.println("Last file that Mapper id: "+Id+" analyzed: "+path + fileName + "_" + fileCount +".har");
                file = new File(path + fileName + "_" + ++fileCount +".har");
            }

            System.out.println("Last file that Mapper id: "+Id+" analyzed: "+path + fileName + "_" + fileCount +".har");
            storageInterface.storeHashMap(timeHarMap, files, placeOfOrder);




        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }
    }
}
