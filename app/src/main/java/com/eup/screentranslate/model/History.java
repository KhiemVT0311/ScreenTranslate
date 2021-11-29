package com.eup.screentranslate.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey(autoGenerate = true) public int uid;
    @ColumnInfo(name = "path") public String path;
    @ColumnInfo(name = "date") public Long date;
    @ColumnInfo(name = "text") public String text;
    @ColumnInfo(name = "translation") public String translation;
    @ColumnInfo(name = "type") public int type;
    @ColumnInfo(name = "transFrom") public String transFrom;
    @ColumnInfo(name = "transTo") public String transTo;

    public History(int uid, String path, Long date, String text, String translation, int type, String transFrom, String transTo) {
        this.uid = uid;
        this.path = path;
        this.date = date;
        this.text = text;
        this.translation = translation;
        this.type = type;
        this.transFrom = transFrom;
        this.transTo = transTo;
    }

    public static int TEXT = 0;
    public static int OBJECT = 1;
    public static int TRANS = 2;
}