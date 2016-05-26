package ngo.patrick.netccentricassignment;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ngo.patrick.netccentricassignment.model.Result;

/**
 * BlogListAdapter: Custom ListAdapter to bind data Data from the BlogPostModel data to each item in the ListView
 */
public class BlogPostListAdapter extends ArrayAdapter<Result>
{

    public BlogPostListAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }

    public BlogPostListAdapter(Context context, int resource, List<Result> items)
    {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_blogpost_row, null);
        }

        //Get blogpost data from specified position
        Result p = getItem(position);

        //Find appropriate layout components and display respective data
        if (p != null)
        {
            TextView idTextView = (TextView) v.findViewById(R.id.id);
            TextView captionTextView = (TextView) v.findViewById(R.id.caption);
            ImageView thumbnailView = (ImageView) v.findViewById(R.id.thumbnail);
            TextView createdOnTextView = (TextView) v.findViewById(R.id.createdOn);
            TextView updatedOnTextView = (TextView) v.findViewById(R.id.updatedOn);

            if (idTextView != null)
            {
                idTextView.setText(Integer.toString(p.getId()));

            }

            if (captionTextView != null) {
                captionTextView.setText(p.getCaption());
            }

            if (thumbnailView != null) {
                Picasso.with(getContext()).load(p.getThumbnail()).into(thumbnailView);
            }

            if (createdOnTextView != null) {
                createdOnTextView.setText(getContext().getResources().getString(R.string.created_on) + p.getCreatedAt());
            }

            if (updatedOnTextView != null) {
                updatedOnTextView.setText(getContext().getResources().getString(R.string.updated_on) + p.getUpdatedAt());
            }
        }

        return v;
    }
}