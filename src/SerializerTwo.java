import java.io.IOException;

public class SerializerTwo implements SuperEncoder {

    @Override
    public byte[] serialize(Object anyBean) throws IOException {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        return null;
    }
}
