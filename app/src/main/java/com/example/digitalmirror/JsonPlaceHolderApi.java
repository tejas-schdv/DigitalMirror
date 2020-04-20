package com.example.digitalmirror;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET(".json")
    Call<List<Post>> getPosts();
}
