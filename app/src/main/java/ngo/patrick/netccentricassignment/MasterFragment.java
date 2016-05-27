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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import ngo.patrick.netccentricassignment.http.BlogPostAPI;
import ngo.patrick.netccentricassignment.model.BlogPostList;
import ngo.patrick.netccentricassignment.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            BlogPostAPI blogPostService = BlogPostAPI.retrofit.create(BlogPostAPI.class);
            final Call<BlogPostList> call = blogPostService.getAllBlogPosts();
            FetchAllBlogPostsTask blogPostsTask = new FetchAllBlogPostsTask();
            blogPostsTask.execute(call);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        //Create a BlogPostListAdapter and bind it to the listview in the layout
        blogPostsListAdapter = new BlogPostListAdapter(getActivity(), R.layout.list_item_blogpost_row);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_blogposts);
        listView.setAdapter(blogPostsListAdapter);

        //Create the click listener for the listview items (clicking on a blogpost leads to the Detail page of that blog post)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Fire an Intent to launch the Detail Activity, with the blogpost id as a parameter
                Result selectedBlogPost = blogPostsListAdapter.getItem(position);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(BlogPostModel.INTENT_ID, Integer.toString(selectedBlogPost.getId()));
                startActivity(detailIntent);
            }
        });

        //Get initial data to display blog posts
        BlogPostAPI blogPostService = BlogPostAPI.retrofit.create(BlogPostAPI.class);
        final Call<BlogPostList> call = blogPostService.getAllBlogPosts();

        new FetchAllBlogPostsTask().execute(call);



       Button newPostButton = (Button)rootView.findViewById(R.id.btn_new_post);

        newPostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Fire an Intent to launch the New Post Activity
                Intent newPostIntent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });

        return rootView;
    }


    private class FetchAllBlogPostsTask extends AsyncTask<Call, Void, BlogPostList>
    {

        private final String LOG_TAG = FetchAllBlogPostsTask.class.getSimpleName();
        /**
         * Retrieve all blogpost data by Http Request
         * Retrieval done on separate thread to avoid cluttering main UI thread
         */
        @Override
        protected BlogPostList doInBackground(Call... params)
        {
            try
            {
                Call<BlogPostList> call = params[0];
                Response<BlogPostList> response = call.execute();


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
         * Add to List Adapter for display on the view
         */
        @Override
        protected void onPostExecute(BlogPostList results)
        {
            if (results != null)
            {
                blogPostsListAdapter.clear();
                for (Result singleBlogPost : results.getResults())
                {
                    blogPostsListAdapter.add(singleBlogPost);
                }
            }
        }

    }
}


