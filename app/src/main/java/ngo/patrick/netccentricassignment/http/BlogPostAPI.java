package ngo.patrick.netccentricassignment.http;

import ngo.patrick.netccentricassignment.model.BlogPost;
import ngo.patrick.netccentricassignment.model.BlogPostList;
import ngo.patrick.netccentricassignment.model.Result;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Patrick on 5/26/2016.
 */
public interface BlogPostAPI
{
    @GET("posts/")
    Call<BlogPostList> getAllBlogPosts();

    @GET("posts/{id}/")
    Call<BlogPost> getSingleBlogPost(
            @Path("id") String id);

    @POST("posts/")
    Call<Result> createBlogPost(
            @Body Result result);

    @FormUrlEncoded
    @POST("posts/")
    Call<Result> updateBlogPost(
            @Field("caption") String caption);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://demo6401519.mockable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
