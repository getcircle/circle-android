// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/media/actions/start_image_upload.proto
package com.rhlabs.protobufs.services.media.actions.start_image_upload;

import com.rhlabs.protobufs.services.media.containers.UploadInstructionsV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class StartImageUploadResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2)
  public final UploadInstructionsV1 upload_instructions;

  public StartImageUploadResponseV1(Integer version, UploadInstructionsV1 upload_instructions) {
    this.version = version;
    this.upload_instructions = upload_instructions;
  }

  private StartImageUploadResponseV1(Builder builder) {
    this(builder.version, builder.upload_instructions);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof StartImageUploadResponseV1)) return false;
    StartImageUploadResponseV1 o = (StartImageUploadResponseV1) other;
    return equals(version, o.version)
        && equals(upload_instructions, o.upload_instructions);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (upload_instructions != null ? upload_instructions.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<StartImageUploadResponseV1> {

    public Integer version;
    public UploadInstructionsV1 upload_instructions;

    public Builder() {
    }

    public Builder(StartImageUploadResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.upload_instructions = message.upload_instructions;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder upload_instructions(UploadInstructionsV1 upload_instructions) {
      this.upload_instructions = upload_instructions;
      return this;
    }

    @Override
    public StartImageUploadResponseV1 build() {
      return new StartImageUploadResponseV1(this);
    }
  }
}
