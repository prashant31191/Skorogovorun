package shavkunov.skorogovorun.lite.controller.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.sackcentury.shinebuttonlib.ShineButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.CardLab;
import shavkunov.skorogovorun.lite.controller.CourseActivity;

public class CoursesFragment extends Fragment {

    public static final int REQUEST_POSTURE = 0;
    public static final int REQUEST_BREATH = 1;
    public static final int REQUEST_VOICE = 2;
    public static final int REQUEST_DICTION = 3;
    public static final String EXTRA_COURSE = "extraCourse";

    private Unbinder unbinder;

    @BindView(R.id.courses_recycler_view)
    RecyclerView coursesRecyclerView;

    public static Fragment newInstance() {
        return new CoursesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        unbinder = ButterKnife.bind(this, view);
        setCoursesRecycler();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setCoursesRecycler() {
        coursesRecyclerView.setNestedScrollingEnabled(false);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coursesRecyclerView.setAdapter(new CoursesAdapter());
    }

    public class CoursesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_image_default)
        ImageView cCard_image;

        @BindView(R.id.card_title_default)
        TextView cCard_title;

        @BindView(R.id.card_button_default)
        ShineButton cCardButton;

        @BindView(R.id.card_progress_default)
        NumberProgressBar cCardProgress;

        public CoursesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cCardButton.init(getActivity());
            cCardProgress.setVisibility(View.VISIBLE);
        }
    }

    public class CoursesAdapter extends RecyclerView.Adapter<CoursesHolder> {

        @Override
        public CoursesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.card_default, parent, false);
            return new CoursesHolder(view);
        }

        @Override
        public void onBindViewHolder(final CoursesHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.cCard_title.setText(getString(R.string.posture));
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.man_posture,
                            holder.cCard_image);

                    holder.cCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.cCardButton.showAnim();
                            CardLab.newInstance().setIntent(getActivity(), CourseActivity.class,
                                    EXTRA_COURSE, getString(R.string.posture), REQUEST_POSTURE);
                        }
                    });
                    break;
                case 1:
                    holder.cCard_title.setText(getString(R.string.breath));
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.breath,
                            holder.cCard_image);
                    holder.cCardProgress.setProgress(35);
                    holder.cCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.cCardButton.showAnim();
                            CardLab.newInstance().setIntent(getActivity(), CourseActivity.class,
                                    EXTRA_COURSE, getString(R.string.breath), REQUEST_BREATH);
                        }
                    });
                    break;
                case 2:
                    holder.cCard_title.setText(getString(R.string.voice));
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.voice,
                            holder.cCard_image);
                    holder.cCardProgress.setProgress(70);
                    holder.cCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.cCardButton.showAnim();
                            CardLab.newInstance().setIntent(getActivity(), CourseActivity.class,
                                    EXTRA_COURSE, getString(R.string.voice), REQUEST_VOICE);
                        }
                    });
                    break;
                case 3:
                    holder.cCard_title.setText(getString(R.string.diction));
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.diction,
                            holder.cCard_image);
                    holder.cCardProgress.setProgress(100);
                    holder.cCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.cCardButton.showAnim();
                            CardLab.newInstance().setIntent(getActivity(), CourseActivity.class,
                                    EXTRA_COURSE, getString(R.string.diction), REQUEST_DICTION);
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
