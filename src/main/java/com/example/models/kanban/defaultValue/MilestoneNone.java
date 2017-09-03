package com.example.models.kanban.defaultValue;

import com.nulabinc.backlog4j.Milestone;
import com.nulabinc.backlog4j.internal.json.MilestoneJSONImpl;

/**
 * Created by zonoise on 2017/09/04.
 */
public class MilestoneNone extends MilestoneJSONImpl {

    private long id;
    private String name;

    public MilestoneNone() {
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
        return this.name;
    }
}