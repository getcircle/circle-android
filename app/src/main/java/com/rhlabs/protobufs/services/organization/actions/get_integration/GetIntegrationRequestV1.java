// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/actions/get_integration.proto
package com.rhlabs.protobufs.services.organization.actions.get_integration;

import com.rhlabs.protobufs.services.organization.containers.integration.IntegrationTypeV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class GetIntegrationRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final IntegrationTypeV1 DEFAULT_INTEGRATION_TYPE = IntegrationTypeV1.GOOGLE_GROUPS;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = ENUM)
  public final IntegrationTypeV1 integration_type;

  public GetIntegrationRequestV1(Integer version, IntegrationTypeV1 integration_type) {
    this.version = version;
    this.integration_type = integration_type;
  }

  private GetIntegrationRequestV1(Builder builder) {
    this(builder.version, builder.integration_type);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetIntegrationRequestV1)) return false;
    GetIntegrationRequestV1 o = (GetIntegrationRequestV1) other;
    return equals(version, o.version)
        && equals(integration_type, o.integration_type);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (integration_type != null ? integration_type.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetIntegrationRequestV1> {

    public Integer version;
    public IntegrationTypeV1 integration_type;

    public Builder() {
    }

    public Builder(GetIntegrationRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.integration_type = message.integration_type;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder integration_type(IntegrationTypeV1 integration_type) {
      this.integration_type = integration_type;
      return this;
    }

    @Override
    public GetIntegrationRequestV1 build() {
      return new GetIntegrationRequestV1(this);
    }
  }
}
