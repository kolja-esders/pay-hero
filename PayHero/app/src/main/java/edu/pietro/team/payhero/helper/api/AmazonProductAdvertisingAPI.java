package edu.pietro.team.payhero.helper.api;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import edu.pietro.team.payhero.entities.AmountOfMoney;
import edu.pietro.team.payhero.helper.AmazonSignedRequestsHelper;
import edu.pietro.team.payhero.social.Item;

public class AmazonProductAdvertisingAPI {

    private static final String AWS_ACCESS_KEY_ID = "AKIAIOP4K7SBKF3TCK4A";

    private static final String AWS_SECRET_KEY = "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";

    private static final String AWS_PRODUCT_ADVERTISING_SERVICE_ID = "AWSECommerceService";

    private static final String AWS_ASSOCIATE_TAG = "koljesde-21";

    private static final String ENDPOINT = "webservices.amazon.de";

    public static Item findByEAN13(String ean) {
        AmazonSignedRequestsHelper helper = null;
        try {
             helper = AmazonSignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            // We're fucked :)
        }

        Map<String, String> params = new HashMap<>();
        params.put("Service", AWS_PRODUCT_ADVERTISING_SERVICE_ID);
        params.put("Operation", "ItemLookup");
        params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
        params.put("AssociateTag", AWS_ASSOCIATE_TAG);
        params.put("ItemId", ean);
        params.put("IdType", "EAN");
        params.put("ResponseGroup", "Images,ItemAttributes,Offers");
        params.put("SearchIndex", "All");

        String url = helper.sign(params);
        try {
            Document response = getResponse(url);
            printResponse(response);
            return doStuff(response);
        } catch (Exception ex) {
            // Catching responses is for losers (is it though?!)
        }
        return null;
    }

    private static boolean isPopulated(Item item) {
        return item != null && item.getImageUrl() != null && item.getName() != null && item.getRetailPrice() != null;
    }

    private static Item doStuff(Document response) {
        NodeList items = response.getElementsByTagName("Item");
        Item result = null;
        if (items.getLength() > 0) {
            for (int i = 0; i < items.getLength(); ++i) {
                Element item = (Element) items.item(i);
                items = item.getElementsByTagName("ProductGroup");
                if (items.getLength() > 0) {
                    result = parseItem(item);
                    if (isPopulated(result)) {
                        return result;
                    }
                }
            }
            // Fallback: Just pick the first element and get any price.
            Element firstElement = (Element) items.item(0);
            items = firstElement.getElementsByTagName("ProductGroup");

            if (items.getLength() > 0 ) {
                return parseItemAnyPrice(firstElement);
            }
        }
        return isPopulated(result) ? result : null;
    }

    private static Item parseItemAnyPrice(Element foundItem) {
        Item item = new Item();
        NodeList prices = foundItem.getElementsByTagName("LowestNewPrice");
        if (prices.getLength() > 0) {
            NodeList childNodes = prices.item(0).getChildNodes();
            Double amount = 0.d;
            Currency currency = Currency.getInstance("EUR");
            for (int i = 0; i < childNodes.getLength(); ++i) {
                Node n = childNodes.item(i);
                if (n.getNodeName().equals("Amount")) {
                    String rawAmount = n.getTextContent();
                    amount = Double.valueOf(rawAmount) / 100;
                } else if (n.getNodeName().equals("CurrencyCode")) {
                    String rawCurrencyCode = n.getTextContent();
                    currency = Currency.getInstance(rawCurrencyCode);
                }
            }
            item.setRetailPrice(new AmountOfMoney(amount, currency));
        }
        NodeList titles = foundItem.getElementsByTagName("Title");
        if (titles.getLength() > 0) {
            item.setName(titles.item(0).getTextContent());
        }
        NodeList publishers = foundItem.getElementsByTagName("Publisher");
        if (publishers.getLength() > 0) {
            item.setProducer(publishers.item(0).getTextContent());
        }
        NodeList largeImages = foundItem.getElementsByTagName("LargeImage");
        if (largeImages.getLength() > 0) {
            Element largeImage = (Element) largeImages.item(0);
            NodeList urls = largeImage.getElementsByTagName("URL");
            if (urls.getLength() > 0) {
                item.setImageUrl(urls.item(0).getTextContent());
            }
        }
        return item;
    }

    // We are just going to parse the first item for now.
    private static Item parseItem(Element foundItem) {
        Item item = new Item();
        NodeList prices = foundItem.getElementsByTagName("LowestNewPrice");
        if (prices.getLength() == 0) {
            prices = foundItem.getElementsByTagName("LowestUsedPrice");
        }
        if (prices.getLength() > 0) {
            NodeList childNodes = prices.item(0).getChildNodes();
            Double amount = 0.d;
            Currency currency = Currency.getInstance("EUR");
            for (int i = 0; i < childNodes.getLength(); ++i) {
                Node n = childNodes.item(i);
                if (n.getNodeName().equals("Amount")) {
                    String rawAmount = n.getTextContent();
                    amount = Double.valueOf(rawAmount) / 100;
                } else if (n.getNodeName().equals("CurrencyCode")) {
                    String rawCurrencyCode = n.getTextContent();
                    currency = Currency.getInstance(rawCurrencyCode);
                }
            }
            item.setRetailPrice(new AmountOfMoney(amount, currency));
        }
        NodeList titles = foundItem.getElementsByTagName("Title");
        if (titles.getLength() > 0) {
            item.setName(titles.item(0).getTextContent());
        }
        NodeList publishers = foundItem.getElementsByTagName("Publisher");
        if (publishers.getLength() > 0) {
            item.setProducer(publishers.item(0).getTextContent());
        }
        NodeList largeImages = foundItem.getElementsByTagName("LargeImage");
        if (largeImages.getLength() > 0) {
            Element largeImage = (Element) largeImages.item(0);
            NodeList urls = largeImage.getElementsByTagName("URL");
            if (urls.getLength() > 0) {
                item.setImageUrl(urls.item(0).getTextContent());
            }
        }
        return item;
    }

    private static Document getResponse(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(url);
        return doc;
    }

    private static void printResponse(Document doc) throws TransformerException, FileNotFoundException {
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        Properties props = new Properties();
        props.put(OutputKeys.INDENT, "yes");
        trans.setOutputProperties(props);
        StreamResult res = new StreamResult(new StringWriter());
        DOMSource src = new DOMSource(doc);
        trans.transform(src, res);
        String toString = res.getWriter().toString();
        System.out.println(toString);
    }
}
