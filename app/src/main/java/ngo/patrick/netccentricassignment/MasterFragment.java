/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ngo.patrick.netccentricassignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Master Fragment: Encapsulates fetching all the BlogPosts and displaying it as a ListView layout.
 */
public class MasterFragment extends Fragment
{
    private BlogPostListAdapter blogPostsListAdapter;

    public MasterFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.masterfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /**
         * Handle clicks on the action bar items
         * Clicking the Refresh button retrieves the data from the API to reflect any new blog posts
         */
        int id = item.getItemId();
        if (id == R.id.action_refresh)
        {
            FetchAllBlogPostsTask blogPostsTask = new FetchAllBlogPostsTask();
            blogPostsTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        //Create a BlogPostListAdapter and bind it to the listview in the layout
        blogPostsListAdapter = new BlogPostListAdapter(getActivity(), R.layout.list_item_blogpost_row);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_blogposts);
        listView.setAdapter(blogPostsListAdapter);

        //Create the click listener for the listview items (clicking on a blogpost leads to the Detail page of that blog post)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                //Fire an Intent to launch the Detail Activity, with the blogpost id as a parameter
                BlogPostModel selectedBlogPost = blogPostsListAdapter.getItem(position);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(BlogPostModel.INTENT_ID, selectedBlogPost.id);
                startActivity(detailIntent);
            }
        });

        //Get initial data to display blog posts
        FetchAllBlogPostsTask blogPostsTask = new FetchAllBlogPostsTask();
        blogPostsTask.execute();

        return rootView;
    }


    public class FetchAllBlogPostsTask extends AsyncTask<String, Void, BlogPostModel[]> {

        private final String LOG_TAG = FetchAllBlogPostsTask.class.getSimpleName();


        /**
         * Take the String representing the list of all blogposts in JSON Format and
         * pull out the data we need to construct the list of BlogPostModel
         */
        private BlogPostModel[] getBlogPostsDataFromJson(String blogpostsJsonStr)
                throws JSONException {

            JSONObject allBlogPostsJson = new JSONObject(blogpostsJsonStr);
            JSONArray blogPostArray = allBlogPostsJson.getJSONArray(BlogPostModel.JSON_RESULTS);

            int numBlogPosts = blogPostArray.length();

            BlogPostModel[] blogposts = new BlogPostModel[numBlogPosts];

            for(int i = 0; i < blogPostArray.length(); i++)
            {
                JSONObject singleBlogPost = blogPostArray.getJSONObject(i);
                BlogPostModel b = new BlogPostModel(singleBlogPost);
                blogposts[i] = b;
            }

            return blogposts;
        }

        /**
         * Retrieve all blogpost data by Http Request
         * Retrieval done on separate thread to avoid cluttering main UI thread
         */
        @Override
        protected BlogPostModel[] doInBackground(String... params)
        {
            //Call the http GET from method from provided API
            String allBlogPostsJsonStr = BlogPostHttpRequest.GetData("");

            try
            {
                return getBlogPostsDataFromJson(allBlogPostsJsonStr);
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
         * Add to List Adapter for display on the view
         */
        @Override
        protected void onPostExecute(BlogPostModel[] result)
        {
            if (result != null)
            {
                blogPostsListAdapter.clear();
                for(BlogPostModel singleBlogPost : result)
                {
                    blogPostsListAdapter.add(singleBlogPost);
                }
            }
        }

    }
}