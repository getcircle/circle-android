// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/get_user.proto
package com.rhlabs.protobufs.services.user.actions.get_user;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class GetUserRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_EMAIL = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String email;

  public GetUserRequestV1(Integer version, String email) {
    this.version = version;
    this.email = email;
  }

  private GetUserRequestV1(Builder builder) {
    this(builder.version, builder.email);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetUserRequestV1)) return false;
    GetUserRequestV1 o = (GetUserRequestV1) other;
    return equals(version, o.version)
        && equals(email, o.email);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (email != null ? email.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetUserRequestV1> {

    public Integer version;
    public String email;

    public Builder() {
    }

    public Builder(GetUserRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.email = message.email;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    @Override
    public GetUserRequestV1 build() {
      return new GetUserRequestV1(this);
    }
  }
}
