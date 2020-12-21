import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Reducer extends UnicastRemoteObject implements ReducerInterface {

    /**
     * Class Reducer outdated  - 09/12/20
     * comeco= (nºlinhas/numMappers) * (posicao-1)
     *
     * fim= (nºlinhas/nºMappers) * (posicao-1) + (nºlinhas/nº mappers)*/

    /**
     * Reducer will be responsable for processing the file's data.
     * Reducer will get the data already prepared by mapper on storage service
     * Reducer will process the data with the professor's algorithm
     * After the processing is completed, the output data will be saved in storage
     */


    private static final long serialVersionUID = 1L;
    StorageInterface storageInterface = null;
    ObjectRegistryInterface ori = null;
    String Id;
    int numReducers, placeOfOrder;

    private LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap = new LinkedHashMap<String, ArrayList<ResourceInfo>>();
    private int fileCount;
    private String fileName;
    private LinkedList<ProcessCombinationModel> combinationStatistics = new LinkedList<>();

    protected Reducer(String Id, int placeOfOrder) throws RemoteException {
        this.Id = Id;
        this.placeOfOrder = placeOfOrder;

    }
    public void test() throws RemoteException{System.out.println("not rmi porblem");}

    public void combinations(int len, int fileCount, LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap)throws RemoteException{

        ArrayList<String> resources= new ArrayList<>(timeHarMap.keySet());
        System.out.println("Número resources " + resources.size());
        Set<Set<String>> combinations = Sets.combinations(ImmutableSet.copyOf(resources), len);
        Set r;
        StringBuffer line = new StringBuffer();
        Iterator combIterator = combinations.iterator();
        System.out.println("Número combinações " + combinations.size());
        int i = 0;
        while (combIterator.hasNext()){
            r = (Set) combIterator.next();

            line.setLength(0);
            Iterator lineIterator = r.iterator();
            while(lineIterator.hasNext()){
                if(line.length()>0) line.append(",");
                line.append(lineIterator.next().toString());
            }

            ProcessCombinationModel combinationInfo= new ProcessCombinationModel();
            combinationInfo.combination = line.toString();
            calculateStatistics(combinationInfo,fileCount, timeHarMap);
            if(i++%100000==0) System.out.println("Comb " + i);
        }
    }

    public void calculateStatistics(ProcessCombinationModel combinationInfo, int fileCount, LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap){
        boolean resourceFound=false;

        String[] resources = combinationInfo.combination.split(","); // resources of each combination
        for (int i =0; i < fileCount; i++) { //controlo por run
            for(String combinationResource : resources){
                resourceFound=false;
                for(ResourceInfo comb : timeHarMap.get(combinationResource))
                    if(comb.harRun == i) {
                        resourceFound = true;
                        combinationInfo.resourceLength += comb.resourceLength;
                        break;
                    }
                if(! resourceFound){ break;}
            }
            if(resourceFound){
                combinationInfo.numberOfRuns++;
            }
        }

        combinationInfo.percentage = (float) combinationInfo.numberOfRuns/fileCount;

        if(combinationInfo.percentage > 0.5) {
            System.out.print("Combination + probability");
            for(String s: resources) System.out.print(System.identityHashCode(s) + "  ");
            System.out.print(combinationInfo.percentage + "\n");

            this.combinationStatistics.add(combinationInfo);
            System.out.println("Comb valida. Percentagem: " + combinationInfo.percentage);
        }
    }
}

