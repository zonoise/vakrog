package com.example.models.kanban.defaultValue;

import com.nulabinc.backlog4j.internal.json.UserJSONImpl;

/**
 * Created by zonoise on 2017/08/29.
 */
public class UserNone extends UserJSONImpl {
    private long id;
    private String name;

    public UserNone() {
        this.id = -1;
        this.name = "NONE";
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getIdAsString() {
        return String.valueOf(this.id);
    }

    @Override
    public String getName() {
//        return super.getName();
        return this.name;
    }
}
