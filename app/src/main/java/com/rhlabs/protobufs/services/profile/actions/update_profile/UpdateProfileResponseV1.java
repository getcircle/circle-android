// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/profile/actions/update_profile.proto
package com.rhlabs.protobufs.services.profile.actions.update_profile;

import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class UpdateProfileResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2)
  public final ProfileV1 profile;

  public UpdateProfileResponseV1(Integer version, ProfileV1 profile) {
    this.version = version;
    this.profile = profile;
  }

  private UpdateProfileResponseV1(Builder builder) {
    this(builder.version, builder.profile);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UpdateProfileResponseV1)) return false;
    UpdateProfileResponseV1 o = (UpdateProfileResponseV1) other;
    return equals(version, o.version)
        && equals(profile, o.profile);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (profile != null ? profile.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UpdateProfileResponseV1> {

    public Integer version;
    public ProfileV1 profile;

    public Builder() {
    }

    public Builder(UpdateProfileResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.profile = message.profile;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder profile(ProfileV1 profile) {
      this.profile = profile;
      return this;
    }

    @Override
    public UpdateProfileResponseV1 build() {
      return new UpdateProfileResponseV1(this);
    }
  }
}
