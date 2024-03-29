// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/profile/actions/get_profile_stats.proto
package com.rhlabs.protobufs.services.profile.actions.get_profile_stats;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GetProfileStatsRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final List<String> DEFAULT_ADDRESS_IDS = Collections.emptyList();
  public static final List<String> DEFAULT_LOCATION_IDS = Collections.emptyList();
  public static final List<String> DEFAULT_TEAM_IDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING, label = REPEATED)
  public final List<String> address_ids;

  @ProtoField(tag = 3, type = STRING, label = REPEATED)
  public final List<String> location_ids;

  @ProtoField(tag = 4, type = STRING, label = REPEATED)
  public final List<String> team_ids;

  public GetProfileStatsRequestV1(Integer version, List<String> address_ids, List<String> location_ids, List<String> team_ids) {
    this.version = version;
    this.address_ids = immutableCopyOf(address_ids);
    this.location_ids = immutableCopyOf(location_ids);
    this.team_ids = immutableCopyOf(team_ids);
  }

  private GetProfileStatsRequestV1(Builder builder) {
    this(builder.version, builder.address_ids, builder.location_ids, builder.team_ids);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetProfileStatsRequestV1)) return false;
    GetProfileStatsRequestV1 o = (GetProfileStatsRequestV1) other;
    return equals(version, o.version)
        && equals(address_ids, o.address_ids)
        && equals(location_ids, o.location_ids)
        && equals(team_ids, o.team_ids);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (address_ids != null ? address_ids.hashCode() : 1);
      result = result * 37 + (location_ids != null ? location_ids.hashCode() : 1);
      result = result * 37 + (team_ids != null ? team_ids.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetProfileStatsRequestV1> {

    public Integer version;
    public List<String> address_ids;
    public List<String> location_ids;
    public List<String> team_ids;

    public Builder() {
    }

    public Builder(GetProfileStatsRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.address_ids = copyOf(message.address_ids);
      this.location_ids = copyOf(message.location_ids);
      this.team_ids = copyOf(message.team_ids);
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder address_ids(List<String> address_ids) {
      this.address_ids = checkForNulls(address_ids);
      return this;
    }

    public Builder location_ids(List<String> location_ids) {
      this.location_ids = checkForNulls(location_ids);
      return this;
    }

    public Builder team_ids(List<String> team_ids) {
      this.team_ids = checkForNulls(team_ids);
      return this;
    }

    @Override
    public GetProfileStatsRequestV1 build() {
      return new GetProfileStatsRequestV1(this);
    }
  }
}
