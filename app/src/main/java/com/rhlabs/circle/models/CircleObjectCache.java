package com.rhlabs.circle.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anju on 6/14/15.
 */
public class CircleObjectCache extends RealmObject {

    @PrimaryKey
    private String id;
    private byte[] data;

    public CircleObjectCache() {

    }

    public CircleObjectCache(String id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
