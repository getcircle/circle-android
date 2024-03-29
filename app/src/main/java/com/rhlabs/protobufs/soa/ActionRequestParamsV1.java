// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/service_protobufs/soa.proto
package com.rhlabs.protobufs.soa;

import com.squareup.wire.ExtendableMessage;
import com.squareup.wire.Extension;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class ActionRequestParamsV1 extends ExtendableMessage<ActionRequestParamsV1> {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  public ActionRequestParamsV1(Integer version) {
    this.version = version;
  }

  private ActionRequestParamsV1(Builder builder) {
    this(builder.version);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ActionRequestParamsV1)) return false;
    ActionRequestParamsV1 o = (ActionRequestParamsV1) other;
    if (!extensionsEqual(o)) return false;
    return equals(version, o.version);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = extensionsHashCode();
      result = result * 37 + (version != null ? version.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends ExtendableBuilder<ActionRequestParamsV1> {

    public Integer version;

    public Builder() {
    }

    public Builder(ActionRequestParamsV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    @Override
    public <E> Builder setExtension(Extension<ActionRequestParamsV1, E> extension, E value) {
      super.setExtension(extension, value);
      return this;
    }

    @Override
    public ActionRequestParamsV1 build() {
      return new ActionRequestParamsV1(this);
    }
  }
}
