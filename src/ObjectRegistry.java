//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**Not implemented yet*/

public class ObjectRegistry extends UnicastRemoteObject implements ObjectRegistryInterface {
    private static final long serialVersionUID = 1L;
    static Map<Integer, String> hm = new TreeMap<Integer, String>();

    protected ObjectRegistry() throws RemoteException {
    }

    public void addObject(String objectID, String serverAddress) throws RemoteException {

        hm.put(Integer.parseInt(objectID), serverAddress);
    }

    public String getObject(String objectID) throws RemoteException {
        return (String) hm.get(Integer.parseInt(objectID));
    }

    public int getNextId(String typeOfServer) throws RemoteException{

        int id=0;

        ArrayList<Integer> keys = new ArrayList<Integer>(hm.keySet());
        for(int i=keys.size()-1; i>=0;i--){

            if(hm.get(keys.get(i)).contains(typeOfServer)){

                return keys.get(i)+1;
            }

            else{

                if(typeOfServer.equals("mapper")){return 2025;}

                else{
                    return keys.get(i)+1;
                }
                }
        }
        return id;
    }

    public int getNumServers(String typeOfServer) throws RemoteException{

        int numOfServers=0;

        ArrayList<Integer> keys = new ArrayList<Integer>(hm.keySet());
        for(int i=keys.size()-1; i>=0;i--){

            if(hm.get(keys.get(i)).contains(typeOfServer)){numOfServers++;}
        }
        return numOfServers;

    }


}


