// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/history/actions/record_action.proto
package com.rhlabs.protobufs.services.history.actions.record_action;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class RecordActionResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  public RecordActionResponseV1(Integer version) {
    this.version = version;
  }

  private RecordActionResponseV1(Builder builder) {
    this(builder.version);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RecordActionResponseV1)) return false;
    return equals(version, ((RecordActionResponseV1) other).version);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = version != null ? version.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<RecordActionResponseV1> {

    public Integer version;

    public Builder() {
    }

    public Builder(RecordActionResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    @Override
    public RecordActionResponseV1 build() {
      return new RecordActionResponseV1(this);
    }
  }
}
