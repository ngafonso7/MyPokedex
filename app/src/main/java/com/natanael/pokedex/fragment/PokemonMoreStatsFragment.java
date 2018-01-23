package com.natanael.pokedex.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.natanael.pokedex.R;
import com.natanael.pokedex.adapter.BestPokemonListAdapter;
import com.natanael.pokedex.model.BestPokemonList;

/**
 * Created by natanael.afonso on 23/01/2018.
 */

public class PokemonMoreStatsFragment extends Fragment {

    private Context context;
    private BestPokemonListAdapter bestPokemonListAdapter;
    private RecyclerView bestPokemonListView;

    private BestPokemonList bestPokemonList;

    private TextView statsSumTextView;
    private TextView weightSumTextView;
    private TextView baseExpSumTextView;

    private static final int NUMBER_OF_COLUMNS = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        bestPokemonListAdapter = new BestPokemonListAdapter();

        bestPokemonList = BestPokemonList.getInstance();

        View rootView = inflater.inflate(R.layout.fragment_pokemon_more_stats, container, false);

        statsSumTextView = rootView.findViewById(R.id.tv_stats_sum_list);
        weightSumTextView = rootView.findViewById(R.id.tv_weight_sum);
        baseExpSumTextView = rootView.findViewById(R.id.tv_baseexp_sum);

        GridLayoutManager gridLayoutManager = getLayoutManager();

        bestPokemonListView = rootView.findViewById(R.id.rv_best_pokemon_list);
        bestPokemonListView.setLayoutManager(gridLayoutManager);
        bestPokemonListView.setAdapter(bestPokemonListAdapter);

        if (bestPokemonList.getBestPokemonList().size() == 0) {
            bestPokemonListView.setBackground(context.getDrawable(R.drawable.no_info_bg));
            statsSumTextView.setBackground(context.getDrawable(R.drawable.no_info_bg));
            statsSumTextView.setText("");
            weightSumTextView.setText("0");
            baseExpSumTextView.setText("0");
        } else {
            bestPokemonListView.setBackgroundColor(Color.WHITE);
            statsSumTextView.setBackgroundColor(Color.WHITE);
            statsSumTextView.setText(bestPokemonList.getBestPokemonStatsSum());
            weightSumTextView.setText(String.valueOf(bestPokemonList.getBestPokemonWeightSum()));
            baseExpSumTextView.setText(String.valueOf(bestPokemonList.getBestPokemonBaseExpSum()));
        }

        return rootView;

    }

    private GridLayoutManager getLayoutManager(){
        return new GridLayoutManager(context, NUMBER_OF_COLUMNS);
    }
}
