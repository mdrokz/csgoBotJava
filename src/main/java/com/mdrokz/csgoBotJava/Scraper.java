package com.mdrokz.csgoBotJava;

// Java Imports
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// Jsoup Imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

enum UrlType {
    Base, Weapon
}

public class Scraper {

    private String siteUrl;

    private String siteTitle;

    private UrlType urlType = UrlType.Base;

    private Document siteDoc;

    public Scraper(String _siteUrl) throws IOException {
        siteUrl = _siteUrl;
        siteDoc = Jsoup.connect(siteUrl).get();
        siteTitle = siteDoc.title();
        if (siteUrl.contains("/weapon")) {
            urlType = UrlType.Weapon;
        }
        System.out.println(String.format("Scraping %s", siteTitle));
    }

    public Optional<Map<String, Item>> getIndex() {

        Map<String, Item> items = new LinkedHashMap<String, Item>();

        Elements elements = siteDoc.select(".row");

        Optional<Elements> cards = Optional.ofNullable(null);

        for (Element e : elements) {
            Elements v = e.select(".result-box");
            if (v.size() != 0) {
                cards = Optional.of(v);
                break;
            }
        }

        for (Element card : cards.orElse(null)) {
            if (card != null) {

                Item item = new Item();
                Elements headers = card.select("h3");
                Optional<Element> headerElement = Optional
                        .ofNullable(headers.isEmpty() ? null : (Element) headers.toArray()[0]);
                if (urlType != UrlType.Base && headerElement.isPresent()) {
                    Element header = headerElement.get();
                    int headerChildrenSize = header.children().size();
                    if (headerChildrenSize > 0) {
                        String name = header.child(0).text();
                        item.name = name;
                    } else {
                        continue;
                    }
                } else if (headerElement.isPresent()) {
                    Element header = headerElement.get();

                    int headerChildrenSize = header.children().size();

                    String name = header.child(0).text();
                    String type = "";
                    if (headerChildrenSize > 1) {
                        type = header.child(1).text();
                    }
                    item.name = type + " " + "|" + " " + name;
                } else {
                    continue;
                }

                headerElement = null;

                Element qualityElement = (Element) card.select(".quality").toArray()[0];

                String quality = qualityElement.child(0).text();

                item.quality = quality;

                qualityElement = null;

                Element e = (Element) card.select(".img-responsive").toArray()[0];

                item.src = e.attr("src");

                Object[] prices = card.select(".price").toArray();

                if (prices.length > 0) {

                    Element normalPriceElement = (Element) prices[0];
                    Element statTrakPriceElement = (Element) prices[1];
                    
                    String normalPrice = normalPriceElement.child(0).child(0).text();

                    item.price.price = normalPrice;

                    if (statTrakPriceElement.child(0).children().size() > 0) {

                        String statTrakPrice = statTrakPriceElement.child(0).child(0).text();
                        item.price.factoryPrice = statTrakPrice;
                    }
                }
                
                Object[] listings = card.select(".market-button-skin").toArray();

                if(listings.length > 0)
                item.steamInfo.listings = ((Element) listings[0]).attr("href");

                items.put(item.name, item);
            }
        }

        return Optional.ofNullable(items);
    }
}