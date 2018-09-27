package model.shipment.shp;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

public class ShippingManager {
    private List<Shipping> listOfShipping = new ArrayList<>();

    public void add(Shipping shipping) {
        Preconditions.checkNotNull(shipping, "Number przesyłki musi zostać podany");
        Preconditions.checkArgument(!isShippingNumberAtList(shipping.getShippingNumber()), "Przesyłka o podanym numerze już znajduje się na liście");
        listOfShipping.add(shipping);
    }

    public void addAll(List<Shipping> list) {
        Preconditions.checkNotNull(list, "Lista nie jest zainicjalizowana");
        list.forEach(line -> {
            try {
                this.add(line);
            } catch (IllegalArgumentException ignored) {}
        });
    }

    public Shipping get(int index) {
        Preconditions.checkElementIndex(index, listOfShipping.size());
        return listOfShipping.get(index);
    }

    public Shipping getShippingByNumber(String shNumber) {
        Preconditions.checkNotNull(shNumber);
        List<Shipping> result = listOfShipping.stream().filter(line -> line.getShippingNumber().equals(shNumber)).collect(Collectors.toList());
        return result.size() == 1 ? result.get(0) : null;
    }

    public List<Shipping> getAll() {
        return new ArrayList<>(listOfShipping);
    }

    public void remove(int index) {
        Preconditions.checkElementIndex(index, listOfShipping.size());
        listOfShipping.remove(index);
    }

    public void remove(Shipping shipping) {
        Preconditions.checkNotNull(shipping);
        listOfShipping.remove(shipping);
    }

    public void remove(String shNumber) {
        Shipping shipping = getShippingByNumber(shNumber);
        if(shipping != null) listOfShipping.remove(shipping);
    }

    public void clear() {
        listOfShipping.clear();
    }

    public int size() {
        return listOfShipping.size();
    }

    private boolean isShippingNumberAtList(String shNumber) {
        return listOfShipping.stream().anyMatch(line -> line.getShippingNumber().equals(shNumber));
    }

}
