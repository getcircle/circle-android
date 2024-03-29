// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/record_device.proto
package com.rhlabs.protobufs.services.user.actions.record_device;

import com.rhlabs.protobufs.services.user.containers.DeviceV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class RecordDeviceResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2)
  public final DeviceV1 device;

  public RecordDeviceResponseV1(Integer version, DeviceV1 device) {
    this.version = version;
    this.device = device;
  }

  private RecordDeviceResponseV1(Builder builder) {
    this(builder.version, builder.device);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RecordDeviceResponseV1)) return false;
    RecordDeviceResponseV1 o = (RecordDeviceResponseV1) other;
    return equals(version, o.version)
        && equals(device, o.device);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (device != null ? device.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RecordDeviceResponseV1> {

    public Integer version;
    public DeviceV1 device;

    public Builder() {
    }

    public Builder(RecordDeviceResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.device = message.device;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder device(DeviceV1 device) {
      this.device = device;
      return this;
    }

    @Override
    public RecordDeviceResponseV1 build() {
      return new RecordDeviceResponseV1(this);
    }
  }
}
