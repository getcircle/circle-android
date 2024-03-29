// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/profile/actions/get_extended_profile.proto
package com.rhlabs.protobufs.services.profile.actions.get_extended_profile;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class GetExtendedProfileRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_PROFILE_ID = "";
  public static final String DEFAULT_USER_ID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String profile_id;

  @ProtoField(tag = 3, type = STRING)
  public final String user_id;

  public GetExtendedProfileRequestV1(Integer version, String profile_id, String user_id) {
    this.version = version;
    this.profile_id = profile_id;
    this.user_id = user_id;
  }

  private GetExtendedProfileRequestV1(Builder builder) {
    this(builder.version, builder.profile_id, builder.user_id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetExtendedProfileRequestV1)) return false;
    GetExtendedProfileRequestV1 o = (GetExtendedProfileRequestV1) other;
    return equals(version, o.version)
        && equals(profile_id, o.profile_id)
        && equals(user_id, o.user_id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (profile_id != null ? profile_id.hashCode() : 0);
      result = result * 37 + (user_id != null ? user_id.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetExtendedProfileRequestV1> {

    public Integer version;
    public String profile_id;
    public String user_id;

    public Builder() {
    }

    public Builder(GetExtendedProfileRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.profile_id = message.profile_id;
      this.user_id = message.user_id;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder profile_id(String profile_id) {
      this.profile_id = profile_id;
      return this;
    }

    public Builder user_id(String user_id) {
      this.user_id = user_id;
      return this;
    }

    @Override
    public GetExtendedProfileRequestV1 build() {
      return new GetExtendedProfileRequestV1(this);
    }
  }
}
