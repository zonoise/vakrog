package com.example.controller;

import com.example.utils.BacklogApiWrapper;
import com.nulabinc.backlog4j.*;
import com.nulabinc.backlog4j.api.option.GetIssuesParams;
import com.nulabinc.backlog4j.auth.AccessToken;
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
        return "/kanban/projects";
    }

    @RequestMapping(value = "/form",method = RequestMethod.GET)
    public String formAxis(@RequestParam(required = false,name="space") String projectKey,
                           HttpSession session){

        BacklogClient client = getClient(session);
        if(null == client){
            return "redirect:/";
        }

        try {
            ResponseList<Project> projects = client.getProjects();
            Project project = projects.get(0);//todo get from request

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
        }
        return "kanban/form";
    }

    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String show(){
        return "";
    }

}
