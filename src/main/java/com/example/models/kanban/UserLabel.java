package com.example.models.kanban;

import com.nulabinc.backlog4j.User;

/**
 * Created by zonoise on 2017/08/29.
 */
public class UserLabel implements Label {
    private User user;

    public UserLabel(User user) {
        this.user = user;
    }

    @Override
    public String name() {
        return user.getName();
    }

    @Override
    public String Id() {
        return user.getIdAsString();
    }
}