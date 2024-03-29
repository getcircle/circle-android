// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/containers.proto
package com.rhlabs.protobufs.services.organization.containers;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class PathPartV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_ID = "";
  public static final String DEFAULT_NAME = "";
  public static final String DEFAULT_OWNER_ID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String id;

  @ProtoField(tag = 3, type = STRING)
  public final String name;

  @ProtoField(tag = 4, type = STRING)
  public final String owner_id;

  public PathPartV1(Integer version, String id, String name, String owner_id) {
    this.version = version;
    this.id = id;
    this.name = name;
    this.owner_id = owner_id;
  }

  private PathPartV1(Builder builder) {
    this(builder.version, builder.id, builder.name, builder.owner_id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PathPartV1)) return false;
    PathPartV1 o = (PathPartV1) other;
    return equals(version, o.version)
        && equals(id, o.id)
        && equals(name, o.name)
        && equals(owner_id, o.owner_id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (owner_id != null ? owner_id.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<PathPartV1> {

    public Integer version;
    public String id;
    public String name;
    public String owner_id;

    public Builder() {
    }

    public Builder(PathPartV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.id = message.id;
      this.name = message.name;
      this.owner_id = message.owner_id;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder owner_id(String owner_id) {
      this.owner_id = owner_id;
      return this;
    }

    @Override
    public PathPartV1 build() {
      return new PathPartV1(this);
    }
  }
}
