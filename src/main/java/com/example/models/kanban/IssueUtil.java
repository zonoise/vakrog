package com.example.models.kanban;

import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.Milestone;
import com.nulabinc.backlog4j.Status;
import com.nulabinc.backlog4j.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zonoise on 2017/08/29.
 */
public class IssueUtil {
    private static final Logger logger = LoggerFactory.getLogger(IssueUtil.class);

    public static List<String> getIdsOfProperty(KanbanAxis axis, Issue issue){
        List<Label> labels = getLabels(axis,issue);
        List<String> ids = labels.stream().map(label -> label.getId()
        ).collect(Collectors.toList());
        return ids;
    }

    public static List<Label> getLabels(KanbanAxis axis, Issue issue){
        List<Label> result = new ArrayList<>();
        switch (axis){
            case user:
                User u = issue.getAssignee();
                if(null != u){
                    result.add(new LabelImpl(u.getIdAsString(),u.getName()));
                }else {
                    result.add(new LabelImpl("-1","NONE"));
                }
                break;
            case status:
                Status s = issue.getStatus();
                if(null != s){
                    result.add(new LabelImpl(s.getIdAsString(),s.getName()));
                }else {
                    result.add(new LabelImpl("-1","NONE"));
                }
                break;
            case milestone:
                List<Milestone> m = issue.getMilestone();
                if(m.isEmpty()){
                    result.add(new LabelImpl("-1","NONE"));
                }else {
                    m.stream()
                            .map(milestone -> new LabelImpl(milestone.getIdAsString(),milestone.getName()))
                            .forEach(label -> result.add(label));
                }
                break;
        }

        return result;
    }

}