import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ResourceInfo extends UnicastRemoteObject implements  Serializable {
    public String resourceType;
    public Float resourceTime;
    public boolean repeatedCall;
    public String cachedResource;
    public Integer harRun;
    public long resourceLength;

    protected ResourceInfo() throws RemoteException {
    }
}