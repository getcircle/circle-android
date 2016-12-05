// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/actions/get_team_descendants.proto
package com.rhlabs.protobufs.services.organization.actions.get_team_descendants;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GetTeamDescendantsRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final List<String> DEFAULT_TEAM_IDS = Collections.emptyList();
  public static final List<String> DEFAULT_ATTRIBUTES = Collections.emptyList();
  public static final Integer DEFAULT_DEPTH = 0;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING, label = REPEATED)
  public final List<String> team_ids;

  @ProtoField(tag = 3, type = STRING, label = REPEATED)
  public final List<String> attributes;

  @ProtoField(tag = 4, type = UINT32)
  public final Integer depth;

  public GetTeamDescendantsRequestV1(Integer version, List<String> team_ids, List<String> attributes, Integer depth) {
    this.version = version;
    this.team_ids = immutableCopyOf(team_ids);
    this.attributes = immutableCopyOf(attributes);
    this.depth = depth;
  }

  private GetTeamDescendantsRequestV1(Builder builder) {
    this(builder.version, builder.team_ids, builder.attributes, builder.depth);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetTeamDescendantsRequestV1)) return false;
    GetTeamDescendantsRequestV1 o = (GetTeamDescendantsRequestV1) other;
    return equals(version, o.version)
        && equals(team_ids, o.team_ids)
        && equals(attributes, o.attributes)
        && equals(depth, o.depth);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (team_ids != null ? team_ids.hashCode() : 1);
      result = result * 37 + (attributes != null ? attributes.hashCode() : 1);
      result = result * 37 + (depth != null ? depth.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetTeamDescendantsRequestV1> {

    public Integer version;
    public List<String> team_ids;
    public List<String> attributes;
    public Integer depth;

    public Builder() {
    }

    public Builder(GetTeamDescendantsRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.team_ids = copyOf(message.team_ids);
      this.attributes = copyOf(message.attributes);
      this.depth = message.depth;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder team_ids(List<String> team_ids) {
      this.team_ids = checkForNulls(team_ids);
      return this;
    }

    public Builder attributes(List<String> attributes) {
      this.attributes = checkForNulls(attributes);
      return this;
    }

    public Builder depth(Integer depth) {
      this.depth = depth;
      return this;
    }

    @Override
    public GetTeamDescendantsRequestV1 build() {
      return new GetTeamDescendantsRequestV1(this);
    }
  }
}