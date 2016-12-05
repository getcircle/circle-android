// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/profile/actions/get_tags.proto
package com.rhlabs.protobufs.services.profile.actions.get_tags;

import com.rhlabs.protobufs.services.profile.containers.TagV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GetTagsResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final List<TagV1> DEFAULT_TAGS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, label = REPEATED, messageType = TagV1.class)
  public final List<TagV1> tags;

  public GetTagsResponseV1(Integer version, List<TagV1> tags) {
    this.version = version;
    this.tags = immutableCopyOf(tags);
  }

  private GetTagsResponseV1(Builder builder) {
    this(builder.version, builder.tags);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetTagsResponseV1)) return false;
    GetTagsResponseV1 o = (GetTagsResponseV1) other;
    return equals(version, o.version)
        && equals(tags, o.tags);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (tags != null ? tags.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetTagsResponseV1> {

    public Integer version;
    public List<TagV1> tags;

    public Builder() {
    }

    public Builder(GetTagsResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.tags = copyOf(message.tags);
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder tags(List<TagV1> tags) {
      this.tags = checkForNulls(tags);
      return this;
    }

    @Override
    public GetTagsResponseV1 build() {
      return new GetTagsResponseV1(this);
    }
  }
}