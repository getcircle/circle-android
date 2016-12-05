// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/get_identities.proto
package com.rhlabs.protobufs.services.user.actions.get_identities;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class GetIdentitiesRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_USER_ID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String user_id;

  public GetIdentitiesRequestV1(Integer version, String user_id) {
    this.version = version;
    this.user_id = user_id;
  }

  private GetIdentitiesRequestV1(Builder builder) {
    this(builder.version, builder.user_id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetIdentitiesRequestV1)) return false;
    GetIdentitiesRequestV1 o = (GetIdentitiesRequestV1) other;
    return equals(version, o.version)
        && equals(user_id, o.user_id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (user_id != null ? user_id.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetIdentitiesRequestV1> {

    public Integer version;
    public String user_id;

    public Builder() {
    }

    public Builder(GetIdentitiesRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.user_id = message.user_id;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder user_id(String user_id) {
      this.user_id = user_id;
      return this;
    }

    @Override
    public GetIdentitiesRequestV1 build() {
      return new GetIdentitiesRequestV1(this);
    }
  }
}