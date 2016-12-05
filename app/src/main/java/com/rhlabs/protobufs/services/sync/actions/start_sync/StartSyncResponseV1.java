// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/sync/actions/start_sync.proto
package com.rhlabs.protobufs.services.sync.actions.start_sync;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class StartSyncResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_SYNC_ID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String sync_id;

  public StartSyncResponseV1(Integer version, String sync_id) {
    this.version = version;
    this.sync_id = sync_id;
  }

  private StartSyncResponseV1(Builder builder) {
    this(builder.version, builder.sync_id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof StartSyncResponseV1)) return false;
    StartSyncResponseV1 o = (StartSyncResponseV1) other;
    return equals(version, o.version)
        && equals(sync_id, o.sync_id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (sync_id != null ? sync_id.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<StartSyncResponseV1> {

    public Integer version;
    public String sync_id;

    public Builder() {
    }

    public Builder(StartSyncResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.sync_id = message.sync_id;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder sync_id(String sync_id) {
      this.sync_id = sync_id;
      return this;
    }

    @Override
    public StartSyncResponseV1 build() {
      return new StartSyncResponseV1(this);
    }
  }
}
