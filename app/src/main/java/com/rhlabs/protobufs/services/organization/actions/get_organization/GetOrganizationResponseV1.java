// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/actions/get_organization.proto
package com.rhlabs.protobufs.services.organization.actions.get_organization;

import com.rhlabs.protobufs.services.organization.containers.OrganizationV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class GetOrganizationResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2)
  public final OrganizationV1 organization;

  public GetOrganizationResponseV1(Integer version, OrganizationV1 organization) {
    this.version = version;
    this.organization = organization;
  }

  private GetOrganizationResponseV1(Builder builder) {
    this(builder.version, builder.organization);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetOrganizationResponseV1)) return false;
    GetOrganizationResponseV1 o = (GetOrganizationResponseV1) other;
    return equals(version, o.version)
        && equals(organization, o.organization);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (organization != null ? organization.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetOrganizationResponseV1> {

    public Integer version;
    public OrganizationV1 organization;

    public Builder() {
    }

    public Builder(GetOrganizationResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.organization = message.organization;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder organization(OrganizationV1 organization) {
      this.organization = organization;
      return this;
    }

    @Override
    public GetOrganizationResponseV1 build() {
      return new GetOrganizationResponseV1(this);
    }
  }
}
