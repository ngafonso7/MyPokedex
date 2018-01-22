package com.natanael.pokedex.model;

import java.util.List;

/**
 * Created by natanael.afonso on 19/01/2018.
 */

public class Pokemon {

    private int id;
    private String name;
    private List<Stat> stats;
    private int base_experience;
    private int weight;
    private List<Ability> abilities;
    private List<Move> moves;
    private List<Type> types;
    private Sprite sprites;

    public Pokemon(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public int getBaseExperience() {
        return base_experience;
    }

    public int getWeight() {
        return weight;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public List<Type> getTypes() {
        return types;
    }

    public String getImage() {
        if (sprites != null) {
            return sprites.getFrontImageUrl();
        }
        return "";
    }

    class Ability {
        private AbilityName ability;

        public String getAbilityName() {
            return this.ability.name;
        }

        class AbilityName {
            private String name;

        }

    }

    class Stat {
        private int base_stat;
        private StatName name;

        public int getBaseStat() {
            return this.base_stat;
        }

        public String getStatName() {
            return this.name.name;
        }

        class StatName {
            private String name;
        }
    }

    class Move {
        private MoveName move;

        public String getMoveName() {
            return this.move.name;
        }

        class MoveName {
            private String name;
        }
    }

    class Type {
        private TypeName type;

        public String getTypeName() {
            return this.type.name;
        }

        class TypeName {
            private String name;

        }
    }

    class Sprite {
        private String back_default;
        private String front_default;

        public String getBackImageUrl() {
            return this.back_default;
        }
        public String getFrontImageUrl() {
            return this.front_default;
        }
    }
}
