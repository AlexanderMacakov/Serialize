import java.io.IOException;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Start {


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Bean beanOne = new Bean("beanOne", 1);
        Bean beanTwo = new Bean("beanTwo", 1);
        Bean beanThree = new Bean("beanThree", 1);
        Bean beanFour = new Bean("beanfour", 1);


        beanOne.setBean(new Bean("innerBeanOne", 2));
        ArrayList<Bean> arrayList = new ArrayList<>();
        arrayList.add(beanTwo);
        arrayList.add(beanThree);
        arrayList.add(beanOne);
        beanOne.setListBean(arrayList);

        byte[] bytes;

        SerializerOne serializerOne = new SerializerOne();

        bytes = serializerOne.serialize(beanOne);

        for (byte b: bytes) {
            System.out.println(b);
        }

        Bean deserializeBean = (Bean) serializerOne.deserialize(bytes);

        System.out.println(deserializeBean.toString());
    }
}
