// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/glossary/actions/delete_term.proto
package com.rhlabs.protobufs.services.glossary.actions.delete_term;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class DeleteTermRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_ID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String id;

  public DeleteTermRequestV1(Integer version, String id) {
    this.version = version;
    this.id = id;
  }

  private DeleteTermRequestV1(Builder builder) {
    this(builder.version, builder.id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DeleteTermRequestV1)) return false;
    DeleteTermRequestV1 o = (DeleteTermRequestV1) other;
    return equals(version, o.version)
        && equals(id, o.id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (id != null ? id.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<DeleteTermRequestV1> {

    public Integer version;
    public String id;

    public Builder() {
    }

    public Builder(DeleteTermRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.id = message.id;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    @Override
    public DeleteTermRequestV1 build() {
      return new DeleteTermRequestV1(this);
    }
  }
}
