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
import com.natanael.pokedex.receiver.InternetReceiver;

public class MainFragmentHolder extends AppCompatActivity {

    private static final int ENTER_LEFT = 1;
    private static final int ENTER_RIGHT = 2;

    public static int MAIN_LIST_SCREEN = 0;
    public static int DETAILS_SCREEN = 1;

    private static int actualScreen = -1;

    private static MainFragmentHolder instance;
    private static FragmentManager fragmentManager;

    private Snackbar snackbar;

    private InternetReceiver internetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_holder);

        instance = this;
        fragmentManager = getSupportFragmentManager();

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
            snackbar.setText(message);
            snackbar.setDuration(duration);
            snackbar.show();
        }
    }

    public void showMessage(int messageId, int duration) {
        if (snackbar != null) {
            snackbar.setText(messageId);
            snackbar.setDuration(duration);
            snackbar.show();
        }
    }

    public static void showScreen(int fragmentId) {
        if (fragmentId != actualScreen) {
            if (fragmentId == MAIN_LIST_SCREEN) {
                //instance.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                changeFragment(new PokemonListFragment(), ENTER_LEFT);
            } else {
                changeFragment(new PokemonDetailsFragment(), ENTER_RIGHT);
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
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
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
        } else if (actualScreen == MAIN_LIST_SCREEN){
            actualScreen = -1;
            super.onBackPressed();
        }

    }

    static class InternetHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            if(msg.what == InternetReceiver.INTERNET_CONNECTED) {
                if (getInstance().snackbar != null) {
                    getInstance().snackbar.dismiss();
                }
            } else if(msg.what == InternetReceiver.INTERNET_DISCONNECTED) {
                getInstance().showMessage(R.string.no_internet_connection_error_message, Snackbar.LENGTH_INDEFINITE);
            }
        }
    }

}
