package com.natanael.pokedex.model;

/**
 * Created by natanael.afonso on 19/01/2018.
 */

public interface LoadPokemonListCallback {

    void onPokemonUrlListRefresh(PokemonUrlList pokemonUrlList);

    void notifyPokemonUrlListRefreshFailure();

}
