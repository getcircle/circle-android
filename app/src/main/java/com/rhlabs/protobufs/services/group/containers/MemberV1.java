// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/group/containers.proto
package com.rhlabs.protobufs.services.group.containers;

import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class MemberV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_ID = "";
  public static final RoleV1 DEFAULT_ROLE = RoleV1.OWNER;
  public static final String DEFAULT_GROUP_ID = "";
  public static final GroupProviderV1 DEFAULT_PROVIDER = GroupProviderV1.GOOGLE;
  public static final String DEFAULT_PROVIDER_UID = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String id;

  @ProtoField(tag = 3)
  public final ProfileV1 profile;

  @ProtoField(tag = 4, type = ENUM)
  public final RoleV1 role;

  @ProtoField(tag = 5, type = STRING)
  public final String group_id;

  @ProtoField(tag = 6, type = ENUM)
  public final GroupProviderV1 provider;

  @ProtoField(tag = 7, type = STRING)
  public final String provider_uid;

  public MemberV1(Integer version, String id, ProfileV1 profile, RoleV1 role, String group_id, GroupProviderV1 provider, String provider_uid) {
    this.version = version;
    this.id = id;
    this.profile = profile;
    this.role = role;
    this.group_id = group_id;
    this.provider = provider;
    this.provider_uid = provider_uid;
  }

  private MemberV1(Builder builder) {
    this(builder.version, builder.id, builder.profile, builder.role, builder.group_id, builder.provider, builder.provider_uid);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MemberV1)) return false;
    MemberV1 o = (MemberV1) other;
    return equals(version, o.version)
        && equals(id, o.id)
        && equals(profile, o.profile)
        && equals(role, o.role)
        && equals(group_id, o.group_id)
        && equals(provider, o.provider)
        && equals(provider_uid, o.provider_uid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (profile != null ? profile.hashCode() : 0);
      result = result * 37 + (role != null ? role.hashCode() : 0);
      result = result * 37 + (group_id != null ? group_id.hashCode() : 0);
      result = result * 37 + (provider != null ? provider.hashCode() : 0);
      result = result * 37 + (provider_uid != null ? provider_uid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<MemberV1> {

    public Integer version;
    public String id;
    public ProfileV1 profile;
    public RoleV1 role;
    public String group_id;
    public GroupProviderV1 provider;
    public String provider_uid;

    public Builder() {
    }

    public Builder(MemberV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.id = message.id;
      this.profile = message.profile;
      this.role = message.role;
      this.group_id = message.group_id;
      this.provider = message.provider;
      this.provider_uid = message.provider_uid;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder profile(ProfileV1 profile) {
      this.profile = profile;
      return this;
    }

    public Builder role(RoleV1 role) {
      this.role = role;
      return this;
    }

    public Builder group_id(String group_id) {
      this.group_id = group_id;
      return this;
    }

    public Builder provider(GroupProviderV1 provider) {
      this.provider = provider;
      return this;
    }

    public Builder provider_uid(String provider_uid) {
      this.provider_uid = provider_uid;
      return this;
    }

    @Override
    public MemberV1 build() {
      return new MemberV1(this);
    }
  }
}
