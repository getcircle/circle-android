// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/containers.proto
package com.rhlabs.protobufs.services.organization.containers;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class ColorV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 0;
  public static final Integer DEFAULT_RED = 0;
  public static final Integer DEFAULT_GREEN = 0;
  public static final Integer DEFAULT_BLUE = 0;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = UINT32)
  public final Integer red;

  @ProtoField(tag = 3, type = UINT32)
  public final Integer green;

  @ProtoField(tag = 4, type = UINT32)
  public final Integer blue;

  public ColorV1(Integer version, Integer red, Integer green, Integer blue) {
    this.version = version;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  private ColorV1(Builder builder) {
    this(builder.version, builder.red, builder.green, builder.blue);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ColorV1)) return false;
    ColorV1 o = (ColorV1) other;
    return equals(version, o.version)
        && equals(red, o.red)
        && equals(green, o.green)
        && equals(blue, o.blue);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (red != null ? red.hashCode() : 0);
      result = result * 37 + (green != null ? green.hashCode() : 0);
      result = result * 37 + (blue != null ? blue.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ColorV1> {

    public Integer version;
    public Integer red;
    public Integer green;
    public Integer blue;

    public Builder() {
    }

    public Builder(ColorV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.red = message.red;
      this.green = message.green;
      this.blue = message.blue;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder red(Integer red) {
      this.red = red;
      return this;
    }

    public Builder green(Integer green) {
      this.green = green;
      return this;
    }

    public Builder blue(Integer blue) {
      this.blue = blue;
      return this;
    }

    @Override
    public ColorV1 build() {
      return new ColorV1(this);
    }
  }
}
