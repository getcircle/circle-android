// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/send_verification_code.proto
package com.rhlabs.protobufs.services.user.actions.send_verification_code;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class SendVerificationCodeResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_MESSAGE_ID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String message_id;

  public SendVerificationCodeResponseV1(Integer version, String message_id) {
    this.version = version;
    this.message_id = message_id;
  }

  private SendVerificationCodeResponseV1(Builder builder) {
    this(builder.version, builder.message_id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SendVerificationCodeResponseV1)) return false;
    SendVerificationCodeResponseV1 o = (SendVerificationCodeResponseV1) other;
    return equals(version, o.version)
        && equals(message_id, o.message_id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (message_id != null ? message_id.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<SendVerificationCodeResponseV1> {

    public Integer version;
    public String message_id;

    public Builder() {
    }

    public Builder(SendVerificationCodeResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.message_id = message.message_id;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder message_id(String message_id) {
      this.message_id = message_id;
      return this;
    }

    @Override
    public SendVerificationCodeResponseV1 build() {
      return new SendVerificationCodeResponseV1(this);
    }
  }
}
