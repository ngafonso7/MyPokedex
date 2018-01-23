package com.natanael.pokedex;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.natanael.pokedex.fragment.PokemonDetailsFragment;
import com.natanael.pokedex.fragment.PokemonListFragment;
import com.natanael.pokedex.receiver.InternetReceiver;

public class MainFragmentHolder extends AppCompatActivity {

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
            actualScreen = fragmentId;
            if (fragmentId == MAIN_LIST_SCREEN) {
                //instance.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                changeFragment(new PokemonListFragment());
            } else {
                changeFragment(new PokemonDetailsFragment());
            }
        }
    }

    private static void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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
