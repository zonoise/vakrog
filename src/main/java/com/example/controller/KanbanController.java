package com.example.controller;

import com.example.exceptions.VakrogException;
import com.example.models.Kanban;
import com.example.models.kanban.KanbanTable;
import com.example.utils.BacklogApiWrapper;
import com.nulabinc.backlog4j.*;
import com.nulabinc.backlog4j.api.option.GetIssuesParams;
import com.nulabinc.backlog4j.auth.AccessToken;
import com.nulabinc.backlog4j.internal.json.IssueJSONImpl;
import com.nulabinc.backlog4j.internal.json.UserJSONImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zonoise on 2017/08/24.
 */

@Controller
@RequestMapping("/kanban")
public class KanbanController {
    private static final Logger logger = LoggerFactory.getLogger(KanbanController.class);

    private BacklogClient getClient(HttpSession session){
        AccessToken accessToken = (AccessToken)session.getAttribute("token");
        String space = (String)session.getAttribute("space");
        BacklogClient backlog = BacklogApiWrapper.getClient(space,accessToken);
        return backlog;
    }

    @RequestMapping("/projects")
    public String projects(HttpSession session,Model model){
        BacklogClient client = getClient(session);
        if(null == client){
            return "redirect:/";
        }

        List<Project> projects = client.getProjects();
        model.addAttribute("projects",projects);
        return "kanban/projects";
    }

    @RequestMapping(value = "/form",method = RequestMethod.GET)
    public String formAxis(@RequestParam(required = false,name="projectId") String projectId,
                           HttpSession session){

        BacklogClient client = getClient(session);
        if(null == client){
            return "redirect:/";
        }

        try {

            ResponseList<Project> projects = client.getProjects();

            Project project = client.getProject(projectId);//todo get from request
            if(null == project){
                throw new VakrogException("project not found");
            }
            List<String> ids = Arrays.asList( project.getIdAsString());
            GetIssuesParams params = new GetIssuesParams(ids);

            List<Issue> issues = client.getIssues(params);
            List<Status> statuses = client.getStatuses();

            issues.forEach(issue -> {
                logger.debug("issue:{}",issue);
            });
            logger.debug("statuses:{}", statuses.toString());
        }catch (BacklogException e){
            logger.debug("BacklogException:{}", e.toString());
        }catch (VakrogException e){
            logger.debug("VakrogException:{}", e.toString());
        }

        return "kanban/form";
    }

    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String show(
//            @RequestParam("x") String x,
//                       @RequestParam("y") String y,
                       @RequestParam("projectId") String projectId,
                                   HttpSession session,Model model){

        BacklogClient client = getClient(session);
        ResponseList<Status> statuses  = client.getStatuses();

        ResponseList<Issue> issues  = client.getIssues(new GetIssuesParams(Arrays.asList(projectId)));
        List<User> users = issues.stream().map(
                issue -> {User assignee = issue.getAssignee();
                    if(null == assignee){
                            logger.debug("assigneee{}:",assignee); //todo
                    }
                    return assignee;
                })
                .filter( user -> user !=null ).distinct().collect(Collectors.toList());


        Kanban kanban = new Kanban(issues);
        KanbanTable data = kanban.getData();
        model.addAttribute("labelX",kanban.getLabelsX());
        model.addAttribute("labelY",kanban.getLabelsY());
        model.addAttribute("data",data);
        return "kanban/show";
    }

}
