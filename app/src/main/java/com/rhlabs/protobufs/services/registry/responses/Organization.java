// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/registry/responses.proto
package com.rhlabs.protobufs.services.registry.responses;

import com.squareup.wire.Message;

public final class Organization extends Message {
  private static final long serialVersionUID = 0L;

  public Organization() {
  }

  private Organization(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Organization;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<Organization> {

    public Builder() {
    }

    public Builder(Organization message) {
      super(message);
    }

    @Override
    public Organization build() {
      return new Organization(this);
    }
  }
}
