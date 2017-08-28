package com.example.models.kanban;

import com.nulabinc.backlog4j.Issue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by zonoise on 2017/08/28.
 */
public interface KanbanTable extends Map<Pair<String, String>, List<Issue>> {
    public List<Issue> getListAt(String x,String y);
    public void putIssueAt(String x,String y,Issue issue);

}
