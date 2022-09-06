package calculator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Currency {

    public static void main(String[] args)  {

        Map<String, Double> currencyMap = new HashMap<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parsowanie dokumentu XML
            DocumentBuilder db = dbf.newDocumentBuilder();

            String www = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml?format=xml";
            Document document = db.parse(new URL(www).openStream());

            // wyciąganie danych po wg nazwy tag -> "Cube"
            NodeList currencyList = document.getDocumentElement().getElementsByTagName("Cube");
            for (int i = 2; i < currencyList.getLength(); i++) {
                Node currency = currencyList.item(i);
                if (currency.getNodeType() == Node.ELEMENT_NODE) {
                    Element currencyElement = (Element) currency;
                    // przypisanie do zmiennych atrybutów: "currency" i "rate"
                    String key = currencyElement.getAttribute("currency");
                    String value = currencyElement.getAttribute("rate");
                    // przypisanie kluczy (nazwy waluty) i wartości (wartości waluty) w Mapowaniu
                    currencyMap.put(key, Double.parseDouble(value));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        System.out.println("KALKULATOR WALUT\n");
        System.out.println("Podaj jeden kurs waluty do przeliczenia podanych poniżej (możesz napisać małą czcionką).");
        System.out.println(currencyMap.keySet());
        Scanner scan = new Scanner(System.in);

        // pętla do pobierania poszczególnych danych od użytkowanika i obliczanie walut
        boolean error = true;
        do {
            try {
                String currencyInput = scan.nextLine();
                if (currencyMap.containsKey(currencyInput.toUpperCase()) == false) {
                    throw new NullPointerException();
                }
                System.out.println("Podaj wartość kwotę w EUR");
                Double currencyValue = scan.nextDouble();
                Double result = currencyValue * currencyMap.get(currencyInput.toUpperCase());
                System.out.println("\n" + currencyValue + " EUR = " + result + " " + currencyInput.toUpperCase());
                error = false;
            } catch (NullPointerException e) {
                System.err.println("Nie podano prawidłowej wartości. Spróbuj jeszcze raz...");
            } catch (InputMismatchException ex) {
                System.err.println("Oczekiwane są wartości liczbowe. Zacznij od początku.");
                System.out.println("\nPodaj jezcze raz nazwę kursu waluty do przeliczenia.");
                scan.nextLine();
            }
        } while (error);

        System.out.println("Przeliczanie waluty wykonano pomyślnie.");
    }
}
