package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.tabs.CoursesFragment;
import shavkunov.skorogovorun.lite.controller.tabs.ExercisesFragment;
import shavkunov.skorogovorun.lite.controller.tabs.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    public static final String LAST_DATE = "lastDate";

    private long lastDate;

    private Fragment fragment;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        setBottomBar();
    }

    private void setBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                String toolbarTitle = null;

                switch (tabId) {
                    case R.id.tab_home:
                        setExercisesFragment();
                        toolbarTitle = getString(R.string.exercises);
                        break;
                    case R.id.tab_course:
                        fragment = CoursesFragment.newInstance();
                        toolbarTitle = getString(R.string.courses);
                        break;
                    case R.id.tab_settings:
                        fragment = SettingsFragment.newInstance();
                        toolbarTitle = getString(R.string.settings);
                        break;
                }

                setFragments();

                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(toolbarTitle);
                }
            }
        });
    }

    private void setExercisesFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(LAST_DATE, lastDate);
        fragment = ExercisesFragment.newInstance();
        fragment.setArguments(bundle);
    }

    private void setFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ExercisesFragment.REQUEST_TONGUE_ACTIVITY:
                    lastDate = data.getLongExtra(TongueTwistersActivity.EXTRA_DATE_TONGUE, 0);
                    setExercisesFragment();
                    setFragments();
                    break;
                case ExercisesFragment.REQUEST_FAVORITE_TONGUE_ACTIVITY:
                    lastDate = data.getLongExtra(FavoriteTongueActivity.EXTRA_DATE_FAVORITE, 0);
                    setExercisesFragment();
                    setFragments();
                    break;
            }
        }
    }
}
