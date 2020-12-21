package harreader.jackson;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.rmi.RemoteException;
import java.util.Date;

public class DefaultMapperFactory implements harreader.jackson.MapperFactory {

    public ObjectMapper instance(harreader.HarReaderMode mode)  throws RemoteException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        if (mode == harreader.HarReaderMode.LAX) {
            module.addDeserializer(Date.class, new harreader.jackson.ExceptionIgnoringDateDeserializer());
            module.addDeserializer(Integer.class, new harreader.jackson.ExceptionIgnoringIntegerDeserializer());
        }
        mapper.registerModule(module);
        return mapper;
    }

}
