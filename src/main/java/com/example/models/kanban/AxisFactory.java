package com.example.models.kanban;

import com.example.models.kanban.defaultValue.UserNone;
import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.Status;
import com.nulabinc.backlog4j.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zonoise on 2017/08/29.
 */
public class AxisFactory {
    public List<Label> createLabels(String label, List<Issue> issues) {
        Stream<Issue> issueStream = issues.stream();
        switch (label) {
            case "status":
                return issueStream.
                        map(issue -> {
                            return issue.getStatus();
                        })
                        .distinct()
                        .map(status -> {
                            return new StatusLabel(status);
                        })
                        .collect(Collectors.toList());
            case "user":
                return issueStream.map(
                        issue -> {
                            User asignee = issue.getAssignee();
                            if(null == asignee){
                                asignee = new UserNone();
                            }
                            return asignee;
                        })
                        .distinct()
                        .map(user -> {
                            return new UserLabel(user);
                        })
                        .collect(Collectors.toList());
            case "milestone":
                return issueStream.map(issue -> {
                        return issue.getMilestone();
                    }).flatMap(milestones -> {
                        return milestones.stream();
                    }) //oh!
                        .distinct().map(milestone -> {
                            return new MilestoneLabel(milestone);
                        })
                        .collect(Collectors.toList());
        }
        return null;
    }
}
