package shavkunov.skorogovorun.lite.controller;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.tabs.CoursesFragment;
import shavkunov.skorogovorun.lite.controller.tabs.ExercisesFragment;
import shavkunov.skorogovorun.lite.controller.tabs.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.viewpager_tab)
    SmartTabLayout viewPagerTab;

    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_text)
    TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        toolbarText.setVisibility(View.VISIBLE);

        setUpTabs();
    }

    private void setUpTabs() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.exercises, ExercisesFragment.class)
                .add(R.string.courses, CoursesFragment.class)
                .add(R.string.settings, SettingsFragment.class)
                .create());

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setDefaultTabTextColor(android.R.color.white);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_asses:
                setIntent("market://details?id=shavkunov.skorogovorun.lite");
                return true;
            case R.id.action_download:
                setIntent("https://yadi.sk/d/SAqBnk9q3Q5Ze7");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setIntent(String uriString) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uriString));
        Intent selectedApp = Intent.createChooser(intent, getString(R.string.select_app));
        startActivity(selectedApp);
    }
}
