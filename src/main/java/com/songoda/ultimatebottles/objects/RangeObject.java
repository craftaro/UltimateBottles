package com.songoda.ultimatebottles.objects;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class RangeObject implements Get {
    private final XPObject upper;
    private final XPObject lower;

    private RangeObject(XPObject upper, XPObject lower) {
        this.upper = upper;
        this.lower = lower;
    }

    public static Optional<RangeObject> of(String value) {
        if(value.split("-").length != 2) {
            return Optional.empty();
        }

        Optional<XPObject> lower = XPObject.of(value.split("-")[0]);
        Optional<XPObject> upper = XPObject.of(value.split("-")[1]);

        if(!lower.isPresent() || !upper.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new RangeObject(upper.get(), lower.get()));
    }

    @Override
    public int get() {
        return ThreadLocalRandom.current().nextInt(lower.get(), upper.get() + 1);
    }

    public XPObject getUpper() {
        return upper;
    }

    public XPObject getLower() {
        return lower;
    }
}
