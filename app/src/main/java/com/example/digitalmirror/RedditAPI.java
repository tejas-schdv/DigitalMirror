package com.example.digitalmirror;


import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.example.digitalmirror.model.Feed;

/**
 * Created by User on 5/1/2017.
 */

public interface RedditAPI {

    String BASE_URL = "https://www.reddit.com/r/news/";

    @Headers("Content-Type: application/json")
    @GET(".json")
    Call<Feed> getData();


}
