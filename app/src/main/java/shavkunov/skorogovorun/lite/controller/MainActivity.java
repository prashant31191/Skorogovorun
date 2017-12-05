package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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
    public static final String POSTURE_PERCENT = "posturePercent";
    public static final String BREATH_PERCENT = "breathPercent";
    public static final String VOICE_PERCENT = "voicePercent";
    public static final String DICTION_PERCENT = "dictionPercent";

    public static final String POSTURE_SAVED = "postureSaved";
    public static final String BREATH_SAVED = "breathSaved";
    public static final String VOICE_SAVED = "voiceSaved";
    public static final String DICTION_SAVED = "dictionSaved";

    private long lastDate;

    private Fragment fragment;
    private SharedPreferences preferences;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                        setCoursesFragment();
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

    private void setCoursesFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(POSTURE_PERCENT,
                preferences.getInt(POSTURE_SAVED, 0));
        bundle.putInt(BREATH_PERCENT,
                preferences.getInt(BREATH_SAVED, 0));
        bundle.putInt(VOICE_PERCENT,
                preferences.getInt(VOICE_SAVED, 0));
        bundle.putInt(DICTION_PERCENT,
                preferences.getInt(DICTION_SAVED, 0));

        fragment = CoursesFragment.newInstance();
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

            switch (requestCode) {
                case CoursesFragment.REQUEST_POSTURE:
                    preferences.edit().putInt(POSTURE_SAVED,
                            data.getIntExtra(CourseActivity.EXTRA_POSTURE, 0)).apply();
                    setCoursesFragment();
                    setFragments();
                    break;
                case CoursesFragment.REQUEST_BREATH:
                    preferences.edit().putInt(BREATH_SAVED,
                            data.getIntExtra(CourseActivity.EXTRA_BREATH, 0)).apply();
                    setCoursesFragment();
                    setFragments();
                    break;
                case CoursesFragment.REQUEST_VOICE:
                    preferences.edit().putInt(VOICE_SAVED,
                            data.getIntExtra(CourseActivity.EXTRA_VOICE, 0)).apply();
                    setCoursesFragment();
                    setFragments();
                    break;
                case CoursesFragment.REQUEST_DICTION:
                    preferences.edit().putInt(DICTION_SAVED,
                            data.getIntExtra(CourseActivity.EXTRA_DICTION, 0)).apply();
                    setCoursesFragment();
                    setFragments();
                    break;
            }
        }
    }
}
