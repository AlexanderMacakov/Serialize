import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


public class SerializerOne implements SuperEncoder {

    private ArrayList<Integer> hashcodeList = new ArrayList<>();

    @Override
    public byte[] serialize(Object anyBean) throws IOException  {

        byte[] massByte = null;
        ByteArrayOutputStream boss = null;
        ObjectOutputStream ooss = null;
        try {
            boss = new ByteArrayOutputStream();
            ooss = new ObjectOutputStream(boss);
            ooss.writeObject(anyBean);
            ooss.flush();
            massByte = boss.toByteArray();
        } finally {
            if (ooss != null) {
                ooss.close();
            }
            if (boss != null) {
                boss.close();
            }
        }
        return massByte;
    }

    @Override
    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        Object objDeserialize = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            objDeserialize = ois.readObject();
        } finally {
            if (bais != null) {
                bais.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        DepthFirstSearch dfs = new DepthFirstSearch();
        dfs.searchObj(objDeserialize);
        return objDeserialize;
    }

    private class DepthFirstSearch {
        public void searchObj(Object bean) {
            if (isNull(bean)) {
                if (!hashcodeList.isEmpty()) {
                    for (Integer hash : hashcodeList) {
                        if (hash == bean.hashCode()) {
                            try {
                                throw new CircularReferenceException();
                            } catch (CircularReferenceException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                hashcodeList.add(bean.hashCode());

                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getType().isAssignableFrom(Map.class)) {
                        try {
                            Map<String, Bean> beanMap = (Map<String, Bean>) field.get(bean);
                            for (Map.Entry<String, Bean> entry : beanMap.entrySet()) {
                                Bean inner = entry.getValue();
                                if (inner.hashCode() == bean.hashCode()) {
                                    throw new CircularReferenceException();
                                }
                                searchObj(inner);
                            }

                        } catch (IllegalAccessException | CircularReferenceException e) {
                            e.printStackTrace();
                        }
                    }
                    if (field.getType().isAssignableFrom(List.class)) {
                        try {
                            Object value = field.get(bean);
                            List<Bean> beanArrayList = (List<Bean>) value;
                            for (Bean beanArray : beanArrayList) {
                                if (beanArray.hashCode() == bean.hashCode()) {
                                    throw new CircularReferenceException();
                                }
                                searchObj(beanArray);
                            }
                        } catch (IllegalAccessException | CircularReferenceException e) {
                            e.printStackTrace();
                        }
                    }
                    if (field.getType().isAssignableFrom(Set.class)) {
                        try {
                            HashSet<Bean> beanHashSet = (HashSet<Bean>) field.get(bean);
                            for (Bean beanHash : beanHashSet) {
                                if (beanHash.hashCode() == bean.hashCode()) {
                                    throw new CircularReferenceException();
                                }
                                searchObj(beanHash);
                            }
                        } catch (IllegalAccessException | CircularReferenceException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!field.getType().equals(String.class) && !field.getType().isPrimitive() && !field.getType().isAssignableFrom(List.class) && !field.getType().isAssignableFrom(Set.class) && !field.getType().isAssignableFrom(Map.class)) {
                        try {
                            Object etc = field.get(bean);
                            if (isNull(etc)) {
                                if (etc.hashCode() == bean.hashCode()) {
                                    throw new CircularReferenceException();
                                }
                                searchObj(etc);
                            }

                        } catch (IllegalAccessException | CircularReferenceException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private boolean isNull(Object obj) {
            return obj != null;
        }


    }

    public static String toString(byte[] bytes) {
        return new String(bytes);
    }
}
