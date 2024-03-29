// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/media/containers.proto
package com.rhlabs.protobufs.services.media.containers;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class UploadInstructionsV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_UPLOAD_ID = "";
  public static final String DEFAULT_UPLOAD_URL = "";
  public static final String DEFAULT_UPLOAD_KEY = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String upload_id;

  @ProtoField(tag = 3, type = STRING)
  public final String upload_url;

  @ProtoField(tag = 4, type = STRING)
  public final String upload_key;

  public UploadInstructionsV1(Integer version, String upload_id, String upload_url, String upload_key) {
    this.version = version;
    this.upload_id = upload_id;
    this.upload_url = upload_url;
    this.upload_key = upload_key;
  }

  private UploadInstructionsV1(Builder builder) {
    this(builder.version, builder.upload_id, builder.upload_url, builder.upload_key);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UploadInstructionsV1)) return false;
    UploadInstructionsV1 o = (UploadInstructionsV1) other;
    return equals(version, o.version)
        && equals(upload_id, o.upload_id)
        && equals(upload_url, o.upload_url)
        && equals(upload_key, o.upload_key);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (upload_id != null ? upload_id.hashCode() : 0);
      result = result * 37 + (upload_url != null ? upload_url.hashCode() : 0);
      result = result * 37 + (upload_key != null ? upload_key.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UploadInstructionsV1> {

    public Integer version;
    public String upload_id;
    public String upload_url;
    public String upload_key;

    public Builder() {
    }

    public Builder(UploadInstructionsV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.upload_id = message.upload_id;
      this.upload_url = message.upload_url;
      this.upload_key = message.upload_key;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder upload_id(String upload_id) {
      this.upload_id = upload_id;
      return this;
    }

    public Builder upload_url(String upload_url) {
      this.upload_url = upload_url;
      return this;
    }

    public Builder upload_key(String upload_key) {
      this.upload_key = upload_key;
      return this;
    }

    @Override
    public UploadInstructionsV1 build() {
      return new UploadInstructionsV1(this);
    }
  }
}
