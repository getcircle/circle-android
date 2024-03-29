// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/get_authentication_instructions.proto
package com.rhlabs.protobufs.services.user.actions.get_authentication_instructions;

import com.rhlabs.protobufs.services.user.actions.authenticate_user.AuthenticateUserRequestV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class GetAuthenticationInstructionsResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final AuthenticateUserRequestV1.AuthBackendV1 DEFAULT_BACKEND = AuthenticateUserRequestV1.AuthBackendV1.INTERNAL;
  public static final Boolean DEFAULT_USER_EXISTS = false;
  public static final String DEFAULT_AUTHORIZATION_URL = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = ENUM)
  public final AuthenticateUserRequestV1.AuthBackendV1 backend;

  @ProtoField(tag = 3, type = BOOL)
  public final Boolean user_exists;

  @ProtoField(tag = 4, type = STRING)
  public final String authorization_url;

  public GetAuthenticationInstructionsResponseV1(Integer version, AuthenticateUserRequestV1.AuthBackendV1 backend, Boolean user_exists, String authorization_url) {
    this.version = version;
    this.backend = backend;
    this.user_exists = user_exists;
    this.authorization_url = authorization_url;
  }

  private GetAuthenticationInstructionsResponseV1(Builder builder) {
    this(builder.version, builder.backend, builder.user_exists, builder.authorization_url);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetAuthenticationInstructionsResponseV1)) return false;
    GetAuthenticationInstructionsResponseV1 o = (GetAuthenticationInstructionsResponseV1) other;
    return equals(version, o.version)
        && equals(backend, o.backend)
        && equals(user_exists, o.user_exists)
        && equals(authorization_url, o.authorization_url);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (backend != null ? backend.hashCode() : 0);
      result = result * 37 + (user_exists != null ? user_exists.hashCode() : 0);
      result = result * 37 + (authorization_url != null ? authorization_url.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetAuthenticationInstructionsResponseV1> {

    public Integer version;
    public AuthenticateUserRequestV1.AuthBackendV1 backend;
    public Boolean user_exists;
    public String authorization_url;

    public Builder() {
    }

    public Builder(GetAuthenticationInstructionsResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.backend = message.backend;
      this.user_exists = message.user_exists;
      this.authorization_url = message.authorization_url;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder backend(AuthenticateUserRequestV1.AuthBackendV1 backend) {
      this.backend = backend;
      return this;
    }

    public Builder user_exists(Boolean user_exists) {
      this.user_exists = user_exists;
      return this;
    }

    public Builder authorization_url(String authorization_url) {
      this.authorization_url = authorization_url;
      return this;
    }

    @Override
    public GetAuthenticationInstructionsResponseV1 build() {
      return new GetAuthenticationInstructionsResponseV1(this);
    }
  }
}
