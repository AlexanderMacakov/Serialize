import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Objects.isNull;

public class SerializerOne implements SuperEncoder {

    private ArrayList<Integer> hashcodeList = new ArrayList<>();

    @Override
    public byte[] serialize(Object anyBean) throws IOException {

        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(anyBean);
                DepthFirstSearch dfs = new DepthFirstSearch();
                dfs.searchObj(anyBean);
            }
            return b.toByteArray();
        }
    }

    @Override
    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(data)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
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

                    if (field.getType().equals(Map.class)) {
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

                    if (field.getType().equals(List.class)) {

                        try {
                            ArrayList<Bean> beanArrayList = (ArrayList<Bean>) field.get(bean);

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

                    if (field.getType().equals(Set.class)) {
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
                    if (field.getType().equals(Bean.class)) {
                        try {
                            Object etc = field.get(bean);

                            if(isNull(etc)) {
                                if (etc.hashCode() == bean.hashCode()) {
                                    throw new CircularReferenceException();
                                }
                            }
                                searchObj(etc);
                            } catch(IllegalAccessException | CircularReferenceException e){
                                e.printStackTrace();
                            }

                    }

                /*else {
                    try {

                        Object etc = field.get(bean);
                        if (etc.hashCode() == bean.hashCode()) {
                            throw new CircularReferenceException();
                        }
                        searchObj(etc);
                    } catch (IllegalAccessException | CircularReferenceException e) {
                        e.printStackTrace();
                    }
                }*/

                }
            }

        }

        private boolean isNull(Object obj) {
            return obj != null;
        }
    }
}
