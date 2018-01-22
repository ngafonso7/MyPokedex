package com.natanael.pokedex.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.natanael.pokedex.R;
import com.natanael.pokedex.adapter.PokemonListAdapter;
import com.natanael.pokedex.model.LoadPokemonDetailsCallback;
import com.natanael.pokedex.model.LoadPokemonListCallback;
import com.natanael.pokedex.model.Pokemon;
import com.natanael.pokedex.model.PokemonList;
import com.natanael.pokedex.model.PokemonUrl;
import com.natanael.pokedex.model.PokemonUrlList;
import com.natanael.pokedex.utils.NetworkUtils;
import com.natanael.pokedex.utils.Utils;

public class PokemonListFragment extends Fragment implements LoadPokemonDetailsCallback,
        LoadPokemonListCallback, PokemonListAdapter.ListItemClickListener {

    private PokemonList pokemonListInstance;
    private PokemonListAdapter pokemonListAdapter;

    private ProgressBar pbLoading;

    private Context context;


    private int loadingPage = 1;

    public PokemonListFragment() {
        pokemonListInstance = PokemonList.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        pokemonListAdapter = new PokemonListAdapter(this);

        View rootView = inflater.inflate(R.layout.fragment_pokemon_list, container, false);

        RecyclerView pokemonListView = rootView.findViewById(R.id.pokemonList);
        pokemonListView.setLayoutManager(getLayoutManager());
        pokemonListView.setAdapter(pokemonListAdapter);

        NetworkUtils.getInstance().getPokemonList(loadingPage, this);

        pbLoading = rootView.findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.VISIBLE);

        return rootView;

    }

    @Override
    public void onPokemonDetailsRefresh(Pokemon pokemonDetails) {
        if (pokemonDetails != null) {
            int listIndex = pokemonListInstance.getPokemonListIndex(pokemonDetails.getId());
            pokemonListInstance.setPokemonDetailsAtIndex(pokemonDetails, listIndex);
            pokemonListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyPokemonDetailsRefreshFailure() {
        pbLoading.setVisibility(View.INVISIBLE);
        //showMessage(R.string.refresh_pokemon_details_error,Snackbar.LENGTH_LONG);

    }

    @Override
    public void onListItemClick(Pokemon clickedPokemon) {

    }

    @Override
    public void onPokemonUrlListRefresh(PokemonUrlList pokemonUrlList) {
        int pokemonListIndex = 0;
        for (PokemonUrl pokemonUrl : pokemonUrlList.getDetailsUrls()) {
            int pokemonId = Utils.getPokemonIdFromUrl(pokemonUrl.getUrl());
            Pokemon dummyPokemonEntry = new Pokemon(getString(R.string.dummy_pokemon_name));
            pokemonListInstance.addPokemonIdToList(pokemonId);
            pokemonListInstance.addPokemonDetails(dummyPokemonEntry);
            NetworkUtils.getInstance().getPokemonDetails(pokemonId,this);
            pokemonListIndex++;
            if (pokemonListIndex == 5) {
                break;
            }
        }
        pokemonListAdapter.notifyDataSetChanged();
        pbLoading.setVisibility(View.INVISIBLE);

    }

    @Override
    public void notifyPokemonUrlListRefreshFailure() {
        pbLoading.setVisibility(View.INVISIBLE);
        //showMessage(R.string.refresh_pokemon_details_error,Snackbar.LENGTH_LONG);
    }

    private GridLayoutManager getLayoutManager(){
        GridLayoutManager layoutManager = new GridLayoutManager(context, getNumberOfColums(context));

        return layoutManager;
    }

    private static int getNumberOfColums(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 124;
        return (int) (dpWidth / scalingFactor);
    }

    /*private void showMessage(String message, int duration) {
        if (snackbar != null) {
            snackbar.setText(message);
            snackbar.setDuration(duration);
            snackbar.show();
        }
    }

    private void showMessage(int messageId, int duration) {
        if (snackbar != null) {
            snackbar.setText(messageId);
            snackbar.setDuration(duration);
            snackbar.show();
        }
    }*/


}
