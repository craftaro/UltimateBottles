package com.songoda.ultimatebottles.objects;

import java.util.Optional;

public class AmountObject {
    private final Get getter;

    private AmountObject(Get getter) {
        this.getter = getter;
    }

    public static Optional<AmountObject> of(String value) {
        value = value.toLowerCase();
        Optional<RangeObject> rangeObject = RangeObject.of(value);
        Optional<XPObject> xpObject = XPObject.of(value);

        if (!rangeObject.isPresent() && !xpObject.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(rangeObject.map(AmountObject::new).orElseGet(() -> new AmountObject(xpObject.get())));
    }

    public int getValue() {
        return getter.get();
    }

    public Get getGetter() {
        return getter;
    }
}
