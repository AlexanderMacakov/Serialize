import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class SerializerOne implements SuperEncoder {

    private ArrayList<Integer> hashcodeList = new ArrayList<>();

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

    private class DFS {

        public void searchObj(Object bean, int rootHash) {

            if (!hashcodeList.isEmpty()) {
                for (Integer hash : hashcodeList) {

                    if (hash == rootHash) {
                        try {
                            throw new CircularReferenceException();
                        } catch (CircularReferenceException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            
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

                            if(inner.hashCode() == rootHash) {
                                throw new CircularReferenceException();
                            }
                            searchObj(inner, rootHash);
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

                            if(beanArray.hashCode() == rootHash) {
                                throw new CircularReferenceException();
                            }

                            searchObj(beanArray, rootHash);
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

                            if (beanHash.hashCode() == rootHash) {
                                throw new CircularReferenceException();
                            }

                            searchObj(beanHash, rootHash);

                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (CircularReferenceException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        Object etc = field.get(DFSBean);
                        if(etc.hashCode() == rootHash) {
                            throw new CircularReferenceException();
                        }
                        searchObj(etc, etc.hashCode());
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
