// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/containers/integration.proto
package com.rhlabs.protobufs.services.organization.containers.integration;

import com.squareup.wire.ProtoEnum;

public enum IntegrationTypeV1
    implements ProtoEnum {
  GOOGLE_GROUPS(0);

  private final int value;

  IntegrationTypeV1(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }
}
