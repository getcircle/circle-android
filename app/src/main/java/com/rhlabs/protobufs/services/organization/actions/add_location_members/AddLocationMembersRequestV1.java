// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/organization/actions/add_location_members.proto
package com.rhlabs.protobufs.services.organization.actions.add_location_members;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class AddLocationMembersRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_LOCATION_ID = "";
  public static final List<String> DEFAULT_PROFILE_IDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String location_id;

  @ProtoField(tag = 3, type = STRING, label = REPEATED)
  public final List<String> profile_ids;

  public AddLocationMembersRequestV1(Integer version, String location_id, List<String> profile_ids) {
    this.version = version;
    this.location_id = location_id;
    this.profile_ids = immutableCopyOf(profile_ids);
  }

  private AddLocationMembersRequestV1(Builder builder) {
    this(builder.version, builder.location_id, builder.profile_ids);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof AddLocationMembersRequestV1)) return false;
    AddLocationMembersRequestV1 o = (AddLocationMembersRequestV1) other;
    return equals(version, o.version)
        && equals(location_id, o.location_id)
        && equals(profile_ids, o.profile_ids);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (location_id != null ? location_id.hashCode() : 0);
      result = result * 37 + (profile_ids != null ? profile_ids.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<AddLocationMembersRequestV1> {

    public Integer version;
    public String location_id;
    public List<String> profile_ids;

    public Builder() {
    }

    public Builder(AddLocationMembersRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.location_id = message.location_id;
      this.profile_ids = copyOf(message.profile_ids);
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder location_id(String location_id) {
      this.location_id = location_id;
      return this;
    }

    public Builder profile_ids(List<String> profile_ids) {
      this.profile_ids = checkForNulls(profile_ids);
      return this;
    }

    @Override
    public AddLocationMembersRequestV1 build() {
      return new AddLocationMembersRequestV1(this);
    }
  }
}
