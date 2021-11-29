package com.eup.screentranslate.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextAnnotations {

    @SerializedName("locale")
    @Expose
    public String locale;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("boundingPoly")
    @Expose
    public BoundingPoly boundingPoly;
}
