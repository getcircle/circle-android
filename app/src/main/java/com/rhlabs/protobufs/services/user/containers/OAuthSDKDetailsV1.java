// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/containers.proto
package com.rhlabs.protobufs.services.user.containers;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class OAuthSDKDetailsV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_CODE = "";
  public static final String DEFAULT_ID_TOKEN = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String code;

  @ProtoField(tag = 3, type = STRING)
  public final String id_token;

  public OAuthSDKDetailsV1(Integer version, String code, String id_token) {
    this.version = version;
    this.code = code;
    this.id_token = id_token;
  }

  private OAuthSDKDetailsV1(Builder builder) {
    this(builder.version, builder.code, builder.id_token);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof OAuthSDKDetailsV1)) return false;
    OAuthSDKDetailsV1 o = (OAuthSDKDetailsV1) other;
    return equals(version, o.version)
        && equals(code, o.code)
        && equals(id_token, o.id_token);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (code != null ? code.hashCode() : 0);
      result = result * 37 + (id_token != null ? id_token.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<OAuthSDKDetailsV1> {

    public Integer version;
    public String code;
    public String id_token;

    public Builder() {
    }

    public Builder(OAuthSDKDetailsV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.code = message.code;
      this.id_token = message.id_token;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder id_token(String id_token) {
      this.id_token = id_token;
      return this;
    }

    @Override
    public OAuthSDKDetailsV1 build() {
      return new OAuthSDKDetailsV1(this);
    }
  }
}
