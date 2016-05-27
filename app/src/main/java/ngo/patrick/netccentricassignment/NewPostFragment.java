package ngo.patrick.netccentricassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import ngo.patrick.netccentricassignment.http.BlogPostAPI;
import ngo.patrick.netccentricassignment.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * New Post Fragment: Encapsulates taking the text from the EditText and posting it to the API.
 */
public class NewPostFragment extends Fragment
{
    private final String LOG_TAG = NewPostFragment.class.getSimpleName();

    public NewPostFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        final View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);

        Button newPostButton = (Button)rootView.findViewById(R.id.btn_new_post);

        newPostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get the EditText
                EditText newPostText = (EditText)rootView.findViewById(R.id.txt_new_post);

                //create new Result model to send
                Result newPost = new Result();
                newPost.setCaption( newPostText.getText().toString());

                //call the http POST method from API
                BlogPostAPI blogPostService = BlogPostAPI.retrofit.create(BlogPostAPI.class);
                final Call<Result> call = blogPostService.createBlogPost(newPost);

                call.enqueue(new Callback<Result>()
                {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response)
                    {
                        Log.v(LOG_TAG, new Gson().toJson(response));

                        //Fire an Intent to launch the Main Activity
                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t)
                    {
                        Log.e(LOG_TAG, t.getMessage());
                    }
                });

                Toast.makeText(getActivity(), newPostText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}