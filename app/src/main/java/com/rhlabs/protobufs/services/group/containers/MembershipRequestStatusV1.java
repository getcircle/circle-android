// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/group/containers.proto
package com.rhlabs.protobufs.services.group.containers;

import com.squareup.wire.ProtoEnum;

public enum MembershipRequestStatusV1
    implements ProtoEnum {
  PENDING(0),
  APPROVED(1),
  DENIED(2);

  private final int value;

  MembershipRequestStatusV1(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }
}
