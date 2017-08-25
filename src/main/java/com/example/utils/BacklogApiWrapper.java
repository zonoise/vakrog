package com.example.utils;

import com.nulabinc.backlog4j.BacklogClient;
import com.nulabinc.backlog4j.BacklogClientFactory;
import com.nulabinc.backlog4j.User;
import com.nulabinc.backlog4j.auth.AccessToken;
import com.nulabinc.backlog4j.conf.BacklogConfigure;
import com.nulabinc.backlog4j.conf.BacklogJpConfigure;

import java.net.MalformedURLException;

/**
 * Created by zonoise on 2017/08/25.
 */
public class BacklogApiWrapper {
    public static BacklogClient getClient(String space, AccessToken token) {
        try {
            BacklogConfigure configureAccessToken = new BacklogJpConfigure(space).accessToken(token);
            BacklogClient backlog = new BacklogClientFactory(configureAccessToken).newClient();
            return backlog;
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
