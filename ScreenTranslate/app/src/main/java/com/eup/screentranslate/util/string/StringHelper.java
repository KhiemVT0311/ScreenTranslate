package com.eup.screentranslate.util.string;

import java.util.List;

public class StringHelper {
    public static String appendAllList(List<String> list, char separator, char endChar){
        if (list == null || list.size() == 0) return null;

        StringBuilder builder = new StringBuilder();
        int len = list.size();
        for (int i = 0; i < len; i++){
            if (i == len - 1){
                builder.append(list.get(i) + endChar);
            } else {
                builder.append(list.get(i) + separator + " ");
            }
        }
        return builder.toString();
    }
}
