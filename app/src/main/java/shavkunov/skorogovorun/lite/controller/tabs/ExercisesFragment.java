package shavkunov.skorogovorun.lite.controller.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.R;

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

    private class ExercisesHolder extends RecyclerView.ViewHolder {

        public ExercisesHolder(View itemView) {
            super(itemView);
        }
    }

    private class ExercisesAdapter extends RecyclerView.Adapter<ExercisesHolder> {

        @Override
        public ExercisesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.card_exercises, parent, false);
            return new ExercisesHolder(view);
        }

        @Override
        public void onBindViewHolder(ExercisesHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
