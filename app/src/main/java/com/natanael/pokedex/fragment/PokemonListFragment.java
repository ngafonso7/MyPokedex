package com.natanael.pokedex.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.natanael.pokedex.MainFragmentHolder;
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

    private static PokemonListFragment instance;

    private PokemonList pokemonListInstance;
    private PokemonListAdapter pokemonListAdapter;

    private ProgressBar pbLoading;

    private boolean isLoading = false;

    private Context context;

    private int loadingPage = 1;

    public PokemonListFragment() {
        pokemonListInstance = PokemonList.getInstance();
    }

    public static PokemonListFragment getInstance() {
        if (instance == null) {
            instance = new PokemonListFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        pokemonListAdapter = new PokemonListAdapter(this);

        View rootView = inflater.inflate(R.layout.fragment_pokemon_list, container, false);

        ConstraintLayout rootLayout = rootView.findViewById(R.id.pokemon_list_root_layout);

        RecyclerView pokemonListView = rootView.findViewById(R.id.pokemonList);
        pokemonListView.setLayoutManager(getLayoutManager());
        pokemonListView.setAdapter(pokemonListAdapter);

        pbLoading = rootView.findViewById(R.id.pb_loading);

        if (pokemonListInstance.getPokemonDetailsList().size() == 0) {
            if (NetworkUtils.isInternetConnected(context)) {
                updatePokemonList();
            } else {
                showMessage(R.string.no_internet_connection_error_message, Snackbar.LENGTH_INDEFINITE);
            }
        }
        return rootView;

    }

    public void updatePokemonList() {
        if (!isLoading) {
            isLoading = true;
            pbLoading.setVisibility(View.VISIBLE);
            NetworkUtils.getInstance().getPokemonList(loadingPage, this);
        }
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
        isLoading = false;
        pbLoading.setVisibility(View.INVISIBLE);
        showMessage(R.string.refresh_pokemon_details_error,Snackbar.LENGTH_LONG);

    }

    @Override
    public void onListItemClick(Pokemon clickedPokemon) {
        if(clickedPokemon.isLoaded() && !isLoading) {
            //Open details screen
            pokemonListInstance.setSelectedPokemon(
                    pokemonListInstance.getPokemonListIndex(clickedPokemon.getId()));
            MainFragmentHolder.showScreen(MainFragmentHolder.DETAILS_SCREEN);
        }
    }

    @Override
    public void onPokemonUrlListRefresh(PokemonUrlList pokemonUrlList) {
        isLoading = false;
        int pokemonListIndex = 0;
        for (PokemonUrl pokemonUrl : pokemonUrlList.getDetailsUrls()) {
            int pokemonId = Utils.getPokemonIdFromUrl(pokemonUrl.getUrl());
            if (pokemonListInstance.getPokemonListIndex(pokemonId) == -1) {
                Pokemon dummyPokemonEntry = new Pokemon(getString(R.string.dummy_pokemon_name));
                pokemonListInstance.addPokemonIdToList(pokemonId);
                pokemonListInstance.addPokemonDetails(dummyPokemonEntry);
            }
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
        isLoading = false;
        pbLoading.setVisibility(View.INVISIBLE);
        showMessage(R.string.refresh_pokemon_details_error,Snackbar.LENGTH_LONG);
    }

    private GridLayoutManager getLayoutManager(){
        return new GridLayoutManager(context, getNumberOfColums(context));
    }

    private static int getNumberOfColums(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 124;
        return (int) (dpWidth / scalingFactor);
    }

    private void showMessage(String message, int duration) {
        MainFragmentHolder.getInstance().showMessage(message, duration);
    }

    private void showMessage(int messageId, int duration) {
        MainFragmentHolder.getInstance().showMessage(messageId, duration);
    }


}
