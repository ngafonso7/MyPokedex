package com.natanael.pokedex.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.natanael.pokedex.MainFragmentHolder;

/**
 * Created by natanael.afonso on 23/01/2018.
 */

public class PreferenceManager {

    private static PreferenceManager instance;

    private static final String SHARED_PREFERENCE_NAME = "pokedex_preferences";
    private static final String KEY_POKEMON_CAUGHT_LIST = "keyPokemonCaughtList";

    private SharedPreferences preferences;
    private Context context;

    public PreferenceManager() {
        context = MainFragmentHolder.getInstance().getApplicationContext();
        preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance() {
        if (instance == null) {
            instance = new PreferenceManager();
        }
        return instance;
    }

    public String getKeyPokemonCaughtList() {
        String list = preferences.getString(KEY_POKEMON_CAUGHT_LIST, "");
        return list;
    }

    public boolean isPokemonIdStored(int id) {
        String list = preferences.getString(KEY_POKEMON_CAUGHT_LIST, "");
        if ("".equals(list)) {
            return false;
        }
        for (String i : list.split(";")) {
            if (i.equals(String.valueOf(id))) {
                return true;
            }
        }
        return false;
    }

    public void storePokemonCaughtId(int id) {
        String list = preferences.getString(KEY_POKEMON_CAUGHT_LIST, "");
        if ("".equals(list)) {
            list = String.valueOf(id);
        } else {
            for (String i : list.split(";")) {
                if (i.equals(String.valueOf(id))) {
                    return;
                }
            }
            list += ";" + id;
        }
        preferences.edit().putString(KEY_POKEMON_CAUGHT_LIST,list).apply();
    }

    public boolean removePokemonCaughtId(int id) {
        String list = preferences.getString(KEY_POKEMON_CAUGHT_LIST, "");
        if ("".equals(list)) {
            return false;
        }
        String ret = "";
        for (String i : list.split(";")) {
            if (!i.equals(String.valueOf(id))) {
                ret += i + ";";
            }
        }
        if (ret.endsWith(";")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        preferences.edit().putString(KEY_POKEMON_CAUGHT_LIST,ret).apply();
        return true;
    }
}
