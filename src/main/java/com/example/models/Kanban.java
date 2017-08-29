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

    private Map<Pair<String,String>,List<Issue>> data;
    private List labelsX;
    private List labelsY;
    public Kanban(List<Issue> issues) {
        this.issues = issues;
        //grouped();
    }

    public void group(String x,String y){
        AxisFactory axisFactory = new AxisFactory();
        List<Label> labelX = axisFactory.createLabels(x,issues);
        List<Label> labelY = axisFactory.createLabels(y,issues);

        KanbanTable table = new KanbanTableImpl();

        issues.stream().forEach(issue -> {
            String idX = IssueUtil.getIdOfProperty(x,issue);//todo property is List ex) milestone
            String idY = IssueUtil.getIdOfProperty(y,issue);
            table.putIssueAt(idX,idY,issue);
        });

        this.data = table;
        this.labelsX = labelX;
        this.labelsY = labelY;
    }

    private void grouped(){
        User u;
        List<User> users = issues.stream()
                //.filter(issue -> issue.getAssignee() != null )
                .map(issue -> {
                    if(null != issue.getAssignee()){
                        return issue.getAssignee();
                    }else {
                        return new UserNone();
                    }
                })
                .distinct()
                .collect(Collectors.toList());

        users.forEach(user -> {logger.debug("aaa:{}",user.getName());});

        List<Status> statuses = issues.stream().map(issue -> {return  issue.getStatus();})
                .distinct()
                .collect(Collectors.toList());

        Map<Pair<String,String>,List<Issue>> map = new HashMap<Pair<String,String>,List<Issue>>();
        this.issues.forEach(issue -> {
            String x = issue.getStatus().getIdAsString();
            User asignee = issue.getAssignee();
            if(null == asignee){
                asignee = new UserNone();
                logger.debug("usernone:{}",asignee.getName());
            }
            String y = asignee.getIdAsString();

            List<Issue> list = map.getOrDefault(Pair.of(x,y),new ArrayList<>());
            list.add(issue);
            map.put(Pair.of(x,y),list);

        });
        Status s;

        this.labelsX = statuses;
        this.labelsY = users;
        this.data = map;
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
