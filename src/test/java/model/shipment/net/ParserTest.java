package model.shipment.net;

import model.shipment.shp.ShippingDetailsData;
import model.shipment.shp.ShippingMainData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class ParserTest {
    private final static File HTML_VALID_PAGE_DATA = new File(ParserTest.class.getResource("/PEKAES_HTML_PL0895790004909004.txt").getFile());
    private final static File HTML_INVALID_SHIPPING_NUMBER = new File(ParserTest.class.getResource("/PEKAES_HTML_INVALID_SHIPPING_NUMBER.txt").getFile());
    private final static File HTML_INVALID_PAGE_DATA = new File(ParserTest.class.getResource("/PEKAES_HTML_INVALID_PAGE.txt").getFile());
    private final static File HTML_VALID_NUMBER_BUT_WITHOUT_DETAILS = new File(ParserTest.class.getResource("/PEKAES_HTML_WITHOUT_DETAILS.txt").getFile());
    private final static File HTML_VALID_NUMBER_BUT_WITHOUT_MAIN_DATA = new File(ParserTest.class.getResource("/PEKAES_HTML_WITHOUT_MAIN_DATA.txt").getFile());
    private final static File HTML_VALID_NUMBER_WITHOUT_SOME_MAIN_DATA = new File(ParserTest.class.getResource("/PEKAES_HTML_VALID_WITHOUT_SOME_MAIN_DATA_ELEMENTS.txt").getFile());
    private final static File HTML_VALID_NUMBER_WITHOUT_SOME_DETAILS_DATA = new File(ParserTest.class.getResource("/PEKAES_HTML_VALID_NUMBER_WITHOUT_SOME_DETAILS_ROWS.txt").getFile());

    private Document validPageData;
    private Document invalidSHippingNumberData;
    private Document invalidPageData;
    private Document getValidShippingNumberWithoutDetailsData;
    private Document getValidShippingNumberWithoutMainData;
    private Document getValidShippingNumberWithoutSomeMainData;
    private Document getValidShippingNumberWithoutSomeDetailsData;


    @BeforeEach
    void beforeEach() throws IOException {
        validPageData = Jsoup.parse(HTML_VALID_PAGE_DATA, "UTF-8");
        invalidPageData = Jsoup.parse(HTML_INVALID_PAGE_DATA, "UTF-8");
        invalidSHippingNumberData = Jsoup.parse(HTML_INVALID_SHIPPING_NUMBER, "UTF-8");
        getValidShippingNumberWithoutDetailsData = Jsoup.parse(HTML_VALID_NUMBER_BUT_WITHOUT_DETAILS, "UTF-8");
        getValidShippingNumberWithoutMainData = Jsoup.parse(HTML_VALID_NUMBER_BUT_WITHOUT_MAIN_DATA, "UTF-8");
        getValidShippingNumberWithoutSomeMainData = Jsoup.parse(HTML_VALID_NUMBER_WITHOUT_SOME_MAIN_DATA, "UTF-8");
        getValidShippingNumberWithoutSomeDetailsData = Jsoup.parse(HTML_VALID_NUMBER_WITHOUT_SOME_DETAILS_DATA, "UTF-8");
    }

    @Test
    @DisplayName("Parser: Null HTML DOM param should throw NullPointerException")
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> new Parser(null));
    }

    @Test
    @DisplayName("Parser: Not null, but invalid page HTML code should thrown IllegalStateException")
    void test_2() {
        assertThatIllegalStateException().isThrownBy(() -> new Parser(invalidPageData));
    }

    @Test
    @DisplayName("Parser: Valid HTML DOM data, but for invalid shp number should throw InvalidArgumentException")
    void test_3() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Parser(invalidSHippingNumberData));
    }

    @Test
    @DisplayName("Parser: Valid shp number, but without main data section data should thrown IllegalStateException")
    void test_4() {
        assertThatIllegalStateException().isThrownBy(() -> new Parser(getValidShippingNumberWithoutMainData));
    }

    @Test
    @DisplayName("Parser: Valid shp number, but without details section data should thrown IllegalStateException")
    void test_5() {
        assertThatIllegalStateException().isThrownBy(() -> new Parser(getValidShippingNumberWithoutDetailsData));
    }

    @Test
    @DisplayName("getMainData: Valid HTML DOM structure, but without required main data rows, should throw IllegalStateException")
    void test_6() {
        assertThatIllegalStateException().isThrownBy(() -> new Parser(getValidShippingNumberWithoutSomeMainData).getMainData());
    }

    @Test
    @DisplayName("getMainData: Valid HTML DOM structure, but without required details data rows, should throw IllegalStateException")
    void test_7() {
        assertThatIllegalStateException().isThrownBy(() -> new Parser(getValidShippingNumberWithoutSomeDetailsData).getDetailsData());
    }

    @Test
    @DisplayName("Parser: All valid elements should return POJO structure with data from HTML")
    void test_8() {
        Parser parser = new Parser(validPageData);
        ShippingMainData mainData = parser.getMainData();
        ShippingDetailsData detailsDataData = parser.getDetailsData().get(0);

        assertThat(mainData.getShippingNumber()).isEqualTo("PL0895790004909004");
        assertThat(mainData.getPostingTerminal()).isEqualTo("PLBYD");
        assertThat(mainData.getPostingCountry()).isEqualTo("PL");
        assertThat(mainData.getPostingData()).isEqualTo("2018-07-25");
        assertThat(mainData.getDeliveryTerminal()).isEqualTo("PLBYD");
        assertThat(mainData.getDeliveryCountry()).isEqualTo("PL");
        assertThat(mainData.getDeliveryType()).isEqualTo("drobnicowa");
        assertThat(mainData.getAmountOfPackages()).isEqualTo("1");
        assertThat(mainData.getDeliveryStatus()).isEqualTo("przesyłka doręczona");
        assertThat(mainData.getDeliveryPlace()).isEqualTo("87-100, TORUN");
        assertThat(detailsDataData.getPackageNumber()).isEqualTo("PL0895790004909004");
        assertThat(detailsDataData.getPackageTime()).isEqualTo("2018-07-30 12:10");
        assertThat(detailsDataData.getPackageStatus()).isEqualTo("dostarczono");


    }
}
