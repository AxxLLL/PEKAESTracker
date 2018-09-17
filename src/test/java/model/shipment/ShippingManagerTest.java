package model.shipment;

import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class ShippingManagerTest {
    private ShippingManager manager;
    private Shipping shipping_1 = new Shipping("TEST_A", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private Shipping shipping_2 = new Shipping("TEST_B", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private Shipping shipping_3 = new Shipping("TEST_C", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private Shipping shipping_4 = new Shipping("TEST_D", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);

    @BeforeEach
    void beforeEach() {
        manager = new ShippingManager();
    }

    @DisplayName("add: Null param should throw null pointer exception")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> manager.add(null));
    }

    @DisplayName("add/get/size: Check if valid shipment is added to list")
    @Test
    void test_2() {
        Shipping shipping = new Shipping("TEST", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
        manager.add(shipping);
        assertThat(manager.size()).isOne();
        assertThat(manager.get(0)).isEqualTo(shipping);
        assertThat(manager.get(0).getShippingNumber()).isEqualTo("TEST");
    }

    @DisplayName("add: Try to add this same shp number to list should throw IllegalArgumentException")
    @Test
    void test_3() {
        Shipping shipping_1 = new Shipping("TEST", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
        Shipping shipping_2 = new Shipping("TEST", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);

        manager.add(shipping_1);
        assertThatIllegalArgumentException().isThrownBy(() -> manager.add(shipping_2));
    }

    @DisplayName("addAll: Try to add this same shp number to list should throw IllegalArgumentException")
    @Test
    void test_4() {
        addDefaultShipments();
        Shipping shipping_5 = new Shipping("TEST_A", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null); //This row should not be added
        Shipping shipping_6 = new Shipping("TEST_B", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null); //This row should not be added

        manager.addAll(Arrays.asList(shipping_1, shipping_2, shipping_3, shipping_4));
        assertThat(manager.size()).isEqualTo(4);
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_2, shipping_3, shipping_4);

        manager.addAll(Arrays.asList(shipping_5, shipping_6)); //Should not be added
        assertThat(manager.size()).isEqualTo(4);
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_2, shipping_3, shipping_4);
    }

    @DisplayName("addAll: Null param should throw NullPointerException")
    @Test
    void test_5() {
        assertThatNullPointerException().isThrownBy(() -> manager.addAll(null));
    }

    @DisplayName("get: Test valid values")
    @Test
    void test_6() {
        addDefaultShipments();
        assertThat(manager.get(0)).isEqualTo(shipping_1);
        assertThat(manager.get(1)).isEqualTo(shipping_2);
        assertThat(manager.get(2)).isEqualTo(shipping_3);
        assertThat(manager.get(3)).isEqualTo(shipping_4);
    }

    @DisplayName("get: Invalid index values should throw exception")
    @Test
    void test_7() {
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> manager.get(-1));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> manager.get(0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> manager.get(1));
    }

    @DisplayName("getByShippingNumber: Valid values should return Shipping objects from list")
    @Test
    void test_8() {
        addDefaultShipments();

        assertThat(manager.getShippingByNumber("TEST_A")).isEqualTo(shipping_1);
        assertThat(manager.getShippingByNumber("TEST_B")).isEqualTo(shipping_2);
        assertThat(manager.getShippingByNumber("TEST_C")).isEqualTo(shipping_3);
        assertThat(manager.getShippingByNumber("TEST_D")).isEqualTo(shipping_4);
    }

    @DisplayName("getByShippingNumber: Invalid value should return null")
    @Test
    void test_9() {
        addDefaultShipments();
        assertThat(manager.getShippingByNumber("TEST_E")).isNull();
    }

    @DisplayName("getByShippingNumber: Null param should throw NullPointerException")
    @Test
    void test_10() {
        addDefaultShipments();
        assertThatNullPointerException().isThrownBy(() -> manager.getShippingByNumber(null));
    }

    @DisplayName("getAll: Should return copy of list from manager")
    @Test
    void test_11() {
        addDefaultShipments();
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_2, shipping_3, shipping_4);
    }

    @DisplayName("remove: Invalid index should throw IndexOutOfBounds exception")
    @Test
    void test_12() {
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> manager.remove(-1));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> manager.remove(0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> manager.remove(1));
    }

    @DisplayName("remove: Valid index should remove item from list")
    @Test
    void test_13() {
        addDefaultShipments();
        manager.remove(3);
        manager.remove(0);
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_2, shipping_3);
    }

    @DisplayName("remove: Null param at Shipping should throw NullPointerException")
    @Test
    void test_14() {
        Shipping shp = null;
        assertThatNullPointerException().isThrownBy(() -> manager.remove(shp));
    }

    @DisplayName("remove: Valid shp object should remove items from list")
    @Test
    void test_15() {
        addDefaultShipments();
        manager.remove(shipping_2);
        manager.remove(shipping_4);
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_3);
    }

    @DisplayName("remove: Valid shp object, but not at list should do nothing with list")
    @Test
    void test_16() {
        addDefaultShipments();
        Shipping shipping_5 = new Shipping("TEST_D", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
        manager.remove(shipping_5);
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_2, shipping_3, shipping_4);
    }

    @DisplayName("remove: Valid shp object should remove items from list")
    @Test
    void test_17() {
        addDefaultShipments();
        manager.remove("TEST_B");
        manager.remove("TEST_D");
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_3);
    }

    @DisplayName("remove: Valid shp but not at list should do nothing")
    @Test
    void test_18() {
        addDefaultShipments();
        manager.remove("TEST_F");
        assertThat(manager.getAll()).containsExactlyInAnyOrder(shipping_1, shipping_2, shipping_3, shipping_4);
    }

    @DisplayName("remove: Null param at shNumber should throw NullPointerException")
    @Test
    void test_19() {
        String str = null;
        assertThatNullPointerException().isThrownBy(() -> manager.remove(str));
    }

    @DisplayName("clear: Should clear list")
    @Test
    void test_20() {
        addDefaultShipments();
        assertThat(manager.size()).isEqualTo(4);
        manager.clear();
        assertThat(manager.size()).isZero();
    }

    private void addDefaultShipments() {
        manager.addAll(Arrays.asList(shipping_1, shipping_2, shipping_3, shipping_4));
    }
}