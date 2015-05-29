package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.imojiapp.imoji.sdk.networking.responses.AddImojiToCollectionResponse;
import com.imojiapp.imoji.sdk.networking.responses.BasicResponse;
import com.imojiapp.imoji.sdk.networking.responses.ErrorResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetUserImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by sajjadtabib on 4/6/15.
 */
class IonNetApiHandle extends ImojiNetworkingInterface {

    private static final String LOG_TAG = ImojiNetApiHandle.class.getSimpleName();
    private Context mContext;

    public IonNetApiHandle(Context context) {
        mContext = context;
    }

    private Builders.Any.B setStandardHeaders(Builders.Any.B requestBuilder) {
        return requestBuilder.addHeader("x-client-version", "2.0.0")
                .addHeader("x-client-model", "android")
                .addHeader("x-client-os-version", Build.VERSION.RELEASE);
    }


    void getFeaturedImojis(int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> callback) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        setStandardHeaders(Ion.with(mContext)
                .load("GET", Api.Endpoints.IMOJI_FEATURED_FETCH))
                .addQuery(Api.Params.ACCESS_TOKEN, apiToken)
                .addQuery(Api.Params.OFFSET, String.valueOf(offset))
                .addQuery(Api.Params.NUMRESULTS, count)
                .as(new TypeToken<ImojiSearchResponse>() {
                })
                .setCallback(new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }


    void searchImojis(String query, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> callback) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        setStandardHeaders(Ion.with(mContext)
                .load("GET", Api.Endpoints.IMOJI_SEARCH))
                .addQuery(Api.Params.ACCESS_TOKEN, apiToken)
                .addQuery(Api.Params.QUERY, query)
                .addQuery(Api.Params.OFFSET, String.valueOf(offset))
                .addQuery(Api.Params.NUMRESULTS, count)
                .as(new TypeToken<ImojiSearchResponse>() {
                })
                .setCallback(new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));

    }

    void getImojiCategories(final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);

        setStandardHeaders(Ion.with(mContext)
                .load("GET", Api.Endpoints.IMOJI_CATEGORIES_FETCH))
                .addQuery(Api.Params.ACCESS_TOKEN, apiToken)
                .addQuery(Api.Params.CLASSIFICATION, ImojiCategory.Classification.NONE)
                .as(new TypeToken<GetCategoryResponse>() {
                })
                .setCallback(new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    void getImojiCategories(String classification, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        setStandardHeaders(Ion.with(mContext)
                .load("GET", Api.Endpoints.IMOJI_CATEGORIES_FETCH))
                .addQuery(Api.Params.ACCESS_TOKEN, apiToken)
                .addQuery(Api.Params.CLASSIFICATION, classification)
                .as(new TypeToken<GetCategoryResponse>() {
                })
                .setCallback(new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    void getUserImojis(com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        setStandardHeaders(Ion.with(mContext)
                .load("GET", Api.Endpoints.USER_IMOJI_FETCH))
                .addQuery(Api.Params.ACCESS_TOKEN, apiToken)
                .as(new TypeToken<GetUserImojiResponse>() {
                })
                .setCallback(new CallbackWrapper<GetUserImojiResponse, List<Imoji>>(cb));
    }

    void getImojisById(List<String> ids, com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        setStandardHeaders(Ion.with(mContext)
                .load("POST", Api.Endpoints.IMOJI_FETCHMULTIPLE))
                .setBodyParameter(Api.Params.ACCESS_TOKEN, apiToken)
                .setBodyParameter(Api.Params.IDS, TextUtils.join(",", ids))
                .as(new TypeToken<FetchImojisResponse>() {
                })
                .setCallback(new CallbackWrapper<FetchImojisResponse, List<Imoji>>(cb));

    }

    void addImojiToUserCollection(String imojiId, com.imojiapp.imoji.sdk.Callback<String, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        setStandardHeaders(Ion.with(mContext)
                .load("POST", Api.Endpoints.USER_IMOJI_COLLECTION_ADD))
                .setBodyParameter(Api.Params.ACCESS_TOKEN, apiToken)
                .setBodyParameter(Api.Params.IMOJIID, imojiId)
                .as(new TypeToken<AddImojiToCollectionResponse>() {
                })
                .setCallback(new CallbackWrapper<AddImojiToCollectionResponse, String>(cb) {
                    @Override
                    public void onCompleted(Exception e, AddImojiToCollectionResponse result) {
                        super.onCompleted(e, result);
                        if (result != null && result.isSuccess()) {
                            //broadcast a sync intent
                            Intent intent = new Intent();
                            intent.setAction(ExternalIntents.Actions.INTENT_REQUEST_SYNC);
                            intent.addCategory(ExternalIntents.Categories.EXTERNAL_CATEGORY);
                            mContext.sendBroadcast(intent);
                        }
                    }
                });
    }

    GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken) {
        String grantType = "client_credentials";
        if (refreshToken != null) {
            grantType = "refresh_token";
        }
        try {
            Log.d(LOG_TAG, "calling ion");
            return setStandardHeaders(Ion.with(mContext).load("POST", Api.Endpoints.OAUTH_TOKEN))
                    .addHeader("Authorization", "Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE))
                    .setBodyParameter(Api.Params.GRANT_TYPE, grantType)
                    .setBodyParameter(Api.Params.REFRESH_TOKEN, refreshToken)
                    .as(new TypeToken<GetAuthTokenResponse>() {
                    })
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    void requestExternalOauth(String clientId, com.imojiapp.imoji.sdk.Callback<ExternalOauthPayloadResponse, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        setStandardHeaders(Ion.with(mContext)
                .load("POST", Api.Endpoints.OAUTH_EXTERNAL_GETIDPAYLOAD))
                .setBodyParameter(Api.Params.ACCESS_TOKEN, apiToken)
                .setBodyParameter(Api.Params.CLIENTID, clientId)
                .as(new TypeToken<ExternalOauthPayloadResponse>() {
                })
                .setCallback(new CallbackWrapper<ExternalOauthPayloadResponse, ExternalOauthPayloadResponse>(cb));
    }

    class CallbackWrapper<T extends BasicResponse<V>, V> implements Callback<T>, FutureCallback<T> {

        private com.imojiapp.imoji.sdk.Callback<V, String> mCallback;

        public CallbackWrapper(com.imojiapp.imoji.sdk.Callback callback) {
            mCallback = callback;
        }

        @Override
        public void success(T result, Response response) {
            if (result.isSuccess()) {
                mCallback.onSuccess(result.getPayload());
            } else {
                mCallback.onFailure(result.status);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
            if (error.getBody() != null) {
                try {
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    Type type = new TypeToken<ErrorResponse>() {
                    }.getType();
                    ErrorResponse response = Utils.gson().fromJson(json, type);
                    mCallback.onFailure(response.getPayload());
                } catch (JsonParseException e) {
                    e.printStackTrace();
                    mCallback.onFailure(Status.NETWORK_ERROR);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    mCallback.onFailure(Status.NETWORK_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onFailure(Status.NETWORK_ERROR);
                }

            } else {
                mCallback.onFailure(Status.NETWORK_ERROR);
            }
        }

        //Ion Implementation
        @Override
        public void onCompleted(Exception e, T result) {
            if (result != null && result.isSuccess()) {
                mCallback.onSuccess(result.getPayload());
            } else if (result != null && !result.isSuccess()) {
                mCallback.onFailure(result.status);
            } else if (e != null) {
                e.printStackTrace();
                mCallback.onFailure(Status.NETWORK_ERROR);
            } else {
                mCallback.onFailure(Status.NETWORK_ERROR);
            }
        }
    }

}
