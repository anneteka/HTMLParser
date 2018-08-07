package aboutyou;

import org.apache.commons.lang.time.StopWatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.File;
import java.util.LinkedHashSet;
import javax.xml.bind.*;


public class Parser {
    private int pages;
    private String searchUrl;
    private Offers offers;
    private LinkedHashSet<String> urls;
    private int triggeredHTTPRequests, extractedProducts;
    private StopWatch stopWatch;

    public Parser(String url, String keyword) throws IOException {
        stopWatch=new StopWatch();
        stopWatch.start();
        Document document = Jsoup.connect(url).get();
        Elements pagesNumbers = document.getElementsByClass("styles__pageNumbers--1Lsj_");
        triggeredHTTPRequests = 1;
        extractedProducts = 0;
        urls = new LinkedHashSet<>();
        try {
            pages = Integer.parseInt((pagesNumbers.last().text()));
        } catch (Exception e) {
            pages = 1;
        }
        searchUrl = url;
        offers = new Offers();
        for (int i = 1; i <= pages; i++) {
            parseSearchPage(searchPage(i));
        }
        productToXML(offers, keyword);
        stopWatch.stop();
        printInfo();
    }

    private void parseSearchPage(String url) throws IOException {
        Document searchPage = Jsoup.connect(url).get();
        triggeredHTTPRequests++;
        Elements products = searchPage.getElementsByClass("styles__tile--2s8XN");
        for (Element e : products) {
            String productLink = e.select("a").attr("href");
            if (urls.add(productLink)) {
                parseProductPage(productLink); //saves time if this page was already parsed
                extractedProducts++;
            }
        }

    }

    private void parseProductPage(String url) throws IOException {
        String productLink = "https://aboutyou.de" + url;
        Document productPage = Jsoup.connect(productLink).get();
        triggeredHTTPRequests++;
        Offer currentOffer = parsedProduct(productPage);
        Elements currentSizes = productPage.getElementsByClass("styles__row--sWR75");
        addSizes(currentOffer, currentSizes);
        Elements colors = productPage.getElementsByClass("styles__thumbnailWrapper--3uDnG");
        for (Element color : colors) {
            if (urls.add(color.select("a").attr("href"))) {
                Document document = Jsoup.connect("https://aboutyou.de" + color.select("a").attr("href")).get();
                triggeredHTTPRequests++;
                Offer colorProduct = parsedProduct(document);
                Elements sizes = document.getElementsByClass("styles__row--sWR75");
                addSizes(colorProduct, sizes);
            }
        }
    }

    private void addSizes(Offer offer, Elements sizes) {
        for (int i = 0; i < sizes.size(); i++) {
            Offer size = new Offer(offer);
            size.setSize(sizes.get(i).getElementsByClass("styles__column--dXc1E").get(0).text());
            offers.getOffers().add(size);
        }
        if (sizes.size()==0) offers.getOffers().add(offer);
    }

    private void productToXML(Offers offers, String keyword) {
        try {
            File file = new File(keyword+".xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Offers.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(offers, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private String searchPage(int n) {
        return searchUrl + "&sort=topseller&page=" + n;
    }

    private Offer parsedProduct(Document productPage) {
        Offer offer = new Offer();
       try {
           offer.setName(productPage.getElementsByClass("styles__title--3Jos_").first().text());
       }
       catch (Exception e){}
       try{
           offer.setBrand(productPage.getElementsByClass("styles__titleContainer--33zw2").get(0).getElementsByTag("h1").text().split("\\|")[0]);
       }catch (Exception e){}
        try{
           offer.setColor(productPage.getElementsByClass("styles__isHoveredState--2BBt9").text());
        }catch (Exception e){}
       try{
           offer.setPrice(productPage.getElementsByClass("productPrices").first().text());
       }catch (Exception e){}
        try {
            offer.setInitialPrice(productPage.getElementsByClass("priceStyles__strike--PSBGK").last().text());
        } catch (Exception e) {
        }
        try {
            Elements descriptions = productPage.getElementsByClass("styles__accordionContainer--1dPP0");
            String description = "";
            String articleID = "";
            for (Element des : descriptions) {
                for (Element e : des.getElementsByTag("li"))
                    if (e.text().length() < 10) description += e.text() + "\n";
                    else if (e.text().length() >= 10 && !e.text().substring(0, 10).equals("Artikel-Nr"))
                        description += e.text() + "\n";
                    else articleID = e.text().substring(12);
            }
            offer.setDescription(description);
            offer.setArticleID(articleID);
        }catch (Exception e){}
        try{
           offer.setShippingCosts(productPage.getElementsByClass("styles__label--1cfc7").last().text());
        }catch (Exception e){}
        return offer;
    }

    private void printInfo() {
        System.out.println("Amount of triggered HTTP requests: " + triggeredHTTPRequests);
        System.out.println("Run-time: "+stopWatch.getTime()/1000+"s");
        System.out.println("Memory footprint: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024))+"b");
        System.out.println("Amount of extracted products: "+extractedProducts);
        System.out.println("Amount of extracted offers (with sizes and colors): "+offers.getOffers().size());
    }
}
