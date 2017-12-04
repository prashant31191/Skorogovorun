package shavkunov.skorogovorun.lite.controller;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private String extraString;

    private List<Card> courses;
    private String[] arrayCourses = Constants.Url.ARRAY_COURSES;

    private SkorogovorunTask task;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        courses = new ArrayList<>();

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
                        courseMagicBar.setVisibility(View.VISIBLE);
                        Collections.addAll(courses, task.getCards());
                        setRecyclerView();
                    } else {
                        noInternetImage.setVisibility(View.VISIBLE);
                        noInternetTitle.setVisibility(View.VISIBLE);
                        noInternetSubtitle.setVisibility(View.VISIBLE);

                        noInternetImage.setImageResource(R.drawable.no_internet);
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
    protected void onDestroy() {
        super.onDestroy();

        if (task != null) {
            task.cancel(true);
        }
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

    private void setRecyclerView() {
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, true) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        courseRecyclerView.setAdapter(new CourseAdapter());
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

    public class CourseHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_title_course)
        TextView cardTitleCourse;

        @BindView(R.id.card_button_course)
        Button cardButtonCourse;

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
            holder.cardTitleCourse.setText(courses.get(position).getTitle());

            final String courseTitle;
            if (position == (courses.size() - 1)) {
                courseTitle = getString(R.string.got_it);
            } else {
                courseTitle = getString(R.string.go_ahead);
            }
            holder.cardButtonCourse.setText(courseTitle);
            holder.cardButtonCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getAdapterPosition() == courses.size() - 2) {
                        courseMagicBar.setPercent(1.0f);
                    }

                    if (holder.getAdapterPosition() == courses.size() - 1) {
                        finish();
                    } else {
                        courseRecyclerView.scrollToPosition(holder.getAdapterPosition() + 1);
                        courseMagicBar.setSmoothPercent(getIncreasedPercent(courseMagicBar));
                    }
                }
            });
        }

        private float getIncreasedPercent(ISmoothTarget target) {
            float itemCount = (float) 1 / (courses.size() - 1);
            return target.getPercent() + itemCount;
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }
    }
}
