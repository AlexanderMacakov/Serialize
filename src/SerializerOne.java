
import java.io.*;

public class SerializerOne implements SuperEncoder {

    @Override
    public byte[] serialize(Object anyBean) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(anyBean);
            }
            return b.toByteArray();
        }
    }

    @Override
    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(data)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }
}
