package com.example.models;

import com.example.controller.KanbanController;
import com.example.models.kanban.*;
import com.example.models.kanban.defaultValue.UserNone;
import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.ResponseList;
import com.nulabinc.backlog4j.Status;
import com.nulabinc.backlog4j.User;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zonoise on 2017/08/28.
 */
public class Kanban {

    private static final Logger logger = LoggerFactory.getLogger(Kanban.class);

    private List<Issue> issues;

    private KanbanTable data;
    private List labelsX;
    private List labelsY;
    public Kanban(List<Issue> issues) {
        this.issues = issues;
    }

    public void group(String x,String y){
        AxisFactory axisFactory = new AxisFactory();
        List<Label> labelX = axisFactory.createLabels(x,issues);
        List<Label> labelY = axisFactory.createLabels(y,issues);

        KanbanTable table = new KanbanTableImpl();

        issues.stream().forEach(issue -> {

            String[] idsX = IssueUtil.getIdsOfProperty(x,issue);//todo property is List ex) milestone
            String[] idsY = IssueUtil.getIdsOfProperty(y,issue);

            for(String idx : idsX){
                for(String idy : idsY){
                    table.putIssueAt(idx,idy,issue);
                }
            }
        });

        this.data = table;
        this.labelsX = labelX;
        this.labelsY = labelY;
    }

    public KanbanTable getData() {
        KanbanTable table = new KanbanTableImpl();
        table.putAll(data);
        return table;
    }

    public List<Label> getLabelsX() {
        return labelsX;
    }

    public List<Label> getLabelsY() {
        return labelsY;
    }
}
