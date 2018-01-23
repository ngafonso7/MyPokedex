package com.natanael.pokedex.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.natanael.pokedex.utils.EndlessRecyclerViewScrollListener;
import com.natanael.pokedex.utils.NetworkUtils;
import com.natanael.pokedex.utils.Utils;

public class PokemonListFragment extends Fragment implements LoadPokemonDetailsCallback,
        LoadPokemonListCallback, PokemonListAdapter.ListItemClickListener {

    private static PokemonListFragment instance;

    private PokemonList pokemonListInstance;
    private PokemonListAdapter pokemonListAdapter;

    private ProgressBar pbLoading;
    private RecyclerView pokemonListView;
    private SwipeRefreshLayout pokemonListSwipeRefresh;
    private ImageView moreStatsImageView;

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

        pokemonListView = rootView.findViewById(R.id.pokemonList);
        GridLayoutManager gridLayoutManager = getLayoutManager();
        pokemonListView.setLayoutManager(gridLayoutManager);
        pokemonListView.setAdapter(pokemonListAdapter);
        pokemonListView.setOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadingPage = page;
                updatePokemonList();
            }
        });

        pbLoading = rootView.findViewById(R.id.pb_loading);

        pokemonListSwipeRefresh = rootView.findViewById(R.id.sr_pokemon_list_layout);
        pokemonListSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updatePokemonList();
            }
        });

        moreStatsImageView = rootView.findViewById(R.id.im_more_stats);
        moreStatsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragmentHolder.showScreen(MainFragmentHolder.MORE_STATS_SCREEN);
            }
        });

        if (pokemonListInstance.getPokemonDetailsList().size() == 0) {
            if (NetworkUtils.isInternetConnected(context)) {
                updatePokemonList();
            } else {
                showMessage(R.string.no_internet_connection_error_message, Snackbar.LENGTH_INDEFINITE);
            }
        } else {
            pokemonListView.setBackgroundColor(Color.WHITE);
        }
        return rootView;

    }

    public void updatePokemonList() {
        if (!isLoading) {
            isLoading = true;
            pokemonListView.setBackgroundColor(Color.WHITE);
            pbLoading.setVisibility(View.VISIBLE);
            pokemonListSwipeRefresh.setRefreshing(false);
            NetworkUtils.getInstance().getPokemonList(loadingPage, this);
        }
    }

    public void errorLoadingInformation() {
        if (pokemonListInstance.getPokemonDetailsList().size() == 0) {
            pokemonListView.setBackground(context.getDrawable(R.drawable.no_info_bg));
        }
        pbLoading.setVisibility(View.INVISIBLE);

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
        errorLoadingInformation();
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
        for (PokemonUrl pokemonUrl : pokemonUrlList.getDetailsUrls()) {
            int pokemonId = Utils.getPokemonIdFromUrl(pokemonUrl.getUrl());
            if (pokemonListInstance.getPokemonListIndex(pokemonId) == -1) {
                Pokemon dummyPokemonEntry = new Pokemon(getString(R.string.dummy_pokemon_name));
                pokemonListInstance.addPokemonIdToList(pokemonId);
                pokemonListInstance.addPokemonDetails(dummyPokemonEntry);
            }
            NetworkUtils.getInstance().getPokemonDetails(pokemonId,this);
        }
        pokemonListAdapter.notifyDataSetChanged();
        pbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void notifyPokemonUrlListRefreshFailure() {
        isLoading = false;
        errorLoadingInformation();
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
