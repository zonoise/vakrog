package com.example.controller;

import com.example.exceptions.VakrogException;
import com.example.forms.KanbanAxis;
import com.example.models.Kanban;
import com.example.models.kanban.Axis;
import com.example.models.kanban.KanbanTable;
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
import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;

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
    public String formAxis(
            @RequestParam(required = false,name="projectId") String projectId,
            Model model, HttpSession session) {

        model.addAttribute("axes",KanbanAxis.values());
        model.addAttribute("projectId",projectId);

        BacklogClient client = getClient(session);
        if(null == client){
            return "redirect:/";
        }

        return "kanban/form";
    }

    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String show(
            @RequestParam("x") String x,
                       @RequestParam("y") String y,
                       @RequestParam("projectId") String projectId,
                                   HttpSession session,Model model){

        BacklogClient client = getClient(session);
        String c = KanbanAxis.user.getCode();
        logger.debug("code{}", c);

        KanbanAxis axisX = KanbanAxis.valueOf(x);
        KanbanAxis axisY = KanbanAxis.valueOf(y);

        if(axisX == KanbanAxis.none || axisY == KanbanAxis.none){
            return "redirect:/";
        }

        int issueParamCount = 100;
        GetIssuesParams issueParam = new GetIssuesParams(Arrays.asList(projectId));
        ResponseList<Issue> issues  = client.getIssues(issueParam.count(issueParamCount));

        Kanban kanban = new Kanban(issues);
        kanban.group(axisX.getCode(),axisY.getCode());

        KanbanTable data = kanban.getData();
        model.addAttribute("labelX",kanban.getLabelsX());
        model.addAttribute("labelY",kanban.getLabelsY());
        model.addAttribute("data",data);
        model.addAttribute("projectId",projectId);
        return "kanban/show";
    }

}
