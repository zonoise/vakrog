package com.example.models.kanban;

import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.Milestone;

import java.util.Optional;

/**
 * Created by zonoise on 2017/08/29.
 */
public class IssueUtil {
    public static String getIdOfProperty(String propertyName, Issue issue){
        switch (propertyName){
            case "user":
                return issue.getAssignee().getIdAsString();
            case "status":
                return issue.getStatus().getIdAsString();
            case "milestone":
                 Optional<Milestone> m = issue.getMilestone().stream().findFirst();
                if(!m.isPresent()){
                    return "NONE";
                }
                return m.get().getIdAsString();
        }
        return null;
    }
}