package com.natanael.pokedex.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.natanael.pokedex.R;
import com.natanael.pokedex.model.Pokemon;
import com.natanael.pokedex.model.PokemonList;
import com.squareup.picasso.Picasso;

/**
 * Created by Natanael G Afonso on 22/01/2018.
 */

public class PokemonDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pokemon_details, container, false);

        ImageView pokemonImageView = rootView.findViewById(R.id.im_pokemon_details_image);
        TextView pokemonNameTextView = rootView.findViewById(R.id.tv_pokemon_name);
        TextView pokemonWeightTextView = rootView.findViewById(R.id.tv_pokemon_weight);
        TextView pokemonBaseExpTextView = rootView.findViewById(R.id.tv_pokemon_baseexp);
        TextView pokemonTypeTextView = rootView.findViewById(R.id.tv_pokemon_type);

        TextView pokemonStatsTextView = rootView.findViewById(R.id.tv_pokemon_stats);
        TextView pokemonAbilitiesTextView = rootView.findViewById(R.id.tv_pokemon_abilities);
        TextView pokemonMovesTextView = rootView.findViewById(R.id.tv_pokemon_moves);

        ImageView pokemonCaughtImageView = rootView.findViewById(R.id.im_pokemon_caught);

        pokemonCaughtImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pokemon details = PokemonList.getInstance().getSelectedPokemon();
                if (details.isCaught()) {
                    details.setCaught(false);
                    ((ImageView) view).setImageResource(R.drawable.pokeball_open);
                    PokemonList.getInstance().removeCaughtPokemon(details.getId());
                } else {
                    details.setCaught(true);
                    ((ImageView) view).setImageResource(R.drawable.pokeball_closed);
                    PokemonList.getInstance().setCaughtPokemon(details);
                }

            }
        });

        Pokemon details = PokemonList.getInstance().getSelectedPokemon();

        if (!"".equals(details.getImage())) {
            Picasso.with(getActivity().getApplicationContext())
                    .load(details.getImage())
                    .placeholder(R.drawable.loading_pokeball_08)
                    .into(pokemonImageView);
        }

        pokemonNameTextView.setText(details.getName());
        pokemonWeightTextView.setText(String.valueOf(details.getWeight()));
        pokemonBaseExpTextView.setText(String.valueOf(details.getBaseExperience()));
        pokemonTypeTextView.setText(details.getTypes());

        pokemonStatsTextView.setText(details.getStats());
        pokemonAbilitiesTextView.setText(details.getAbilities());
        pokemonMovesTextView.setText(details.getMoves());

        if (details.isCaught()) {
            pokemonCaughtImageView.setImageResource(R.drawable.pokeball_closed);
        } else {
            pokemonCaughtImageView.setImageResource(R.drawable.pokeball_open);
        }

        return rootView;

    }
}
