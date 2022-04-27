package com.visenze.common.util;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.COLON;

/**
 * Created by Hung on 28/1/21.
 */
public abstract class MultimapUtil {

    public static void putIfPresent(Multimap<String, String> multimap, Optional opt, String key) {
        if (opt.isPresent())
            multimap.put(key, String.valueOf(opt.get()));
    }

    public static void putList(Multimap<String, String> multimap, Optional<List<String>> list, String key) {
        if (list.isPresent()) {
            for (String val : list.get())
                multimap.put(key, val);
        }
    }

    public static void putMap(Multimap<String, String> multimap, Optional<Map<String, String>> customParams) {
        if (customParams.isPresent()) {
            for (Map.Entry<String, String> entry : customParams.get().entrySet()) {
                multimap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void setValueMap(Multimap<String, String> mapToUpdate,
                                 Optional<Map<String,String>> data,
                                 String key) {
        if (data.isPresent()) {
            Map<String,String> map = data.get();
            for (Map.Entry<String, String> entry : map.entrySet())
                mapToUpdate.put(key, entry.getKey() + COLON + entry.getValue());
        }
    }

}
