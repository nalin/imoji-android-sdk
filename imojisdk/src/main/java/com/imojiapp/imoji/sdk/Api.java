package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.Config;

/**
 * Created by sajjadtabib on 5/28/15.
 */
public class Api{

    public interface Endpoints {
        String IMOJI_FEATURED_FETCH = Config.BASE_URL + "/imoji/featured/fetch";
        String IMOJI_SEARCH = Config.BASE_URL + "/imoji/search";
        String IMOJI_FETCHMULTIPLE = Config.BASE_URL + "/imoji/fetchMultiple";
        String IMOJI_CATEGORIES_FETCH = Config.BASE_URL + "/imoji/categories/fetch";
        String USER_IMOJI_FETCH = Config.BASE_URL + "/user/imoji/fetch";
        String USER_IMOJI_COLLECTION_ADD = Config.BASE_URL + "/user/imoji/collection/add";
        String OAUTH_TOKEN = Config.BASE_URL + "/oauth/token";
        String OAUTH_EXTERNAL_GETIDPAYLOAD = Config.BASE_URL + "/oauth/external/getIdPayload";
    }

    public interface Params {
        String OFFSET = "offset";
        String NUMRESULTS = "numResults";
        String QUERY = "query";
        String ACCESS_TOKEN = "access_token";
        String IDS = "ids";
        String CLASSIFICATION = "classification";
        String IMOJIID = "imojiId";
        String GRANT_TYPE = "grant_type";
        String REFRESH_TOKEN = "refresh_token";
        String CLIENTID = "clientId";
    }
}