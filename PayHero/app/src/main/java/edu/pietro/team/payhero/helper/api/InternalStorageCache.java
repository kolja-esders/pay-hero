package edu.pietro.team.payhero.helper.api;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.pietro.team.payhero.helper.AbstractCache;

public class InternalStorageCache<T extends Serializable> extends AbstractCache<T> {

    private Context mContext;

    public InternalStorageCache(Context context) {
        super();
        mContext = context;
    }

    public InternalStorageCache(Context context, String prefix) {
        super(prefix);
        mContext = context;
    }

    public void put(String key, T value) throws IOException {
        FileOutputStream fos = mContext.openFileOutput(constructFullKey(key), Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(value);
        oos.close();
        fos.close();
    }

    public T get(String key) throws IOException {
        FileInputStream fis = mContext.openFileInput(constructFullKey(key));
        ObjectInputStream ois = new ObjectInputStream(fis);
        T result = null;
        try {
            result = (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            // TODO(kolja): Create custom exceptions for cache problems and throw those
            e.printStackTrace();
        }
        return result;
    }
}
