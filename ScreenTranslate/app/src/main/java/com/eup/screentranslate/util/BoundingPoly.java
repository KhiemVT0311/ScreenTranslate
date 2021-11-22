package com.eup.screentranslate.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoundingPoly {
    @SerializedName("vertices")
    @Expose
    public List<Vertex> vertices;

    private class Vertex{
        private int x =0;
        private int y=0;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
