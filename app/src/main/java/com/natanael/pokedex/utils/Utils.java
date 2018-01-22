package com.natanael.pokedex.utils;

/**
 * Created by natanael.afonso on 19/01/2018.
 */

public class Utils {

    public static int getPokemonIdFromUrl(String url) {
        String searchUrl = NetworkUtils.BASE_URL + NetworkUtils.SEARCH_POKEMON_URL_SUFIX;
        String id = url.replace(searchUrl,"").replace("/","");
        return Integer.valueOf(id);
    }
}
