package com.example.controller;

import com.example.forms.GetOauthAccessTokenForm;
import com.nulabinc.backlog4j.auth.AccessToken;
import com.nulabinc.backlog4j.auth.BacklogOAuthSupport;
import com.nulabinc.backlog4j.conf.BacklogConfigure;
import com.nulabinc.backlog4j.conf.BacklogJpConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;

/**
 * Created by zonoise on 2017/08/23.
 */
@Controller
public class BacklogAuthController {
    private static final Logger logger = LoggerFactory.getLogger(BacklogAuthController.class);


    @Value("${backlog.spaceid}")
    private String spaceId;

    @Value("${backlog.apikey}")
    private String apiKey;

    @Value("${backlog.apisecret}")
    private String apiSecret;

    @RequestMapping("/login")
    public String login(Model model){

        try {

            BacklogConfigure configure = new BacklogJpConfigure(spaceId).apiKey(apiKey);

            BacklogOAuthSupport support = new BacklogOAuthSupport(configure);
            support.setOAuthClientId(apiKey,apiSecret);
            String url = support.getOAuthAuthorizationURL();
            model.addAttribute("url",url);

            logger.info(this.getClass().toString() + "{}",apiKey);
        }catch (MalformedURLException e){
            //todo
        }

        return "login";
    }

    @RequestMapping("/callback")
    public String callback(@RequestParam("code") String code, Model model){
        try{
            BacklogConfigure configure = new BacklogJpConfigure(spaceId).apiKey(apiKey);

            BacklogOAuthSupport support = new BacklogOAuthSupport(configure);
            support.setOAuthClientId(apiKey,apiSecret);

            GetOauthAccessTokenForm f = new GetOauthAccessTokenForm();

            f.setRest_url(configure.getOAuthAccessTokenURL());
            f.setGrant_type("authorization_code");
            f.setCode(code);
            f.setClient_id(apiKey);
            f.setClient_secret(apiSecret);

            model.addAttribute("rest_url",configure.getRestBaseURL()+"/oauth2/token");
            model.addAttribute("formData",f);

        }catch (MalformedURLException e){
            //todo
        }

        return "callback";
    }


    @RequestMapping("/gettoken")
    public String getToken(@RequestParam("code") String code, Model model){
        try {

            BacklogConfigure configure = new BacklogJpConfigure(spaceId).apiKey(apiKey);
            BacklogOAuthSupport support = new BacklogOAuthSupport(configure);
            support.setOAuthClientId(apiKey, apiSecret);

            //NullPointerException で落ちる
            AccessToken accessToken = support.getOAuthAccessToken(code);

        }catch (MalformedURLException e){

        }
        return "gettoken";
    }
}

