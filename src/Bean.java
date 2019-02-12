import java.io.Serializable;
import java.util.*;

public class Bean implements Serializable {

    private String name = "BeanName";
    private int count = 2;

    private Bean bean;

    private List<Bean> listBean;
    private Set<Bean> setBean;
    private Map<String, Bean> mapBean;

    Bean(String name, int count) {
        this.name = name;
        this.count = count;

        this.listBean = new ArrayList<>();
        this.setBean = new HashSet<>();
        this.mapBean = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }

    public List<Bean> getListBean() {
        return listBean;
    }

    public void setListBean(List<Bean> listBean) {
        this.listBean = listBean;
    }

    public Set<Bean> getSetBean() {
        return setBean;
    }

    public void setSetBean(Set<Bean> setBean) {
        this.setBean = setBean;
    }

    public Map<String, Bean> getMapBean() {
        return mapBean;
    }

    public void setMapBean(Map<String, Bean> mapBean) {
        this.mapBean = mapBean;
    }

    @Override
    public String toString() {
        return "Bean " +
                "name='" + name + '\'' +
                ", count=" + count
                ;
    }
}