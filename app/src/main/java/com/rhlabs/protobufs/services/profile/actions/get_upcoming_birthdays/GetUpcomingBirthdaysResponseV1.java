// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/profile/actions/get_upcoming_birthdays.proto
package com.rhlabs.protobufs.services.profile.actions.get_upcoming_birthdays;

import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GetUpcomingBirthdaysResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final List<ProfileV1> DEFAULT_PROFILES = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, label = REPEATED, messageType = ProfileV1.class)
  public final List<ProfileV1> profiles;

  public GetUpcomingBirthdaysResponseV1(Integer version, List<ProfileV1> profiles) {
    this.version = version;
    this.profiles = immutableCopyOf(profiles);
  }

  private GetUpcomingBirthdaysResponseV1(Builder builder) {
    this(builder.version, builder.profiles);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetUpcomingBirthdaysResponseV1)) return false;
    GetUpcomingBirthdaysResponseV1 o = (GetUpcomingBirthdaysResponseV1) other;
    return equals(version, o.version)
        && equals(profiles, o.profiles);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (profiles != null ? profiles.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetUpcomingBirthdaysResponseV1> {

    public Integer version;
    public List<ProfileV1> profiles;

    public Builder() {
    }

    public Builder(GetUpcomingBirthdaysResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.profiles = copyOf(message.profiles);
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder profiles(List<ProfileV1> profiles) {
      this.profiles = checkForNulls(profiles);
      return this;
    }

    @Override
    public GetUpcomingBirthdaysResponseV1 build() {
      return new GetUpcomingBirthdaysResponseV1(this);
    }
  }
}
