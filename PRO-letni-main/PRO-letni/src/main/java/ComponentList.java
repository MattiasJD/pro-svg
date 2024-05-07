import component.BaseComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentList {
    private final ArrayList<BaseComponent> baseComponents;

    private static ComponentList INSTANCE;

    private long editTime;


    public static ComponentList getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ComponentList();
        }
        return INSTANCE;
    }

    private ComponentList() {
        baseComponents = new ArrayList<>();
    }


    public void add(BaseComponent p) {
        baseComponents.add(p);
    }

    public void remove(BaseComponent p) {
        baseComponents.remove(p);
    }

    public List<BaseComponent> getComponents() {
        return baseComponents;
    }

    public void setBaseComponents(List<BaseComponent> newBaseComponents) {
        this.baseComponents.clear();
        this.baseComponents.addAll(newBaseComponents);
    }

    public static ComponentList loadInstance(ComponentList loadedComponentList) {
        INSTANCE = loadedComponentList;
        return INSTANCE;
    }

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    public void addEditTime() {
        this.editTime += 1;
    }
}
