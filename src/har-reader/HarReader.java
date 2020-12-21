package harreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import harreader.jackson.DefaultMapperFactory;
import harreader.jackson.MapperFactory;
import harreader.model.Har;
import harreader.jackson.MapperFactory;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class HarReader {

    private final MapperFactory mapperFactory;

    public HarReader(MapperFactory mapperFactory)throws RemoteException {
        if (mapperFactory == null) {
            throw new IllegalArgumentException("mapperFactory must not be null!");
        }
        this.mapperFactory = mapperFactory;
    }

    public HarReader() throws RemoteException {
        this(new DefaultMapperFactory());
    }

    public Har readFromFile(File har) throws harreader.HarReaderException,  RemoteException {
        return this.readFromFile(har, harreader.HarReaderMode.STRICT);
    }

    public Har readFromFile(File har, harreader.HarReaderMode mode) throws harreader.HarReaderException,  RemoteException {

        ObjectMapper mapper = mapperFactory.instance(mode);
        try {

            return mapper.readValue(har, Har.class);
        } catch (IOException e) {

            throw new harreader.HarReaderException(e);
        }
    }

    public Har readFromString(String har) throws harreader.HarReaderException,  RemoteException {
        return this.readFromString(har, harreader.HarReaderMode.STRICT);
    }

    public Har readFromString(String har, harreader.HarReaderMode mode) throws harreader.HarReaderException,  RemoteException{
        ObjectMapper mapper = mapperFactory.instance(mode);
        try {
            return mapper.readValue(har, Har.class);
        } catch (IOException e) {
            throw new harreader.HarReaderException(e);
        }
    }

}
