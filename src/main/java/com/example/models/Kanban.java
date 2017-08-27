package com.example.models;

import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.ResponseList;
import com.nulabinc.backlog4j.Status;
import com.nulabinc.backlog4j.User;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zonoise on 2017/08/28.
 */
public class Kanban {
    private List<Issue> issues;


    private Map<Pair<String,String>,List<Issue>> data;
    private List labelsX;
    private List labelsY;
    public Kanban(List<Issue> issues) {
        this.issues = issues;
        grouped();
    }

    private void grouped(){
        List<User> users = issues.stream().map(
                issue -> {return issue.getAssignee();})
                .distinct()
                .collect(Collectors.toList());

        List<Status> statuses = issues.stream().map(issue -> {return  issue.getStatus();})
                .distinct()
                .collect(Collectors.toList());

        Map<Pair<String,String>,List<Issue>> map = new HashMap<Pair<String,String>,List<Issue>>();
        this.issues.forEach(issue -> {
            String x = issue.getStatus().getIdAsString();
            String y = issue.getAssignee().getIdAsString();
            List<Issue> list = map.getOrDefault(Pair.of(x,y),new ArrayList<>());
            list.add(issue);
            map.put(Pair.of(x,y),list);
        });

        this.labelsX = statuses;
        this.labelsY = users;
        this.data = map;

    }

    public Map<Pair<String, String>, List<Issue>> getData() {
        return data;
    }

    public List getLabelsX() {
        return labelsX;
    }

    public List getLabelsY() {
        return labelsY;
    }
}
