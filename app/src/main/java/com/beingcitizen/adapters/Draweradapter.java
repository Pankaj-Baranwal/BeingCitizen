package com.beingcitizen.adapters;

/**
 * Created by saransh on 14-06-2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingcitizen.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp1 on 28-12-2014.
 */
public class Draweradapter extends RecyclerView.Adapter<Draweradapter.ViewHolder>{


    public static int mSelectedPosition = 1;
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java
    int image=R.drawable.ic_profile2;
    private String name;        //String Resource for header View Name
    private int profileid;        //int Resource for header view profile picture
    private String email, mlaName, mlaConsti;       //String Resource for header view email
    public static boolean mlaExists = true;
    static ImageView arrows;
    public static CircleImageView mla_image, profile;
    static TextView mla_name;
    static TextView mla_consti;

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;
        Context c;
        TextView textView;
        ImageView imageView;
        TextView Name;

        public ViewHolder(final View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            itemView.setClickable(true);
            // Bundle extras = this.getIntent().getExtras();
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{
                Name = (TextView) itemView.findViewById(R.id.username);         // Creating Text View object from header.xml for name
                mla_name = (TextView) itemView.findViewById(R.id.mla_name);
                mla_consti = (TextView) itemView.findViewById(R.id.mla_constituency);
                mla_image = (CircleImageView) itemView.findViewById(R.id.mlaImage);
                arrows = (ImageView) itemView.findViewById(R.id.arrows);
                if (!mlaExists){
                    mla_consti.setVisibility(View.GONE);
                    arrows.setVisibility(View.GONE);
                    mla_image.setVisibility(View.GONE);
                    mla_name.setVisibility(View.GONE);
                }
                profile = (CircleImageView)itemView.findViewById(R.id.profile_picture);
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }


    public Draweradapter(String Titles[], int Icons[], String Name, String Email, String mla_name_txt, String mla_consti_txt, int Profile){ // Draweradapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        email = Email;
        if (!mla_name_txt.contentEquals("No_mla_id")) {
            mlaConsti = mla_consti_txt;
        }else{
            mlaExists = false;
        }
        profileid=Profile;
        //in adapter

    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        parent.setBackgroundColor(Color.BLACK);

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout
            v.setBackgroundColor(Color.BLACK);
            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view
            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons

            final int normalColor = Color.WHITE;
            final int selectedColor = Color.parseColor("#EE7E1B");//red #C43425  blue #33B5E5
            if (mSelectedPosition == position) {
                holder.textView.setTextColor(selectedColor);
                holder.imageView.setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                holder.textView.setTextColor(normalColor);
                holder.imageView.setColorFilter(normalColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        else{

           // holder.profile.setImageResource(image);           // Similarly we set the resources for header view
            holder.Name.setText(name);
            if (mlaExists)
            mla_consti.setText(mlaConsti);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
