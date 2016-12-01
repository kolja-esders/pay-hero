package edu.pietro.team.payhero.helper;


import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;

public abstract class AbstractCache<T extends Serializable> {

    private String mPrefix;

    public AbstractCache() {
        mPrefix = "";
    }

    public AbstractCache(String prefix) {
        if (null == prefix) {
            throw new NullPointerException("prefix == null");
        }
        mPrefix = prefix;
    }

    // Later on...
    // void put(String key, T value, Calendar expirationDate);

    public abstract void put(String key, T value) throws IOException;

    public abstract T get(String key) throws IOException;

    protected String constructFullKey(String key) {
        return mPrefix.isEmpty() ? key : StringUtils.join(mPrefix, "\\", key);
    }
}
