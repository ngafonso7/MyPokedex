package com.natanael.pokedex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.natanael.pokedex.fragment.PokemonListFragment;

public class MainFragmentHolder extends AppCompatActivity {

    public static int MAIN_LIST_SCREEN = 0;
    public static int DETAILS_SCREEN = 1;

    private static int actualScreen = -1;

    private static MainFragmentHolder instance;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_holder);

        instance = this;
        fragmentManager = getSupportFragmentManager();

        showScreen(MAIN_LIST_SCREEN);
    }

    public MainFragmentHolder getInstance() {
        return instance;
    }

    public static void showScreen(int fragmentId) {
        actualScreen = fragmentId;
        if (fragmentId == MAIN_LIST_SCREEN) {
            //instance.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            changeFragment(new PokemonListFragment());
        }
    }

    private static void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
