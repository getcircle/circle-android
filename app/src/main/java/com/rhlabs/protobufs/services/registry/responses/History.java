// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/registry/responses.proto
package com.rhlabs.protobufs.services.registry.responses;

import com.squareup.wire.Message;

public final class History extends Message {
  private static final long serialVersionUID = 0L;

  public History() {
  }

  private History(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof History;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<History> {

    public Builder() {
    }

    public Builder(History message) {
      super(message);
    }

    @Override
    public History build() {
      return new History(this);
    }
  }
}