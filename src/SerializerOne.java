import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class SerializerOne implements SuperEncoder {

    @Override
    public byte[] serialize(Object anyBean) throws IOException {
        
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(anyBean);
                DFS.searchObj(anyBean, anyBean.hashCode());
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

    private static class DFS {


        public static void searchObj(Object bean, int RootHash) {
            List<Object> anyObjectBean = new ArrayList<>();
            Class DFSBean = bean.getClass();
            Field[] fields = DFSBean.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                if (field.getType().equals(Map.class)) {
                    try {
                        Map<String, Bean> beanMap = (Map<String, Bean>) field.get(DFSBean);

                        for (Map.Entry<String, Bean> entry : beanMap.entrySet()){

                            Bean inner = entry.getValue();

                            if(inner.hashCode() == RootHash) {
                                throw new CircularReferenceException();
                            }
                            searchObj(inner, RootHash);
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (CircularReferenceException e) {
                        e.printStackTrace();
                    }
                }
                if (field.getType().equals(List.class)) {

                    try {
                        ArrayList<Bean> beanArrayList = (ArrayList<Bean>) field.get(DFSBean);

                        for (Bean beanArray: beanArrayList) {

                            if(beanArray.hashCode() == RootHash) {
                                throw new CircularReferenceException();
                            }

                            searchObj(beanArray, RootHash);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (CircularReferenceException e) {
                        e.printStackTrace();
                    }
                }
                if (field.getType().equals(Set.class)) {
                    try {
                        HashSet<Bean> beanHashSet = (HashSet<Bean>) field.get(DFSBean);

                        for (Bean beanHash : beanHashSet) {

                            if (beanHash.hashCode() == RootHash) {
                                throw new CircularReferenceException();
                            }

                            searchObj(beanHash, RootHash);

                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (CircularReferenceException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        Object etc = field.get(DFSBean);
                        if(etc.hashCode() == RootHash) {
                            throw new CircularReferenceException();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (CircularReferenceException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
