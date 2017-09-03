package com.example.models.kanban;

import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.Milestone;
import com.nulabinc.backlog4j.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by zonoise on 2017/08/29.
 */
public class IssueUtil {
    private static final Logger logger = LoggerFactory.getLogger(IssueUtil.class);

    public static String[] getIdsOfProperty(String propertyName, Issue issue){
        switch (propertyName){
            case "user":
                User u = issue.getAssignee();
                if(null != u){
                    return new String[]{u.getIdAsString()};
                }else {
                    return new String[]{"-1"};
                }

            case "status":
                return new String[]{issue.getStatus().getIdAsString()};
            case "milestone":
                 String[] ids = issue.getMilestone().stream().map(milestone -> milestone.getIdAsString())
                        .toArray(String[]::new);
                 if(ids.length == 0){
                     ids = new String[]{"-1"};
                 }
                 return ids;
        }
        return null;
    }
}