package com.eup.screentranslate.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.eup.screentranslate.model.BaseModel;
import com.eup.screentranslate.model.FieldInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SystemData {
    private ContentResolver resolver;

    public SystemData(Context context) {
        resolver = context.getContentResolver();
    }

    public <T extends BaseModel> ArrayList<T> getData(Class<T> clz) {
        ArrayList<T> data = new ArrayList<>();
        try {
            T t = clz.newInstance();
            Cursor c = resolver.query(t.getContentUri(), null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                T item = clz.newInstance();
                readInfo(c, item);
                data.add(item);
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return data;
        }
    }

    private <T extends BaseModel> void readInfo(Cursor c, T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field f: fields) {
            f.setAccessible(true);
            FieldInfo info = f.getAnnotation(FieldInfo.class);
            if (info == null) continue;
            try {
                setValue(c, t, f, info);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private <T extends BaseModel> void setValue(Cursor c, T t, Field f, FieldInfo info) throws IllegalAccessException {
        int index = c.getColumnIndex(info.columnName());
        String value = c.getString(index);
        switch (f.getType().getSimpleName()) {
            case "int" :
                f.setInt(t, Integer.parseInt(value));
                break;
            case "float" :
                f.setFloat(t, Float.parseFloat(value));
                break;
            default:
                f.set(t, value);
                break;
        }
    }
}
