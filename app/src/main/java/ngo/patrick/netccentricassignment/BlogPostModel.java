package ngo.patrick.netccentricassignment;

import org.json.JSONObject;
import org.json.JSONException;


/**
 * Created by Patrick on 5/18/2016.
 */
public class BlogPostModel
{
    public String id = "";
    public String caption = "";
    public String thumbnail = "";
    public String image = "";
    public String createdAt = "";
    public String updatedAt = "";

    //Used to pass ID of blog post through Intent parameter
    public static String INTENT_ID = "blogpost_id";

    // These are the names of the JSON objects that need to be extracted.
    public static String JSON_RESULTS = "results";
    public static String JSON_ID = "id";
    public static String JSON_CAPTION = "caption";
    public static String JSON_THUMBNAIL = "thumbnail";
    public static String JSON_IMAGE = "image";
    public static String JSON_CREATED_ON = "createdAt";
    public static String JSON_UPDATED_ON = "updatedAt";

    public BlogPostModel()
    {
    }

    //Construct a BlogPostModel from a describing JSON object
    public BlogPostModel(JSONObject blogPostJSON) throws JSONException
    {
        if (blogPostJSON.has(BlogPostModel.JSON_ID))
        {
            this.id = blogPostJSON.getString(BlogPostModel.JSON_ID);
        }
        if (blogPostJSON.has(BlogPostModel.JSON_CAPTION))
        {
            this.caption = blogPostJSON.getString(BlogPostModel.JSON_CAPTION);
        }
        if (blogPostJSON.has(BlogPostModel.JSON_THUMBNAIL))
        {
            this.thumbnail = blogPostJSON.getString(BlogPostModel.JSON_THUMBNAIL);
        }
        if (blogPostJSON.has(BlogPostModel.JSON_IMAGE))
        {
            this.image = blogPostJSON.getString(BlogPostModel.JSON_IMAGE);
        }
        if (blogPostJSON.has(BlogPostModel.JSON_CREATED_ON))
        {
            this.createdAt = blogPostJSON.getString(BlogPostModel.JSON_CREATED_ON);
        }
        if (blogPostJSON.has(BlogPostModel.JSON_UPDATED_ON))
        {
            this.updatedAt = blogPostJSON.getString(BlogPostModel.JSON_UPDATED_ON);
        }
    }
}
