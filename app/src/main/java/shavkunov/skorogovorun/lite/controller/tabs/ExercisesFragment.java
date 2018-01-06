package shavkunov.skorogovorun.lite.controller.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.CardLab;
import shavkunov.skorogovorun.lite.controller.FavoriteTongueActivity;
import shavkunov.skorogovorun.lite.controller.TongueTwistersActivity;

public class ExercisesFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.exercises_recycler_view)
    RecyclerView exercisesRecyclerView;

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
        exercisesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        exercisesRecyclerView.setAdapter(new ExercisesAdapter());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class ExercisesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_image_default)
        ImageView eCardImage;

        @BindView(R.id.card_title_default)
        TextView eCardTitle;

        @BindView(R.id.card_button_default)
        ShineButton eCardButton;

        @BindView(R.id.card_favorite_default)
        ShineButton eCardFavoriteButton;

        public ExercisesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            eCardButton.init(getActivity());
            eCardFavoriteButton.init(getActivity());
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
        public void onBindViewHolder(final ExercisesHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.eCardTitle.setText(R.string.tongue_twisters);
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.patters,
                            holder.eCardImage);

                    holder.eCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.eCardButton.showAnim();
                            Intent intent = new Intent(getActivity(),
                                    TongueTwistersActivity.class);
                            startActivity(intent);
                        }
                    });
                    holder.eCardFavoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.eCardFavoriteButton.showAnim();
                            Intent intent = new Intent(getActivity(),
                                    FavoriteTongueActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    holder.eCardTitle.setText(R.string.poems);
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.poet,
                            holder.eCardImage);
                    break;
                case 2:
                    holder.eCardTitle.setText(R.string.breathing_exercises);
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.breathing,
                            holder.eCardImage);
                    break;
                case 3:
                    holder.eCardTitle.setText(R.string.posture);
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.posture_training,
                            holder.eCardImage);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
