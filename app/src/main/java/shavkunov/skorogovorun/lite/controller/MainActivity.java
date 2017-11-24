package shavkunov.skorogovorun.lite.controller;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.tabs.CoursesFragment;
import shavkunov.skorogovorun.lite.controller.tabs.ExercisesFragment;
import shavkunov.skorogovorun.lite.controller.tabs.SettingsFragment;

public class MainActivity extends AppCompatActivity {

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
                        fragment = ExercisesFragment.newInstance();
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
                getSupportActionBar().setTitle(toolbarTitle);
            }
        });
    }

    private Fragment createFragment() {
        return fragment;
    }

    private void setFragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, createFragment());
        ft.commit();
    }
}
