package com.beingcitizen.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.beingcitizen.R;
import com.beingcitizen.interfaces.adapterUpdate;
import com.beingcitizen.interfaces.retrieveCamp;
import com.beingcitizen.tab.SlidingTabLayout;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saransh on 19-06-2015.
 */
public class Campaign extends Fragment implements retrieveCamp {

  private FragmentManager fragmentManager;
   private DialogFragment mMenuDialogFragment;
   // private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    String selected = "";
    List<MenuObject> menuObjects;
    public static AllCampaign alc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewpager_main, container, false);
        mPager= (ViewPager) view.findViewById(R.id.viewPager);
        mPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mTabs=(SlidingTabLayout) view.findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        setHasOptionsMenu(true);
        initMenuFragment();
        alc = new AllCampaign();
        addFragment(alc, true, R.id.container);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.opaque_red);
            }


        });

       // mTabs.setViewPager(mPager);
        return view;

    }
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        menuParams.setAnimationDuration(60);
        menuParams.setAnimationDelay(100);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }
    private List<MenuObject> getMenuObjects() {

        menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.icn_close);

        MenuObject laworder = new MenuObject("Law and Order");
        laworder.setResource(R.drawable.public_law_and_order);
        laworder.setBgColor(Color.alpha(R.color.color_white));
        laworder.setDividerColor(Color.alpha(R.color.color_white));
        laworder.setScaleType(ImageView.ScaleType.FIT_START);

        MenuObject phs = new MenuObject("Public Health and Sanitation");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.public_health_and_sanitation);
        phs.setBitmap(b);
        phs.setBgColor(Color.alpha(R.color.color_white));
        phs.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject communication = new MenuObject("Communication");
        communication.setResource(R.drawable.debates);
        communication.setBgColor(Color.alpha(R.color.color_white));
        communication.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject watersupply = new MenuObject("Water-Irrigation,Drainage,Embankments");
        watersupply.setResource(R.drawable.water_supplies);
        watersupply.setBgColor(Color.alpha(R.color.color_white));
        watersupply.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject land = new MenuObject("Lands, Agriculture");
        land.setResource(R.drawable.water_supplies);
        land.setBgColor(Color.alpha(R.color.color_white));
        land.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject trade = new MenuObject("Trade,Commerce,Employment");
        trade.setResource(R.drawable.market);
        trade.setBgColor(Color.alpha(R.color.color_white));
        trade.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject environ = new MenuObject("Environment and Holticulture");
        environ.setResource(R.drawable.water_supplies);
        environ.setBgColor(Color.alpha(R.color.color_white));
        environ.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject market = new MenuObject("Tourism, Art and Culture");
        market.setResource(R.drawable.market);
        market.setBgColor(Color.alpha(R.color.color_white));
        market.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject power = new MenuObject("Power");
        trade.setResource(R.drawable.industries);
        trade.setBgColor(Color.alpha(R.color.color_white));
        trade.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject corruption = new MenuObject("Corruption/Vigillance");
        corruption.setResource(R.drawable.police);
        corruption.setBgColor(Color.alpha(R.color.color_white));
        corruption.setDividerColor(Color.alpha(R.color.color_white));
       // MenuObject tax = new MenuObject("State Taxes (Electricity, Land, Roads, Toll)");
        //tax.setResource(R.drawable.industries);
        //tax.setBgColor(Color.alpha(R.color.color_white));
       // tax.setDividerColor(Color.alpha(R.color.color_white));

        menuObjects.add(close);
        menuObjects.add(laworder);
        menuObjects.add(phs);
        menuObjects.add(communication);
        menuObjects.add(watersupply);
        menuObjects.add(land);
        menuObjects.add(trade);
        menuObjects.add(environ);
        menuObjects.add(market);
        menuObjects.add(power);
        menuObjects.add(corruption);
       // menuObjects.add(tax);
        return menuObjects;
    }
    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        getActivity().invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = getChildFragmentManager().popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (getChildFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(getChildFragmentManager(), ContextMenuDialogFragment.TAG);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

   // set this to set according to category


//    public void onMenuItemClick(View clickedView, int position) {
////        selected = menuObjects.get(position).getTitle();
////        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
////        RetrieveCampaign rtC = new RetrieveCampaign(Campaign.this);
////        String uid = sp.getString("id", "16");
////        rtC.execute(uid);
////        Toast.makeText(Campaign.this.getActivity(), "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void retrieve(JSONObject param) {
        try {
            if (param.getJSONArray("campaigns")!=null && param.getJSONArray("campaigns").length()!=0) {
                JSONArray jA;
                JSONArray jASorted = new JSONArray();
                jA = param.getJSONArray("campaigns");
                for (int i=0; i<jA.length(); i++)
                    if (jA.getJSONObject(i).getString("tags").contentEquals(selected))
                        jASorted.put(jA.getJSONObject(i));
                adapterUpdate upd = new AllCampaign();
                upd.updateAdapt(jA);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter{
        int ICONS[] = {R.mipmap.ic_notifications_off_white_24dp,R.drawable.aka};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("POSITION", position+"");
            switch (position) {

                // Open FragmentTab1.java
                case 0:
                    return new AllCampaign();

                // Open FragmentTab2.java
                case 1:
                    return new CampaignCategory();
                // Open FragmentTab3.java

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable=getResources().getDrawable(ICONS[position]);
            drawable.setBounds(0,0,48,48);
            ImageSpan imageSpan=new ImageSpan(drawable);
            SpannableString spannableString=new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}