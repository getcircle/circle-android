// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/logout.proto
package com.rhlabs.protobufs.services.user.actions.logout;

import com.rhlabs.protobufs.services.user.containers.token.ClientTypeV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class LogoutRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final ClientTypeV1 DEFAULT_CLIENT_TYPE = ClientTypeV1.IOS;
  public static final Boolean DEFAULT_REVOKE_ALL = false;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = ENUM)
  public final ClientTypeV1 client_type;

  @ProtoField(tag = 3, type = BOOL)
  public final Boolean revoke_all;

  public LogoutRequestV1(Integer version, ClientTypeV1 client_type, Boolean revoke_all) {
    this.version = version;
    this.client_type = client_type;
    this.revoke_all = revoke_all;
  }

  private LogoutRequestV1(Builder builder) {
    this(builder.version, builder.client_type, builder.revoke_all);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LogoutRequestV1)) return false;
    LogoutRequestV1 o = (LogoutRequestV1) other;
    return equals(version, o.version)
        && equals(client_type, o.client_type)
        && equals(revoke_all, o.revoke_all);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (client_type != null ? client_type.hashCode() : 0);
      result = result * 37 + (revoke_all != null ? revoke_all.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<LogoutRequestV1> {

    public Integer version;
    public ClientTypeV1 client_type;
    public Boolean revoke_all;

    public Builder() {
    }

    public Builder(LogoutRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.client_type = message.client_type;
      this.revoke_all = message.revoke_all;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder client_type(ClientTypeV1 client_type) {
      this.client_type = client_type;
      return this;
    }

    public Builder revoke_all(Boolean revoke_all) {
      this.revoke_all = revoke_all;
      return this;
    }

    @Override
    public LogoutRequestV1 build() {
      return new LogoutRequestV1(this);
    }
  }
}
