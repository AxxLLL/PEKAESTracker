package model.tracker.net;

import com.google.common.base.Preconditions;
import model.tracker.ShippingDetailsData;
import model.tracker.ShippingMainData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/*
* Throws:
*   1) NullPointerException when:
*       a) Document param in constructor is null
*       b) HTML DOM code doesn't have required headers ('formularz-wyszukiwania-przesylki' (section for input form) or 'package-details' (section with main data for shipping))
*       c) When param given for parser methods are null
*   2) InvalidStateException when
*       a) Page doesn't have section with package details (for each pallet)
*   3) IllegalArgumentException when
*       a) Shipping number is invalid
*       b) Element counter for main data is less than 10
*       c) Element counter for pallet data is not equal to 3
*
* */
public class Parser {
    private Element packageMainData;
    private Elements detailsPackagesData;
    private static final String packagesHeader = "formularz-wyszukiwania-przesylki";
    private static final String packagesMainDataHeader = "package-details";

    /*
    * Throws:
    *   1) NullPointerException when page html is invalid (ex. site have problems, mysql errors, or simply connection is invalid)
    *   2) IllegalArgumentException when shipping number is invalid, but input form section is founded. This exception can be thrown when single packages details headers isn't found too.
    * */
    public Parser(Document document) {
        Element formData;
        Preconditions.checkNotNull(document);
        Preconditions.checkNotNull((formData = getFormData(document)),"Strona nie zawiera wymaganych nagłówków ('" + packagesHeader + "')");
        Preconditions.checkArgument(!isInvalidPackageNumberHeader(formData), "Numer przesyłki jest niepoprawny");
        Preconditions.checkNotNull((packageMainData = getShippingMainData(formData)), "Strona nie zawiera wymaganych nagłówków ('" + packagesMainDataHeader + "')");
        detailsPackagesData = getShippingDetailsData(packageMainData);
        Preconditions.checkState(detailsPackagesData.size() > 0, "Strona nie zawiera wymaganych nagłówków (szczegóły przesyłki)");
    }

    /*
    * Throws:
    *   1) IllegalStateException when all headers are founded, but element counter doesn't match to pattern (in this version of site, must be equals 10 rows to
    *       main data, and more to details data)
    * */
    public ShippingMainData getMainData() {
        Preconditions.checkNotNull(packageMainData);
        Elements elements = packageMainData.getElementsByTag("dd");
        Preconditions.checkState(elements.size() >= 11, "(Main) Liczba elementów nie odpowiada obsługiwanemu formatowi danych.");
        return new ShippingMainData(elements.get(0).text(), elements.get(1).text(), elements.get(2).text(), elements.get(3).text(), elements.get(4).text(), elements.get(5).text(), elements.get(6).text(), elements.get(7).text(), elements.get(8).text(), elements.get(9).text());
    }

    /*
    *   Throws:
    *       1) IllegalStateException when element counter is not 3 (3 - because each details data have 3 rows - package num, time and status)
    * */
    public List<ShippingDetailsData> getDetailsData() {
        Preconditions.checkNotNull(detailsPackagesData);
        List<ShippingDetailsData> listOfPackages = new ArrayList<>();
        for(Element element : detailsPackagesData) {
            Elements elements = element.getElementsByTag("dd");
            Preconditions.checkState(elements.size() == 3, "(Details - 1) Liczba elementów nie odpowiada obsługiwanemu formatowi danych.");
            listOfPackages.add(new ShippingDetailsData(elements.get(0).text(), elements.get(1).text(), elements.get(2).text()));
        }
        return listOfPackages;
    }

    private boolean isInvalidPackageNumberHeader(Element element) {
        return element.getElementsByTag("p").size() != 0;
    }

    private Element getFormData(Document document) {
        return document.getElementById(packagesHeader);
    }

    private Element getShippingMainData(Element element) {
        return element.getElementsByClass(packagesMainDataHeader).first();
    }

    private Elements getShippingDetailsData(Element element) {
        return element.getElementsByClass("packages");
    }

}
