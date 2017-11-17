package shavkunov.skorogovorun.lite.controller.tabs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.TongueTwistersActivity;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ExercisesFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.exercises_recycler_view)
    RecyclerView exercisesRecyclerView;

    public static Fragment newInstance() {
        return new ExercisesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        unbinder = ButterKnife.bind(this, view);
        setExercisesRecyclerView();
        return view;
    }

    private void setExercisesRecyclerView() {
        exercisesRecyclerView.setNestedScrollingEnabled(false);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exercisesRecyclerView.setAdapter(new ExercisesAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class ExercisesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.eCard_image)
        ImageView eCardImage;

        @BindView(R.id.eCard_title)
        TextView eCardTitle;

        @BindView(R.id.eCard_countTitle)
        TextView eCardCount;

        @BindView(R.id.eCard_button)
        FloatingActionButton eCardButton;

        private View itemView;

        public ExercisesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesHolder> {

        @Override
        public ExercisesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.card_exercises, parent, false);
            return new ExercisesHolder(view);
        }

        @Override
        public void onBindViewHolder(ExercisesHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.eCardTitle.setText(R.string.tongueTwisters);
                    setImage(holder, R.drawable.forest);
                    holder.eCardCount.setText("100 скороговорок"); // В будущем будет расчет кол-ва скороговорок
                    holder.eCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setIntent(TongueTwistersActivity.class);
                        }
                    });
                    break;
            }
        }

        private void setIntent(Class<?> cls) {
            startActivity(new Intent(getContext(), cls));
        }

        private void setImage(ExercisesHolder holder, @DrawableRes int imageRes) {
            Glide.with(holder.itemView)
                    .load(imageRes)
                    .apply(bitmapTransform(new ColorFilterTransformation(Color.argb(210, 66, 66, 66))))
                    .into(holder.eCardImage);
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}