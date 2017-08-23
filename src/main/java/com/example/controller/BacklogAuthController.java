package com.example.controller;

import com.example.forms.GetOauthAccessTokenForm;
import com.google.api.client.http.*;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;


import com.google.api.client.util.GenericData;
import com.nulabinc.backlog4j.auth.AccessToken;
import com.nulabinc.backlog4j.auth.BacklogOAuthSupport;
import com.nulabinc.backlog4j.conf.BacklogConfigure;
import com.nulabinc.backlog4j.conf.BacklogJpConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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
    public String callback(@RequestParam("code") String code, Model model) throws IOException {
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

            GenericData data = new GenericData();
            data.put("grant_type","authorization_code");
            data.put("code",code);
            data.put("client_id",apiKey);
            data.put("client_secret",apiSecret);

            OAuthAccessTokenResponse d = getOAuthAccessToken(configure.getOAuthAccessTokenURL(),data);
            if(null != d){
                logger.info("token:{}",d.toString());
            }
        }catch (MalformedURLException e){
            //todo
        }

        return "callback";
    }

    private OAuthAccessTokenResponse getOAuthAccessToken(String url,GenericData data) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        try {
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl genericUrl = new GenericUrl(url);
            HttpContent httpContent =  new UrlEncodedContent(data);
            HttpRequest request = requestFactory.buildPostRequest(genericUrl,httpContent);
            request.setLoggingEnabled(true);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType("application/x-www-form-urlencoded");
            request.setHeaders(headers);
            HttpResponse response = request.execute();
            System.out.print(response.getStatusCode());
            if(response.isSuccessStatusCode()){
                String responseString = response.parseAsString();
                logger.info("response:{}",responseString);
                OAuthAccessTokenResponse d = response.parseAs(OAuthAccessTokenResponse.class);
                return d;
            }
        }catch (java.io.IOException e){

        } finally {
            httpTransport.shutdown();
        }
        return null;
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

class OAuthAccessTokenResponse extends GenericData {
    @Key("token_type")
    public String tokenType;
    @Key("access_token")
    public String accessToken;
    @Key("expires_in")
    public Integer expiresIn;
    @Key("refresh_token")
    public String refreshToken;
}