package com.example.models.kanban;

import com.example.models.kanban.defaultValue.MilestoneNone;
import com.example.models.kanban.defaultValue.UserNone;
import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.Milestone;
import com.nulabinc.backlog4j.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zonoise on 2017/08/29.
 */
public class AxisFactory {
    private static final Logger logger = LoggerFactory.getLogger(AxisFactory.class);

    public List<Label> createLabels(KanbanAxis label, List<Issue> issues) {
        List<Label> l = issues.stream().map(
                issue -> {  return IssueUtil.getLabels(label,issue);}
        )
        .flatMap(labels -> labels.stream() )
        .distinct().collect(Collectors.toList());

        return l;
    }
}
