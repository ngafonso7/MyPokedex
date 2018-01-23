package com.natanael.pokedex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by natanael.afonso on 22/01/2018.
 */

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder> {

    private PokemonList pokemonListInstance;

    private final ListItemClickListener onClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Pokemon clickedPokemon);
    }

    public PokemonListAdapter(ListItemClickListener clickListener) {
        pokemonListInstance = PokemonList.getInstance();
        onClickListener = clickListener;
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.pokemon_list_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(layoutForListItem, parent, false);

        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        if (pokemonListInstance.getPokemonDetailsList() != null) {
            holder.bind(pokemonListInstance.getPokemonDetailsList().get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (pokemonListInstance.getPokemonDetailsList() != null) {
            return pokemonListInstance.getPokemonDetailsList().size();
        }
        return 0;
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final Context context;

        final ImageView pokemonImageView;
        final TextView pokemonNameTextView;
        final ImageView pokemonCaughtImageView;


        private PokemonViewHolder(View itemView) {
            super(itemView);

            pokemonImageView = itemView.findViewById(R.id.im_pokemon_image);
            pokemonNameTextView = itemView.findViewById(R.id.tv_pokemon_name);
            pokemonCaughtImageView = itemView.findViewById(R.id.im_pokemon_list_caught);

            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        private void bind(Pokemon details) {
            String pokemonName = details.getName();
            String pokemonImageUrl = details.getImage();

            if (!"".equals(pokemonImageUrl)) {
                Picasso.with(context)
                        .load(pokemonImageUrl)
                        .placeholder(R.drawable.loading_pokeball_08)
                        .into(pokemonImageView);
            } else {
                pokemonImageView.setImageResource(R.drawable.loading_pokeball_08);
            }

            if (!"".equals(pokemonName)) {
                pokemonName = pokemonName.substring(0,1).toUpperCase() + pokemonName.substring(1);
            }
            pokemonNameTextView.setText(pokemonName);

            if (details.isCaught()) {
                pokemonCaughtImageView.setVisibility(View.VISIBLE);
            } else {
                pokemonCaughtImageView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            if (clickedPosition >= 0) {
                onClickListener.onListItemClick(pokemonListInstance.getPokemonDetailsList().get(clickedPosition));
            }
        }


    }
}
