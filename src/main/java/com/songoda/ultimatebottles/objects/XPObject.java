package com.songoda.ultimatebottles.objects;

import com.songoda.ultimatebottles.utils.Experience;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;

import static java.lang.Integer.parseInt;

public class XPObject implements Get {
    private final String value;

    private XPObject(String value) {
        this.value = value;
    }

    public static Optional<XPObject> of(String value) {
        value = value.toLowerCase();

        String toTest = value.endsWith("l") ? value.substring(0, value.length() - 1) : value;

        if (!StringUtils.isNumeric(toTest)) {
            return Optional.empty();
        }

        try {
            Integer.parseInt(toTest);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return Optional.of(new XPObject(value));
    }

    @Override
    public int get() {
        if (value.endsWith("l")) {
            return Experience.getExpToLevel(parseInt(value.substring(0, value.length() - 1)));
        }

        return Integer.parseInt(value);
    }
}
