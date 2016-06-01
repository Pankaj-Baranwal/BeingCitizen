package com.beingcitizen.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;
import com.beingcitizen.R;
import com.beingcitizen.tab.SlidingTabLayout;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saransh on 19-06-2015.
 */
public class Campaign extends Fragment {

  private FragmentManager fragmentManager;
   private DialogFragment mMenuDialogFragment;
   // private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    String TITLES[] = {"All Campaign","Campaign Genre"};


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
        addFragment(new AllCampaign(), true, R.id.container);
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
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.icn_close);

        MenuObject laworder = new MenuObject("Public Law & Order");
        laworder.setResource(R.drawable.public_law_and_order);
        laworder.setBgColor(Color.alpha(R.color.color_white));
        laworder.setDividerColor(Color.alpha(R.color.color_white));
        laworder.setScaleType(ImageView.ScaleType.FIT_START);

        MenuObject police = new MenuObject("Police");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.police);
        police.setBitmap(b);
        police.setBgColor(Color.alpha(R.color.color_white));
        police.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject publichealth = new MenuObject("Public Health & Sanitation");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.public_health_and_sanitation));
        publichealth.setDrawable(bd);
        publichealth.setBgColor(Color.alpha(R.color.color_white));
        publichealth.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject government = new MenuObject("Local Government");
        government.setResource(R.drawable.local_government);
        government.setBgColor(Color.alpha(R.color.color_white));
        government.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject roadsbridges = new MenuObject("Communications – Roads & Bridges");
        roadsbridges.setResource(R.drawable.roads_and_bridges);
        roadsbridges.setBgColor(Color.alpha(R.color.color_white));
        roadsbridges.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject watersupply = new MenuObject("Water Supplies");
        watersupply.setResource(R.drawable.water_supplies);
        watersupply.setBgColor(Color.alpha(R.color.color_white));
        watersupply.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject industry = new MenuObject("Industries");
        industry.setResource(R.drawable.industries);
        industry.setBgColor(Color.alpha(R.color.color_white));
        industry.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject market = new MenuObject("Markets & Fairs");
        market.setResource(R.drawable.market);
        market.setBgColor(Color.alpha(R.color.color_white));
        market.setDividerColor(Color.alpha(R.color.color_white));

        MenuObject trade = new MenuObject("Trade & Commerce (within State)");
        trade.setResource(R.drawable.trade);
        trade.setBgColor(Color.alpha(R.color.color_white));
        trade.setDividerColor(Color.alpha(R.color.color_white));

       // MenuObject tax = new MenuObject("State Taxes (Electricity, Land, Roads, Toll)");
        //tax.setResource(R.drawable.industries);
        //tax.setBgColor(Color.alpha(R.color.color_white));
       // tax.setDividerColor(Color.alpha(R.color.color_white));

        menuObjects.add(close);
        menuObjects.add(laworder);
        menuObjects.add(police);
        menuObjects.add(publichealth);
        menuObjects.add(government);
        menuObjects.add(roadsbridges);
        menuObjects.add(watersupply);
        menuObjects.add(industry);
        menuObjects.add(market);
        menuObjects.add(trade);
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


 //   public void onMenuItemClick(View clickedView, int position) {
 //       Toast.makeText(Campaign.this.getActivity(), "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
  //      Log.e("campaign", "" + position);
  //  }


   // @Override
    // public void onStop() {
        // TODO Auto-generated method stub

    //    super.onStop();


   // }
    class MyPagerAdapter extends FragmentPagerAdapter{
        int ICONS[] = {R.mipmap.ic_notifications_off_white_24dp,R.drawable.aka};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                // Open FragmentTab1.java
                case 0:
                    AllCampaign fragmenttab2 = new AllCampaign();
                    return fragmenttab2;

                // Open FragmentTab2.java
                case 1:
                CampaignCategory fragmenttab = new CampaignCategory();
                return fragmenttab;
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
