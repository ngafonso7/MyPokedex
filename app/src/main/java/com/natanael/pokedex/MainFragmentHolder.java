package com.natanael.pokedex;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.natanael.pokedex.fragment.PokemonDetailsFragment;
import com.natanael.pokedex.fragment.PokemonListFragment;
import com.natanael.pokedex.fragment.PokemonMoreStatsFragment;
import com.natanael.pokedex.receiver.InternetReceiver;

public class MainFragmentHolder extends AppCompatActivity {

    private static final int ENTER_LEFT = 1;
    private static final int ENTER_RIGHT = 2;
    private static final int ENTER_BOTTOM = 3;

    public static int MAIN_LIST_SCREEN = 0;
    public static int DETAILS_SCREEN = 1;
    public static int MORE_STATS_SCREEN = 2;

    private static int actualScreen = -1;

    private static MainFragmentHolder instance;
    private static FragmentManager fragmentManager;

    private static PokemonListFragment pokemonListFragment;

    private Snackbar snackbar;

    private InternetReceiver internetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_holder);

        instance = this;
        fragmentManager = getSupportFragmentManager();

        pokemonListFragment = new PokemonListFragment();

        FrameLayout layout = findViewById(R.id.fragment_container);

        snackbar = Snackbar.make(layout, "", Snackbar.LENGTH_SHORT);

        showScreen(MAIN_LIST_SCREEN);

        Handler internetHandler = new InternetHandler();

        internetReceiver = new InternetReceiver(internetHandler);
        this.registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(internetReceiver);
        super.onDestroy();
    }

    public static MainFragmentHolder getInstance() {
        return instance;
    }

    public void showMessage(String message, int duration) {
        if (snackbar != null) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
            snackbar.setText(message);
            snackbar.setDuration(duration);
            snackbar.show();
        }
    }

    public void showMessage(int messageId, int duration) {
        if (snackbar != null) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
            snackbar.setText(messageId);
            snackbar.setDuration(duration);
            snackbar.show();
        }
    }

    public static void showScreen(int fragmentId) {
        if (fragmentId != actualScreen) {
            if (fragmentId == MAIN_LIST_SCREEN) {
                //instance.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                changeFragment(pokemonListFragment, ENTER_LEFT);
            } else if (fragmentId == DETAILS_SCREEN){
                changeFragment(new PokemonDetailsFragment(), ENTER_RIGHT);
            } else if (fragmentId == MORE_STATS_SCREEN) {
                changeFragment(new PokemonMoreStatsFragment(), ENTER_RIGHT);
            }
            actualScreen = fragmentId;
        }
    }

    private static void changeFragment(Fragment fragment, int type) {
        /*fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
                */
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (type == ENTER_LEFT) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (type == ENTER_RIGHT){
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        } else if (type == ENTER_BOTTOM){
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top);
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commitNow();

    }

    @Override
    public void onBackPressed()
    {
        if (actualScreen == DETAILS_SCREEN) {
            showScreen(MAIN_LIST_SCREEN);
        } else if (actualScreen == MAIN_LIST_SCREEN) {
            actualScreen = -1;
            super.onBackPressed();
        } else if (actualScreen == MORE_STATS_SCREEN) {
            showScreen(MAIN_LIST_SCREEN);
        }

    }

    static class InternetHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            if(msg.what == InternetReceiver.INTERNET_CONNECTED) {
                if (getInstance().snackbar != null) {
                    getInstance().snackbar.dismiss();
                }
                pokemonListFragment.updatePokemonList();
            } else if(msg.what == InternetReceiver.INTERNET_DISCONNECTED) {
                getInstance().showMessage(R.string.no_internet_connection_error_message, Snackbar.LENGTH_INDEFINITE);
                pokemonListFragment.errorLoadingInformation();
            }
        }
    }

}
