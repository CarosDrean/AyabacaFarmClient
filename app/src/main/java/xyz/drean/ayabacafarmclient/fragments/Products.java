package xyz.drean.ayabacafarmclient.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.drean.ayabacafarmclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Products extends Fragment {


    AppBarLayout appBarLayout = null;
    TabLayout tabLayout = null;
    ViewPager viewPager = null;

    public Products() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        if(savedInstanceState == null) {
            insertTabs(container);
            viewPager = v.findViewById(R.id.pager);
            populateViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appBarLayout.removeView(tabLayout);
    }

    private void insertTabs(ViewGroup container){
        View padre = (View) container.getParent();
        appBarLayout = padre.findViewById(R.id.appbar);
        tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#FFCCBC"), Color.parseColor("#FFFFFF"));
        appBarLayout.addView(tabLayout);
    }

    private void populateViewPager(ViewPager viewPager){
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(Categories.newInstance(0), getString(R.string.pastillas));
        adapter.addFragment(Categories.newInstance(1), getString(R.string.jarabes));
        adapter.addFragment(Categories.newInstance(2), getString(R.string.injectables));
        viewPager.setAdapter(adapter);
    }

    class AdaptadorSecciones extends FragmentStatePagerAdapter {
        List<Fragment> fragmentos = new ArrayList();
        List<String> titulos = new  ArrayList();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentos.add(fragment);
            titulos.add(title);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentos.get(i);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titulos.get(position);
        }
    }

}
