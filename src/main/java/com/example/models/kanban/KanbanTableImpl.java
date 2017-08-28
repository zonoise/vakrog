package com.example.models.kanban;

import com.nulabinc.backlog4j.Issue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Created by zonoise on 2017/08/28.
 */
public class KanbanTableImpl extends HashMap<Pair<String, String>, List<Issue>> implements KanbanTable {
    public List<Issue> getListAt(String x,String y){
        return this.get(Pair.of(x,y));
    }
}
