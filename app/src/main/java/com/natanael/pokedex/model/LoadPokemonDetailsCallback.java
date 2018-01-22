package com.natanael.pokedex.model;

/**
 * Created by natanael.afonso on 19/01/2018.
 */

public interface LoadPokemonDetailsCallback {

    void onPokemonDetailsRefresh(Pokemon pokemonDetails);

    void notifyPokemonDetailsRefreshFailure();
}
