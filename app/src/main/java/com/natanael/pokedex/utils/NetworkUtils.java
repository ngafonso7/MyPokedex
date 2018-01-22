package com.natanael.pokedex.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.natanael.pokedex.model.LoadPokemonDetailsCallback;
import com.natanael.pokedex.model.LoadPokemonListCallback;
import com.natanael.pokedex.model.Pokemon;
import com.natanael.pokedex.model.PokemonUrlList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by natanael.afonso on 19/01/2018.
 */

public class NetworkUtils {

    public static final String BASE_URL = "https://pokeapi.co/api/v2/";
    public static final String SEARCH_POKEMON_URL_SUFIX = "pokemon";

    private static final int DEFAULT_LIMIT_PAGE = 20;

    private static NetworkUtils instance;

    private final PokemonRequestApi pokemonRequestApi;

    public NetworkUtils() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokemonRequestApi = retrofit.create(PokemonRequestApi.class);
    }

    public static NetworkUtils getInstance(){
        if (instance == null) {
            instance = new NetworkUtils();
        }
        return instance;
    }

    public void getPokemonList(int loadingPage, final LoadPokemonListCallback callback) {
        pokemonRequestApi.getPokemonList(DEFAULT_LIMIT_PAGE, (loadingPage * 20) - 20).enqueue(new Callback<PokemonUrlList>() {
            @Override
            public void onResponse(Call<PokemonUrlList> call, Response<PokemonUrlList> response) {
                if(response.isSuccessful()) {
                    callback.onPokemonUrlListRefresh(response.body());
                }
            }

            @Override
            public void onFailure(Call<PokemonUrlList> call, Throwable t) {
                callback.notifyPokemonUrlListRefreshFailure();
            }
        });
    }

    public void getPokemonDetails(int pokemonId, final LoadPokemonDetailsCallback callback) {
        pokemonRequestApi.getPokemonDetails(pokemonId).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful()) {
                    callback.onPokemonDetailsRefresh(response.body());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                callback.notifyPokemonDetailsRefreshFailure();
            }
        });
    }

    public static boolean isInternetConnected(Context context) {
        boolean isOnline = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null) {
                if(netInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                        netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isOnline = true;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return isOnline;
    }

    public interface PokemonRequestApi {
        @GET(SEARCH_POKEMON_URL_SUFIX)
        Call<PokemonUrlList> getPokemonList(
                @Query("limit") int limit,
                @Query("offset") int offset);

        @GET(SEARCH_POKEMON_URL_SUFIX + "/{id}")
        Call<Pokemon> getPokemonDetails(
                @Path("id") int id
        );
    }
}
