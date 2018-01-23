package com.natanael.pokedex.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private static final int NUMBER_OF_COLUMNS = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pokemon_more_stats, container, false);

        context = getActivity().getApplicationContext();
        bestPokemonListAdapter = new BestPokemonListAdapter();

        bestPokemonListView = rootView.findViewById(R.id.rv_best_pokemon_list);
        GridLayoutManager gridLayoutManager = getLayoutManager();
        bestPokemonListView.setLayoutManager(gridLayoutManager);
        bestPokemonListView.setAdapter(bestPokemonListAdapter);

        if (BestPokemonList.getInstance().getBestPokemonList().size() == 0) {
            bestPokemonListView.setBackground(context.getDrawable(R.drawable.no_info_bg));
        }

        return rootView;

    }

    private GridLayoutManager getLayoutManager(){
        return new GridLayoutManager(context, NUMBER_OF_COLUMNS);
    }
}
