package com.marcinmejner.instaclone.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.models.Comment;
import com.marcinmejner.instaclone.models.UserAccountSettings;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "CommentListAdapter";

    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContex;

    public CommentListAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContex = context;
        layoutResource = resource;
    }

    private static class ViewHolder{

        TextView comment, username, timeStamp, reply, likes;
        CircleImageView profileImage;
        ImageView like;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            holder.comment = convertView.findViewById(R.id.comment);
            holder.username = convertView.findViewById(R.id.commentUsername);
            holder.timeStamp = convertView.findViewById(R.id.comment_time_posted);
            holder.reply = convertView.findViewById(R.id.comment_reply);
            holder.like = convertView.findViewById(R.id.comment_like);
            holder.profileImage = convertView.findViewById(R.id.comment_profile_image);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.comment.setText(getItem(position).getComment());

        //set timeStamp defference
        String timeStampDifference = getTimeStampDifference(getItem(position));
        if(!timeStampDifference.equals("0")){
            holder.timeStamp.setText(timeStampDifference + "d");
        }else{
            holder.timeStamp.setText("Today");
        }

        //set username and profileImage
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(mContex.getString(R.string.dbname_user_account_settings))
                .orderByChild(mContex.getString(R.string.field_user_id))
                .equalTo(getItem(position).getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    holder.username.setText(singleSnapshot.getValue(UserAccountSettings.class).getUsername());

                    ImageLoader imageLoader= ImageLoader.getInstance();
                    imageLoader.displayImage(singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
                            holder.profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: querry canceled");
            }
        });

        try{
            if(position == 0){
                holder.like.setVisibility(View.GONE);
                holder.likes.setVisibility(View.GONE);
                holder.reply.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            Log.e(TAG, "getView: NullPointerException" + e.getMessage());
        }


        return convertView;
    }

    /**
     * Zwraca String reprezentujacy ile dni temu został utworzony post
     *
     * @return
     */
    private String getTimeStampDifference(Comment comment) {
        Log.d(TAG, "getTimeStampDifference: gettimg timestamp");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = c.getTime();
        sdf.format(today);
        Date timeStamp;
        final String photoTimeStamp = comment.getDate_created();

        try {
            timeStamp = sdf.parse(photoTimeStamp);
            difference = String.valueOf(Math.round(today.getTime() - timeStamp.getTime()) / 1000 / 60 / 60 / 24);
        } catch (ParseException e) {
            Log.e(TAG, "getTimeStampDifference: " + e.getMessage());
            difference = "0";
        }

        return difference;
    }

}
