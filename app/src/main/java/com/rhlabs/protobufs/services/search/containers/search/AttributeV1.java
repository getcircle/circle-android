// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/search/containers/search.proto
package com.rhlabs.protobufs.services.search.containers.search;

import com.squareup.wire.ProtoEnum;

public enum AttributeV1
    implements ProtoEnum {
  LOCATION_ID(0),
  TEAM_ID(1);

  private final int value;

  AttributeV1(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }
}