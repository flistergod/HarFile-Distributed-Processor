import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public interface ReducerInterface extends Remote {

    public void combinations(int len, int fileCount, LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap)throws RemoteException;

    public void calculateStatistics(ProcessCombinationModel combinationInfo, int fileCount, LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap) throws RemoteException;
    public void test() throws RemoteException;

}
