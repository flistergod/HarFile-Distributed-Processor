package harreader.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import harreader.HarReaderMode;

import java.rmi.RemoteException;

public interface MapperFactory {

    ObjectMapper instance(HarReaderMode mode) throws RemoteException;

}
