import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

public class SerializerTwo implements SuperEncoder {

    @Override
    public byte[] serialize(Object anyBean) {
        return SerializationUtils.serialize((Serializable) anyBean);
    }

    @Override
    public Object deserialize(byte[] data) {
        return (Object) SerializationUtils.deserialize(data);
    }
}
