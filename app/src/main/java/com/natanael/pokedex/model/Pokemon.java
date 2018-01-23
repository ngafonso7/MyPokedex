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
    private boolean isLoaded;

    public Pokemon(String name) {
        this.name = name;
        this.isLoaded = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        String res = name;
        res = res.substring(0,1).toUpperCase() + res.substring(1,res.length());
        return res;
    }

    public String getStats() {
        String res = "";

        for (Stat s : stats) {
            String stat = s.getStatName().replace("-"," ");
            stat = stat.substring(0,1).toUpperCase() + stat.substring(1,stat.length());
            res += stat + " - " + s.getBaseStat() +"\n";
        }

        return res;
    }

    public int getBaseExperience() {
        return base_experience;
    }

    public int getWeight() {
        return weight;
    }

    public String getAbilities() {
        String res = "";
        if (abilities == null) return res;
        for (Ability a : abilities) {
            String ability = a.getAbilityName();
            ability += ability.substring(0,1).toUpperCase() + ability.substring(1,ability.length());
            res += ability + "\n";
        }
        return res;
    }

    public String getMoves() {
        String res = "";

        for (Move m : moves) {
            String move = m.getMoveName().replace("-"," ");
            move = move.substring(0,1).toUpperCase() + move.substring(1,move.length());
            res += move + "\n";
        }

        return res;
    }

    public String getTypes() {
        String res = "";
        if (types == null) return res;
        for (Type type : types) {
            String typ = type.getTypeName();
            typ = typ.substring(0,1).toUpperCase() + typ.substring(1,typ.length());
            res += typ + " - ";
        }
        res = res.substring(0,res.length() - 3);
        return res;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
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
        private StatName stat;

        public int getBaseStat() {
            return this.base_stat;
        }

        public String getStatName() {
            return stat.name;
        }

        class StatName {
            private String url;
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
