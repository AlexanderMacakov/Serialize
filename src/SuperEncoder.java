import java.io.IOException;

public interface SuperEncoder {

    byte[] serialize(Object anyBean) throws IOException;

    Object deserialize(byte[] data) throws IOException, ClassNotFoundException;




}
