package com.t28.android.example.volley;

import com.android.volley.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapベースのオンメモリキャッシュ
 */
public class MapCache implements Cache {
    private static final long NO_TIME_TO_LIVE = 0;

    private final Map<String, Entry> mEntries;

    /**
     * コンストラクタ
     */
    public MapCache() {
        mEntries = new HashMap<>();
    }

    @Override
    public Entry get(String key) {
        return mEntries.get(key);
    }

    @Override
    public void put(String key, Entry entry) {
        mEntries.put(key, entry);
    }

    @Override
    public void initialize() {
        mEntries.clear();
    }

    @Override
    public void invalidate(String key, boolean fullExpire) {
        final Entry entry = mEntries.get(key);
        if (entry == null) {
            return;
        }

        entry.softTtl = NO_TIME_TO_LIVE;
        if (fullExpire) {
            entry.ttl = NO_TIME_TO_LIVE;
        }
        // 参照を直接操作しているためputしない
    }

    @Override
    public void remove(String key) {
        mEntries.remove(key);
    }

    @Override
    public void clear() {
        mEntries.clear();
    }
}
