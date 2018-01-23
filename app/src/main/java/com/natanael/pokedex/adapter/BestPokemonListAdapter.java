package com.natanael.pokedex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.natanael.pokedex.R;
import com.natanael.pokedex.model.BestPokemonList;
import com.natanael.pokedex.model.Pokemon;
import com.natanael.pokedex.model.PokemonList;
import com.squareup.picasso.Picasso;

/**
 * Created by natanael.afonso on 23/01/2018.
 */

public class BestPokemonListAdapter extends RecyclerView.Adapter<BestPokemonListAdapter.PokemonViewHolder>  {

    private PokemonList pokemonListInstance;

    public BestPokemonListAdapter() {
        pokemonListInstance = PokemonList.getInstance();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.best_pokemon_list_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(layoutForListItem, parent, false);

        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        if (BestPokemonList.getInstance().getBestPokemonList().size() > 0) {
            holder.bind(BestPokemonList.getInstance().getBestPokemonList().get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (BestPokemonList.getInstance().getBestPokemonList() != null) {
            return BestPokemonList.getInstance().getBestPokemonList().size();
        }
        return 0;
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder{

        final Context context;

        final ImageView pokemonImageView;
        final TextView pokemonNameTextView;

        private PokemonViewHolder(View itemView) {
            super(itemView);

            pokemonImageView = itemView.findViewById(R.id.im_best_pokemon_image);
            pokemonNameTextView = itemView.findViewById(R.id.tv_best_pokemon_name);

            context = itemView.getContext();
        }

        private void bind(Pokemon details) {
            if (!"".equals(details.getImage())) {
                Picasso.with(context)
                        .load(details.getImage())
                        .placeholder(R.drawable.loading_pokeball_08)
                        .into(pokemonImageView);
            }
            pokemonNameTextView.setText(details.getName());
        }
    }
}
