package com.natanael.pokedex.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by natanael.afonso on 19/01/2018.
 */

public class PokemonList {

    private static PokemonList instance;
    private List<Pokemon> pokemonDetailsList;
    private int pokemonCount;

    private HashMap<Integer, Integer> mapPokemonIdtoListIndex = new HashMap<>();

    public PokemonList() {
        pokemonDetailsList = new ArrayList<>();
        pokemonCount = 0;
    }

    public static PokemonList getInstance() {
        if (instance == null) {
            instance = new PokemonList();
        }
        return instance;
    }

    public int addPokemonIdToList(int id) {
        //pokemonDetailsList.add(++pokemonCount, details);
        mapPokemonIdtoListIndex.put(id, pokemonCount++);
        return pokemonCount;
    }

    public void addPokemonDetails(Pokemon details) {
        pokemonDetailsList.add(details);
    }

    public void setPokemonDetailsAtIndex(Pokemon details, int index ) {
        pokemonDetailsList.set(index, details);
    }

    public int getPokemonListIndex(int pokemonId) {
        return mapPokemonIdtoListIndex.get(pokemonId);
    }

    public List<Pokemon> getPokemonDetailsList() {
        return getInstance().pokemonDetailsList;
    }

    public int getPokemonCount() {
        return pokemonCount;

    }

}
