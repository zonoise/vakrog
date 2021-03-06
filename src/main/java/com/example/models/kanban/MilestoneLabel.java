package com.example.models.kanban;

import com.nulabinc.backlog4j.Milestone;

/**
 * Created by zonoise on 2017/08/29.
 */
public class MilestoneLabel implements Label {
    private Milestone milestone;

    public MilestoneLabel(Milestone milestone) {
        this.milestone = milestone;
    }

    @Override
    public String getName() {
        return this.milestone.getName();
    }

    @Override
    public String getId() {
        return this.milestone.getIdAsString();
    }
}
