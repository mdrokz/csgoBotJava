package com.mdrokz.csgoBotJava;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    public static final Map<String, String> Alias = new LinkedHashMap<String, String>();

    static {
        // initialize the item aliases using HashMap
        initializeAliases();
    }

    private static void initializeAliases() {
        // pistols
        Alias.put("PISTOLS:","PISTOLS:");
        Alias.put("cz", "CZ75-Auto");
        Alias.put("deagle", "Desert+Eagle");
        Alias.put("dualb", "Dual+Berettas");
        Alias.put("fiveseven", "Five-SeveN");
        Alias.put("glock", "Glock-18");
        Alias.put("p2000", "P2000");
        Alias.put("p250", "P250");
        Alias.put("revolver", "R8+Revolver");
        Alias.put("tec9", "Tec-9");
        Alias.put("usp", "USP-S");

        // rifles
        Alias.put("RIFLES:","RIFLES:");
        Alias.put("ak47", "AK-47");
        Alias.put("aug", "AUG");
        Alias.put("awp", "AWP");
        Alias.put("famas", "FAMAS");
        Alias.put("g3sg", "G3SG1");
        Alias.put("galil", "Galil+AR");
        Alias.put("m4s", "M4A1-S");
        Alias.put("m4", "M4A4");
        Alias.put("scar", "SCAR-20");
        Alias.put("sg", "SG+553");
        Alias.put("scout", "SSG+08");

        // smg
        Alias.put("SMG:","SMG:");
        Alias.put("mac10", "MAC-10");
        Alias.put("mp5", "MP5-SD");
        Alias.put("mp7", "MP7");
        Alias.put("mp9", "MP9");
        Alias.put("ppbizon", "PP-Bizon");
        Alias.put("p90", "P90");
        Alias.put("ump", "UMP-45");

        // heavy
        Alias.put("HEAVY:","HEAVY:");
        Alias.put("mag7", "MAG-7");
        Alias.put("nova", "Nova");
        Alias.put("sawedoff", "Sawed-Off");
        Alias.put("xm1014", "XM1014");
        Alias.put("m249", "M249");
        Alias.put("negev", "Negev");

        // knives
        Alias.put("KNIVES:","KNIVES:");
        Alias.put("nomad", "Nomad+Knife");
        Alias.put("skeleton", "Skeleton+Knife");
        Alias.put("survival", "Survival+Knife");
        Alias.put("paracord", "Paracord+Knife");
        Alias.put("classic", "Classic+Knife");
        Alias.put("bayonet", "Bayonet");
        Alias.put("bowie", "Bowie+Knife");
        Alias.put("butterfly", "Butterfly+Knife");
        Alias.put("falchion", "Falchion+Knife");
        Alias.put("flip", "Flip+Knife");
        Alias.put("gut", "Gut+Knife");
        Alias.put("huntsman", "Huntsman+Knife");
        Alias.put("karambit", "Karambit");
        Alias.put("m9bayonet", "M9+Bayonet");
        Alias.put("navaja", "Navaja+Knife");
        Alias.put("shadow", "Shadow+Daggers");
        Alias.put("stiletto", "Stiletto+Knife");
        Alias.put("talon", "Talon+Knife");
        Alias.put("ursus", "Ursus+Knife");
    }

}