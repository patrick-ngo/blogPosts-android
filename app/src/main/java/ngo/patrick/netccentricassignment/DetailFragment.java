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
        FetchBlogDetailTask blogPostTask = new FetchBlogDetailTask(getActivity(), rootView);
        blogPostTask.execute(getActivity().getIntent().getStringExtra(BlogPostModel.INTENT_ID));

        return rootView;
    }


    public class FetchBlogDetailTask extends AsyncTask<String, Void, BlogPostModel> {
        private Context mContext;
        private View mRootView;
        private final String LOG_TAG = FetchBlogDetailTask.class.getSimpleName();

        public FetchBlogDetailTask (Context context, View rootView){
            mContext = context;
            mRootView = rootView;
        }

        /**
         * Take the String representing a single blogpost in JSON Format and
         * pull out the data we need to construct the BlogPostModel
         */
        private BlogPostModel getBlogPostDataFromJson(String blogpostsJsonStr)
                throws JSONException
        {
                JSONObject blogPostJson = new JSONObject(blogpostsJsonStr);
                BlogPostModel b = new BlogPostModel(blogPostJson);

                return b;
        }

        /**
         * Retrieve single blogpost data by Http Request
         * Retrieval done on separate thread to avoid cluttering main UI thread
         */
        @Override
        protected BlogPostModel doInBackground(String... params)
        {
            //blog post id must be provided
            if (params.length == 0)
            {
                return null;
            }

            //Call the http GET from method from provided API
            String forecastJsonStr = BlogPostHttpRequest.GetData(params[0]);

            try
            {
                return getBlogPostDataFromJson(forecastJsonStr);
            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once data blog data is retrieved,
         * Display to view
         */
        @Override
        protected void onPostExecute(BlogPostModel result)
        {
            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra(BlogPostModel.INTENT_ID))
            {
                String id = intent.getStringExtra(BlogPostModel.INTENT_ID);

                //display image of the blog post
                ImageView thumbnailImageView = ((ImageView) mRootView.findViewById(R.id.thumbnail));
                Picasso.with(getContext()).load(result.image).into(thumbnailImageView);

                //display caption of the blog post
                TextView captionTextView =  ((TextView) mRootView.findViewById(R.id.caption));
                captionTextView.setText(result.caption);

                //display creation date of the blog post
                TextView createdOnTextView =  ((TextView) mRootView.findViewById(R.id.createdOn));
                createdOnTextView.setText(getContext().getResources().getString(R.string.created_on) + " " + result.createdAt);

                //display update date of the blog post
                TextView updatedOnTextView = ((TextView) mRootView.findViewById(R.id.updatedOn));
                updatedOnTextView.setText(getContext().getResources().getString(R.string.updated_on) + " " + result.updatedAt);
            }
        }

    }
}