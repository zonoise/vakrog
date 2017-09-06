package com.example.models.kanban;


import com.example.models.kanban.Label;

/**
 * Created by zonoise on 2017/09/07.
 */
public class LabelImpl implements Label {
    private String id;

    private String name;

    public LabelImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id ;
    }

    @Override
    public String getName() {
        return this.name;
    }

    //intellij generate
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelImpl label = (LabelImpl) o;

        if (id != null ? !id.equals(label.id) : label.id != null) return false;
        return !(name != null ? !name.equals(label.name) : label.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
