package shavkunov.skorogovorun.lite.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.magicprogresswidget.MagicProgressBar;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dreamtobe.percentsmoothhandler.ISmoothTarget;
import shavkunov.skorogovorun.lite.Constants;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.SkorogovorunTask;
import shavkunov.skorogovorun.lite.controller.tabs.CoursesFragment;
import shavkunov.skorogovorun.lite.model.Card;

public class CourseActivity extends AppCompatActivity {

    public static final String EXTRA_POSTURE = "extraPosture";
    public static final String EXTRA_BREATH = "extraBreath";
    public static final String EXTRA_VOICE = "extraVoice";
    public static final String EXTRA_DICTION = "extraDiction";

    private static final String SAVED_POSTURE = "savedPosture";
    private static final String SAVED_BREATH = "savedBreath";
    private static final String SAVED_VOICE = "savedVoice";
    private static final String SAVED_DICTION = "savedDiction";

    private static final String SAVED_POSTURE_PERCENT = "savedPosturePercent";
    private static final String SAVED_BREATH_PERCENT = "savedBreathPercent";
    private static final String SAVED_VOICE_PERCENT = "savedVoicePercent";
    private static final String SAVED_DICTION_PERCENT = "savedDictionPercent";

    private String extraString;
    private boolean isInternet;

    private List<Card> courses;
    private String[] arrayCourses = Constants.Url.ARRAY_COURSES;

    private SkorogovorunTask task;
    private SharedPreferences preferences;

    @BindView(R.id.course_recycler_view)
    RecyclerView courseRecyclerView;

    @BindView(R.id.progress)
    DilatingDotsProgressBar progressBar;

    @BindView(R.id.image_empty)
    ImageView noInternetImage;

    @BindView(R.id.title_empty)
    TextView noInternetTitle;

    @BindView(R.id.subtitle_empty)
    TextView noInternetSubtitle;

    @BindView(R.id.button_empty)
    FloatingActionButton noInternetButton;

    @BindView(R.id.course_progress_bar)
    MagicProgressBar courseMagicBar;

    @BindView(R.id.course_count)
    TextView courseCount;

    @BindView(R.id.toolbar_courses)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        courses = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        ActionBar actionBar = getSupportActionBar();
        extraString = getIntent().getStringExtra(CoursesFragment.EXTRA_COURSE);
        if (actionBar != null) {
            actionBar.setTitle(getIntent()
                    .getStringExtra(CoursesFragment.EXTRA_COURSE));
        }

        setTask(extraString);
        getCoursesFromInternet();
    }

    private void getCoursesFromInternet() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.showNow();

                if (task.getStatus().toString().equals("FINISHED")) {
                    progressBar.hideNow();

                    if (task.getCards() != null) {
                        courseCount.setVisibility(View.VISIBLE);
                        courseMagicBar.setVisibility(View.VISIBLE);
                        Collections.addAll(courses, task.getCards());
                        setViews();
                        toolbar.setNavigationIcon(R.drawable.chevron_left);
                        setSupportActionBar(toolbar);
                        isInternet = true;
                    } else {
                        noInternetImage.setVisibility(View.VISIBLE);
                        noInternetTitle.setVisibility(View.VISIBLE);
                        noInternetSubtitle.setVisibility(View.VISIBLE);

                        noInternetImage.setImageResource(R.drawable.no_connection);
                        noInternetTitle.setText(R.string.no_connection);
                        noInternetSubtitle.setText(R.string.check_your_connection);
                        noInternetButton.setVisibility(View.VISIBLE);
                        noInternetButton.setImageResource(R.drawable.refresh);
                    }
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isInternet) {
            passPercentForResult();
        }

        super.onBackPressed();
    }

    private void passPercentForResult() {
        LinearLayoutManager manager = (LinearLayoutManager)
                courseRecyclerView.getLayoutManager();
        Intent intent = new Intent();
        String extraPercent = null;

        if (extraString.equals(getString(R.string.posture))) {
            extraPercent = EXTRA_POSTURE;
        } else if (extraString.equals(getString(R.string.breath))) {
            extraPercent = EXTRA_BREATH;
        } else if (extraString.equals(getString(R.string.voice))) {
            extraPercent = EXTRA_VOICE;
        } else if (extraString.equals(getString(R.string.diction))) {
            extraPercent = EXTRA_DICTION;
        }

        int resultPercent = 0;
        if (manager != null) {
            if (courses.size() - 1 == manager.findFirstVisibleItemPosition()) {
                resultPercent = 100;
            } else {
                resultPercent = (100 / (courses.size() - 1)) *
                        manager.findFirstVisibleItemPosition();
            }
        }

        intent.putExtra(extraPercent, resultPercent);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isInternet) {
            saveValuablesAndPercent();
        }

        if (task != null) {
            task.cancel(true);
        }
    }

    private void saveValuablesAndPercent() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                courseRecyclerView.getLayoutManager();

        String savedValuable = null;
        String savedPercent = null;

        if (extraString.equals(getString(R.string.posture))) {
            savedValuable = SAVED_POSTURE;
            savedPercent = SAVED_POSTURE_PERCENT;
        } else if (extraString.equals(getString(R.string.breath))) {
            savedValuable = SAVED_BREATH;
            savedPercent = SAVED_BREATH_PERCENT;
        } else if (extraString.equals(getString(R.string.voice))) {
            savedValuable = SAVED_VOICE;
            savedPercent = SAVED_VOICE_PERCENT;
        } else if (extraString.equals(getString(R.string.diction))) {
            savedValuable = SAVED_DICTION;
            savedPercent = SAVED_DICTION_PERCENT;
        }

        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        float lastPercent = courseMagicBar.getPercent();

        preferences.edit().putInt(savedValuable, lastPosition).apply();
        preferences.edit().putFloat(savedPercent, lastPercent).apply();
    }

    private void setTask(String extraString) {
        task = new SkorogovorunTask();
        String url = null;
        if (extraString.equals(getString(R.string.posture))) {
            url = arrayCourses[0];
        } else if (extraString.equals(getString(R.string.breath))) {
            url = arrayCourses[1];
        } else if (extraString.equals(getString(R.string.voice))) {
            url = arrayCourses[2];
        } else if (extraString.equals(getString(R.string.diction))) {
            url = arrayCourses[3];
        }
        task.setUrl(url);
        task.execute();
    }

    private void setViews() {
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, true) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        courseRecyclerView.setAdapter(new CourseAdapter());

        int savedPosition = 0;
        float savedPercent = 0;

        if (extraString.equals(getString(R.string.posture))) {
            savedPosition = preferences.getInt(SAVED_POSTURE, 0);
            savedPercent = preferences.getFloat(SAVED_POSTURE_PERCENT, 0);
        } else if (extraString.equals(getString(R.string.breath))) {
            savedPosition = preferences.getInt(SAVED_BREATH, 0);
            savedPercent = preferences.getFloat(SAVED_BREATH_PERCENT, 0);
        } else if (extraString.equals(getString(R.string.voice))) {
            savedPosition = preferences.getInt(SAVED_VOICE, 0);
            savedPercent = preferences.getFloat(SAVED_VOICE_PERCENT, 0);
        } else if (extraString.equals(getString(R.string.diction))) {
            savedPosition = preferences.getInt(SAVED_DICTION, 0);
            savedPercent = preferences.getFloat(SAVED_DICTION_PERCENT, 0);
        }

        setCourseCount(savedPosition);
        courseRecyclerView.scrollToPosition(savedPosition);
        courseMagicBar.setPercent(savedPercent);

        if (savedPosition == courses.size() - 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAlertDialog();
                }
            }, 1);
        }
    }

    @OnClick(R.id.button_empty)
    public void onNoInternetButton() {
        if (CardLab.newInstance().isConnectedToNetwork(this)) {
            noInternetButton.setVisibility(View.GONE);
        }

        noInternetImage.setVisibility(View.GONE);
        noInternetTitle.setVisibility(View.GONE);
        noInternetSubtitle.setVisibility(View.GONE);

        setTask(extraString);
        getCoursesFromInternet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_restart:
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertDialog() {
        LinearLayoutManager manager = (LinearLayoutManager)
                courseRecyclerView.getLayoutManager();

        int position = -1;
        if (manager != null) {
            position = manager.findFirstVisibleItemPosition();
        }

        if (position == -1) {
            Toast.makeText(this, getString(R.string.check_your_connection),
                    Toast.LENGTH_SHORT).show();
        } else if (position == 0) {
            Toast.makeText(this, getString(R.string.start_course),
                    Toast.LENGTH_SHORT).show();
        } else {
            String textDialog;

            if (position == courses.size() - 1) {
                textDialog = getString(R.string.end_course);
            } else {
                textDialog = getString(R.string.middle_course);
            }

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.go_to_start))
                    .setMessage(textDialog)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            courseRecyclerView.scrollToPosition(0);
                            courseMagicBar.setPercent(0);
                        }
                    })
                    .show();
        }
    }

    private void setCourseCount(int currentPosition) {
        String result = String.valueOf(new StringBuilder()
                .append(currentPosition + 1)
                .append("/").append(courses.size()));
        courseCount.setText(result);
    }

    public class CourseHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_title_course)
        TextView cardTitleCourse;

        @BindView(R.id.card_button_prev)
        Button cardButtonPrev;

        @BindView(R.id.card_button_next)
        Button cardButtonNext;

        public CourseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(CourseActivity.this);
            View view = inflater.inflate(R.layout.card_course, parent, false);
            return new CourseHolder(view);
        }

        @Override
        public void onBindViewHolder(final CourseHolder holder, int position) {
            setCourseCount(position);
            holder.cardTitleCourse.setText(courses.get(position).getTitle());
            holder.cardButtonPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getAdapterPosition() == 0) {
                        passPercentForResult();
                        finish();
                    } else {
                        courseRecyclerView.scrollToPosition(holder.getAdapterPosition() - 1);
                        courseMagicBar.setSmoothPercent(getReducedPercent(courseMagicBar));
                        notifyDataSetChanged();
                    }
                }
            });

            String courseTitle;
            if (position == (courses.size() - 1)) {
                courseTitle = getString(R.string.got_it);
            } else {
                courseTitle = getString(R.string.go_ahead);
            }
            holder.cardButtonNext.setText(courseTitle);
            holder.cardButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getAdapterPosition() == courses.size() - 2) {
                        courseMagicBar.setPercent(1.0f);
                    }

                    if (holder.getAdapterPosition() == courses.size() - 1) {
                        passPercentForResult();
                        finish();
                    } else {
                        courseRecyclerView.scrollToPosition(holder.getAdapterPosition() + 1);

                        /* setSmoothPercent() принимает значение типа float,
                        где максимальное значение - это 1.0f (100 % в Progress Bar)
                         */
                        courseMagicBar.setSmoothPercent(getIncreasedPercent(courseMagicBar));
                        notifyDataSetChanged();
                    }
                }
            });
        }

        /**
         * Высчитываем интервалы для того кол-ва элементов, что есть в списке, чтобы получилось
         * возращаемое значение равное куску от 1.0f (100% в Progress Bar)
         *
         * @param target - значение типа MagicProgressBar
         * @return значение типа данных float, кусок от значения 1.0f для только одного элемента
         * из списка
         */
        private float getIncreasedPercent(ISmoothTarget target) {
            float itemCount = (float) 1 / (courses.size() - 1);
            return target.getPercent() + itemCount;
        }

        private float getReducedPercent(ISmoothTarget target) {
            float itemCount = (float) 1 / (courses.size() - 1);
            return target.getPercent() - itemCount;
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }
    }
}
