// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/registry/responses.proto
package com.rhlabs.protobufs.services.registry.responses;

import com.squareup.wire.Message;

public final class Media extends Message {
  private static final long serialVersionUID = 0L;

  public Media() {
  }

  private Media(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Media;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<Media> {

    public Builder() {
    }

    public Builder(Media message) {
      super(message);
    }

    @Override
    public Media build() {
      return new Media(this);
    }
  }
}
