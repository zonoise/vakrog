package com.example.forms;

/**
 * Created by zonoise on 2017/08/23.
 */
public class GetOauthAccessTokenForm{

    String rest_url;
    String grant_type;
    String client_id;
    String code;
    String client_secret;
    String redirect_uri;

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getRest_url() {
        return rest_url;
    }

    public void setRest_url(String rest_url) {
        this.rest_url = rest_url;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
