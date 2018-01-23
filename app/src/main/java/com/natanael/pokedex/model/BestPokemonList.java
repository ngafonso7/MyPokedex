package com.natanael.pokedex.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by natanael.afonso on 23/01/2018.
 */

public class BestPokemonList {

    private static BestPokemonList instance;

    private static final int MAX_BEST_POKEMON_LIST_SIZE = 6;

    private static final String ATTACK_STAT_NAME = "attack";

    private List<Pokemon> bestPokemonList;
    private int bestPokemonWeightSum;
    private int bestPokemonBaseExpSum;
    private List<String> bestPokemonStatsSum;

    private Comparator<Pokemon> attackStatSorter = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon pokemon, Pokemon t1) {
            int stat = getStatValue(ATTACK_STAT_NAME, pokemon);
            int stat1 = getStatValue(ATTACK_STAT_NAME, t1);
            if (stat > stat1) {
                return 0;
            } else if (stat1 > stat) {
                return -1;
            }
            return 0;
        }
    };

    public BestPokemonList() {
        bestPokemonList = new ArrayList<>();
        bestPokemonStatsSum = new ArrayList<>();
    }

    public static BestPokemonList getInstance() {
        if (instance == null) {
            instance = new BestPokemonList();
        }
        return instance;
    }


    public List<Pokemon> getBestPokemonList() {
        return bestPokemonList;
    }

    public int getBestPokemonWeightSum() {
        return bestPokemonWeightSum;
    }

    public int getBestPokemonBaseExpSum() {
        return bestPokemonBaseExpSum;
    }

    public List<String> getBestPokemonStatsSum() {
        return bestPokemonStatsSum;
    }

    public void calculateBestPokemonList(List<Pokemon> caughtPokemonList) {
        bestPokemonList = caughtPokemonList;
        if (caughtPokemonList != null && caughtPokemonList.size() > MAX_BEST_POKEMON_LIST_SIZE) {
            bestPokemonList.clear();
            caughtPokemonList.sort(attackStatSorter);
            for (int index=0; index < MAX_BEST_POKEMON_LIST_SIZE; index++) {
                bestPokemonList.add(caughtPokemonList.get(index));
            }
        }
        bestPokemonList.sort(attackStatSorter);

        calculateSums(bestPokemonList);
    }

    private void calculateSums(List<Pokemon> bestPokemonList) {
        this.bestPokemonWeightSum = 0;
        this.bestPokemonBaseExpSum = 0;

        for(Pokemon pokemon : bestPokemonList) {
            this.bestPokemonWeightSum += pokemon.getWeight();
            this.bestPokemonBaseExpSum += pokemon.getBaseExperience();
        }

        calculateAllStatsSum(bestPokemonList);


    }
    private void calculateAllStatsSum(List<Pokemon> bestPokemonList) {

        bestPokemonStatsSum.clear();

        HashMap<String, Integer> statsSumList = new HashMap<>();

        for(Pokemon pokemon : bestPokemonList) {
            for(String stat : pokemon.getStats().split("\n")) {
                String[] parts = stat.split(": ");
                String name = parts[0];
                int value = Integer.valueOf(parts[1]);

                if (statsSumList.containsKey(name)) {
                    int oldValue = statsSumList.get(name);
                    statsSumList.replace(name,oldValue + value);
                } else {
                    statsSumList.put(name, value);
                }
            }
        }

        for(String key : statsSumList.keySet()) {
            int value = statsSumList.get(key);
            String name = key.substring(0,1).toUpperCase() + key.substring(1, key.length());
            String stat = name + ": " + String.valueOf(value);
            bestPokemonStatsSum.add(stat);
        }

    }

    private int getStatValue(String statName, Pokemon pokemon) {
        int ret = 0;

        for (String stat : pokemon.getStats().split("\n")) {
            String[] parts = stat.split(":");
            if (statName.equals(parts[0].toLowerCase())) {
                return Integer.valueOf(parts[1].trim());
            }
        }

        return ret;
    }
}
