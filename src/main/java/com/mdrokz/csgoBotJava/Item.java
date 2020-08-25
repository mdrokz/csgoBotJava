package com.mdrokz.csgoBotJava;

class SteamInfo {
    public String inspect = "";
    public String listings = "";
}

class Price {
    public String price = "";
    public String factoryPrice = "";
}

public class Item {
    public String name = "";
    public String src = "";
    public String quality = "";
    public SteamInfo steamInfo = new SteamInfo();
    public Price price = new Price();
}