package com.example.models.kanban;

import com.nulabinc.backlog4j.Status;

/**
 * Created by zonoise on 2017/08/29.
 */
public class StatusLabel implements Label {
    private Status status;
    public StatusLabel(Status status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return this.status.getName();
    }

    @Override
    public String getId() {
        return status.getIdAsString();
    }
}