package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.group.containers.GroupV1;

/**
 * Created by anju on 6/30/15.
 */
public class GroupActionEvent {

    public enum Type {
        Leave,
        Join,
        RequestToJoin
    }

    private Type mType;
    private GroupV1 mGroup;

    public GroupActionEvent(Type type, GroupV1 group) {
        mType = type;
        mGroup = group;
    }

    public Type getType() {
        return mType;
    }

    public GroupV1 getGroup() {
        return mGroup;
    }
}
