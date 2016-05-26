package ngo.patrick.netccentricassignment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ngo.patrick.netccentricassignment.http.BlogPostAPI;
import ngo.patrick.netccentricassignment.model.BlogPost;
import ngo.patrick.netccentricassignment.model.BlogPostList;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Detail Fragment: Encapsulates fetching the details of a single blog post and displaying it on the layout.
 */
public class DetailFragment extends Fragment
{

    public DetailFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Get initial data to display blog post details
        BlogPostAPI blogPostService = BlogPostAPI.retrofit.create(BlogPostAPI.class);
        final Call<BlogPost> call = blogPostService.getSingleBlogPost(getActivity().getIntent().getStringExtra(BlogPostModel.INTENT_ID));

        FetchBlogDetailTask blogPostTask = new FetchBlogDetailTask(getActivity(), rootView);
        blogPostTask.execute(call);

        return rootView;
    }


    public class FetchBlogDetailTask extends AsyncTask<Call, Void, BlogPost> {
        private Context mContext;
        private View mRootView;
        private final String LOG_TAG = FetchBlogDetailTask.class.getSimpleName();

        public FetchBlogDetailTask (Context context, View rootView){
            mContext = context;
            mRootView = rootView;
        }

        /**
         * Retrieve single blogpost data by Http Request
         * Retrieval done on separate thread to avoid cluttering main UI thread
         */
        @Override
        protected BlogPost doInBackground(Call ... params)
        {
            try
            {
                Call<BlogPost> call = params[0];
                Response<BlogPost> response = call.execute();

                return response.body();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Once data blog data is retrieved,
         * Display to view
         */
        @Override
        protected void onPostExecute(BlogPost result)
        {
            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra(BlogPostModel.INTENT_ID))
            {
                String id = intent.getStringExtra(BlogPostModel.INTENT_ID);

                //display image of the blog post
                ImageView thumbnailImageView = ((ImageView) mRootView.findViewById(R.id.thumbnail));
                Picasso.with(getContext()).load(result.getImage()).into(thumbnailImageView);

                //display caption of the blog post
                TextView captionTextView =  ((TextView) mRootView.findViewById(R.id.caption));
                captionTextView.setText(result.getCaption());

                //display creation date of the blog post
                TextView createdOnTextView =  ((TextView) mRootView.findViewById(R.id.createdOn));
                createdOnTextView.setText(getContext().getResources().getString(R.string.created_on) + " " + result.getCreatedAt());

                //display update date of the blog post
                TextView updatedOnTextView = ((TextView) mRootView.findViewById(R.id.updatedOn));
                updatedOnTextView.setText(getContext().getResources().getString(R.string.updated_on) + " " + result.getUpdatedAt());
            }
        }

    }
}