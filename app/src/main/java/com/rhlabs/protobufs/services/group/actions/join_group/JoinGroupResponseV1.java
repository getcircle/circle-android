// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/group/actions/join_group.proto
package com.rhlabs.protobufs.services.group.actions.join_group;

import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class JoinGroupResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2)
  public final MembershipRequestV1 request;

  public JoinGroupResponseV1(Integer version, MembershipRequestV1 request) {
    this.version = version;
    this.request = request;
  }

  private JoinGroupResponseV1(Builder builder) {
    this(builder.version, builder.request);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof JoinGroupResponseV1)) return false;
    JoinGroupResponseV1 o = (JoinGroupResponseV1) other;
    return equals(version, o.version)
        && equals(request, o.request);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (request != null ? request.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<JoinGroupResponseV1> {

    public Integer version;
    public MembershipRequestV1 request;

    public Builder() {
    }

    public Builder(JoinGroupResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.request = message.request;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder request(MembershipRequestV1 request) {
      this.request = request;
      return this;
    }

    @Override
    public JoinGroupResponseV1 build() {
      return new JoinGroupResponseV1(this);
    }
  }
}
