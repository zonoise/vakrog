package com.example.controller;

import com.example.forms.GetOauthAccessTokenForm;
import com.google.api.client.http.*;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Key;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;
import com.nulabinc.backlog4j.BacklogClient;
import com.nulabinc.backlog4j.BacklogClientFactory;
import com.nulabinc.backlog4j.User;
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

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by zonoise on 2017/08/23.
 */
@Controller
public class BacklogAuthController {
    private static final Logger logger = LoggerFactory.getLogger(BacklogAuthController.class);

    @Value("${backlog.apikey}")
    private String apiKey;

    @Value("${backlog.apisecret}")
    private String apiSecret;

    @RequestMapping("/spaceform")
    public String spaceForm(Model model){
        return "spaceForm";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(required = false,name="space") String space,
                        Model model,
                        HttpSession session){

        if(null == space || 0 == space.length() ){
            return "redirect:/spaceform";
        }else {
            session.setAttribute("space",space);
        }

        try {

            BacklogConfigure configure = new BacklogJpConfigure(space).apiKey(apiKey);

            BacklogOAuthSupport support = new BacklogOAuthSupport(configure);
            support.setOAuthClientId(apiKey,apiSecret);
            String url = support.getOAuthAuthorizationURL();
            model.addAttribute("url",url);

        }catch (MalformedURLException e){
            //todo
        }

        return "login";
    }

    @RequestMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           Model model,
    HttpSession session) throws IOException
    {

        String space = (String)session.getAttribute("space");

        logger.info("callback space:{}",space);
        try{
            BacklogConfigure configure = new BacklogJpConfigure(space).apiKey(apiKey);

            GenericData data = new GenericData();
            data.put("grant_type","authorization_code");
            data.put("code",code);
            data.put("client_id",apiKey);
            data.put("client_secret",apiSecret);

            OAuthAccessTokenResponse d = getOAuthAccessToken(configure.getOAuthAccessTokenURL(),data);
            if(null != d){
                logger.info("token:{}",d.toString());
            }else {
                logger.info("token is null:");
            }

            AccessToken accessToken = new AccessToken(d.accessToken,d.expiresIn,d.refreshToken);
            BacklogConfigure configureAccessToken = new BacklogJpConfigure(space).accessToken(accessToken);
            BacklogClient backlog = new BacklogClientFactory(configureAccessToken).newClient();
            User backlogUser = backlog.getMyself();
            logger.info("backloguser:{}",backlogUser.toString());
            model.addAttribute("space",space);
            model.addAttribute("name",backlogUser.getName());
            model.addAttribute("userId",backlogUser.getUserId());

        }catch (MalformedURLException e){
            //todo
        }

        return "callback";
    }

    private OAuthAccessTokenResponse getOAuthAccessToken(String url,GenericData data) throws IOException
    {

        HttpTransport httpTransport = new NetHttpTransport();
        try {
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl genericUrl = new GenericUrl(url);
            HttpContent httpContent =  new UrlEncodedContent(data);
            HttpRequest request = requestFactory.buildPostRequest(genericUrl,httpContent);
            request.setLoggingEnabled(true);
            request.setParser(new JacksonFactory().createJsonObjectParser());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType("application/x-www-form-urlencoded");
            request.setHeaders(headers);

            HttpResponse response = request.execute();
            logger.info("statuscode:{}",response.getStatusCode());

            if(response.isSuccessStatusCode()){
                OAuthAccessTokenResponse d = response.parseAs(OAuthAccessTokenResponse.class);

                logger.debug("OAuthAccessTokenResponse:{}",d);
                logger.debug("OAuthAccessTokenResponse token :{}",d.accessToken);

                return d;
            }
        }catch (java.io.IOException e){
            logger.debug("exeption:{}",e);
            throw e;
        } finally {
            httpTransport.shutdown();
           // transport.shutdown();
        }
        return null;
    }

    public static class OAuthAccessTokenResponse extends GenericData {
        @Key("token_type")
        public String tokenType;
        @Key("access_token")
        public String accessToken;
        @Key("expires_in")
        public long expiresIn;
        @Key("refresh_token")
        public String refreshToken;
    }
}
