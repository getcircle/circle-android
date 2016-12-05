// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/profile/containers.proto
package com.rhlabs.protobufs.services.profile.containers;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class ProfileV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final String DEFAULT_ID = "";
  public static final String DEFAULT_ORGANIZATION_ID = "";
  public static final String DEFAULT_USER_ID = "";
  public static final String DEFAULT_TITLE = "";
  public static final String DEFAULT_FIRST_NAME = "";
  public static final String DEFAULT_LAST_NAME = "";
  public static final String DEFAULT_IMAGE_URL = "";
  public static final String DEFAULT_FULL_NAME = "";
  public static final String DEFAULT_BIRTH_DATE = "";
  public static final String DEFAULT_HIRE_DATE = "";
  public static final Boolean DEFAULT_VERIFIED = false;
  public static final List<ProfileItemV1> DEFAULT_ITEMS = Collections.emptyList();
  public static final String DEFAULT_NICKNAME = "";
  public static final List<ContactMethodV1> DEFAULT_CONTACT_METHODS = Collections.emptyList();
  public static final String DEFAULT_EMAIL = "";
  public static final Boolean DEFAULT_IS_ADMIN = false;
  public static final String DEFAULT_SMALL_IMAGE_URL = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = STRING)
  public final String id;

  @ProtoField(tag = 3, type = STRING)
  public final String organization_id;

  @ProtoField(tag = 4, type = STRING)
  public final String user_id;

  @ProtoField(tag = 5, type = STRING)
  public final String title;

  @ProtoField(tag = 6, type = STRING)
  public final String first_name;

  @ProtoField(tag = 7, type = STRING)
  public final String last_name;

  @ProtoField(tag = 8, type = STRING)
  public final String image_url;

  @ProtoField(tag = 9, type = STRING)
  public final String full_name;

  @ProtoField(tag = 10, type = STRING)
  public final String birth_date;

  @ProtoField(tag = 11, type = STRING)
  public final String hire_date;

  @ProtoField(tag = 12, type = BOOL)
  public final Boolean verified;

  @ProtoField(tag = 13, label = REPEATED, messageType = ProfileItemV1.class)
  public final List<ProfileItemV1> items;

  @ProtoField(tag = 14, type = STRING)
  public final String nickname;

  @ProtoField(tag = 15, label = REPEATED, messageType = ContactMethodV1.class)
  public final List<ContactMethodV1> contact_methods;

  @ProtoField(tag = 16, type = STRING)
  public final String email;

  @ProtoField(tag = 17, type = BOOL)
  public final Boolean is_admin;

  @ProtoField(tag = 18, type = STRING)
  public final String small_image_url;

  @ProtoField(tag = 19)
  public final ProfileStatusV1 status;

  public ProfileV1(Integer version, String id, String organization_id, String user_id, String title, String first_name, String last_name, String image_url, String full_name, String birth_date, String hire_date, Boolean verified, List<ProfileItemV1> items, String nickname, List<ContactMethodV1> contact_methods, String email, Boolean is_admin, String small_image_url, ProfileStatusV1 status) {
    this.version = version;
    this.id = id;
    this.organization_id = organization_id;
    this.user_id = user_id;
    this.title = title;
    this.first_name = first_name;
    this.last_name = last_name;
    this.image_url = image_url;
    this.full_name = full_name;
    this.birth_date = birth_date;
    this.hire_date = hire_date;
    this.verified = verified;
    this.items = immutableCopyOf(items);
    this.nickname = nickname;
    this.contact_methods = immutableCopyOf(contact_methods);
    this.email = email;
    this.is_admin = is_admin;
    this.small_image_url = small_image_url;
    this.status = status;
  }

  private ProfileV1(Builder builder) {
    this(builder.version, builder.id, builder.organization_id, builder.user_id, builder.title, builder.first_name, builder.last_name, builder.image_url, builder.full_name, builder.birth_date, builder.hire_date, builder.verified, builder.items, builder.nickname, builder.contact_methods, builder.email, builder.is_admin, builder.small_image_url, builder.status);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ProfileV1)) return false;
    ProfileV1 o = (ProfileV1) other;
    return equals(version, o.version)
        && equals(id, o.id)
        && equals(organization_id, o.organization_id)
        && equals(user_id, o.user_id)
        && equals(title, o.title)
        && equals(first_name, o.first_name)
        && equals(last_name, o.last_name)
        && equals(image_url, o.image_url)
        && equals(full_name, o.full_name)
        && equals(birth_date, o.birth_date)
        && equals(hire_date, o.hire_date)
        && equals(verified, o.verified)
        && equals(items, o.items)
        && equals(nickname, o.nickname)
        && equals(contact_methods, o.contact_methods)
        && equals(email, o.email)
        && equals(is_admin, o.is_admin)
        && equals(small_image_url, o.small_image_url)
        && equals(status, o.status);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (organization_id != null ? organization_id.hashCode() : 0);
      result = result * 37 + (user_id != null ? user_id.hashCode() : 0);
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (first_name != null ? first_name.hashCode() : 0);
      result = result * 37 + (last_name != null ? last_name.hashCode() : 0);
      result = result * 37 + (image_url != null ? image_url.hashCode() : 0);
      result = result * 37 + (full_name != null ? full_name.hashCode() : 0);
      result = result * 37 + (birth_date != null ? birth_date.hashCode() : 0);
      result = result * 37 + (hire_date != null ? hire_date.hashCode() : 0);
      result = result * 37 + (verified != null ? verified.hashCode() : 0);
      result = result * 37 + (items != null ? items.hashCode() : 1);
      result = result * 37 + (nickname != null ? nickname.hashCode() : 0);
      result = result * 37 + (contact_methods != null ? contact_methods.hashCode() : 1);
      result = result * 37 + (email != null ? email.hashCode() : 0);
      result = result * 37 + (is_admin != null ? is_admin.hashCode() : 0);
      result = result * 37 + (small_image_url != null ? small_image_url.hashCode() : 0);
      result = result * 37 + (status != null ? status.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ProfileV1> {

    public Integer version;
    public String id;
    public String organization_id;
    public String user_id;
    public String title;
    public String first_name;
    public String last_name;
    public String image_url;
    public String full_name;
    public String birth_date;
    public String hire_date;
    public Boolean verified;
    public List<ProfileItemV1> items;
    public String nickname;
    public List<ContactMethodV1> contact_methods;
    public String email;
    public Boolean is_admin;
    public String small_image_url;
    public ProfileStatusV1 status;

    public Builder() {
    }

    public Builder(ProfileV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.id = message.id;
      this.organization_id = message.organization_id;
      this.user_id = message.user_id;
      this.title = message.title;
      this.first_name = message.first_name;
      this.last_name = message.last_name;
      this.image_url = message.image_url;
      this.full_name = message.full_name;
      this.birth_date = message.birth_date;
      this.hire_date = message.hire_date;
      this.verified = message.verified;
      this.items = copyOf(message.items);
      this.nickname = message.nickname;
      this.contact_methods = copyOf(message.contact_methods);
      this.email = message.email;
      this.is_admin = message.is_admin;
      this.small_image_url = message.small_image_url;
      this.status = message.status;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder organization_id(String organization_id) {
      this.organization_id = organization_id;
      return this;
    }

    public Builder user_id(String user_id) {
      this.user_id = user_id;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder first_name(String first_name) {
      this.first_name = first_name;
      return this;
    }

    public Builder last_name(String last_name) {
      this.last_name = last_name;
      return this;
    }

    public Builder image_url(String image_url) {
      this.image_url = image_url;
      return this;
    }

    public Builder full_name(String full_name) {
      this.full_name = full_name;
      return this;
    }

    public Builder birth_date(String birth_date) {
      this.birth_date = birth_date;
      return this;
    }

    public Builder hire_date(String hire_date) {
      this.hire_date = hire_date;
      return this;
    }

    public Builder verified(Boolean verified) {
      this.verified = verified;
      return this;
    }

    public Builder items(List<ProfileItemV1> items) {
      this.items = checkForNulls(items);
      return this;
    }

    public Builder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    public Builder contact_methods(List<ContactMethodV1> contact_methods) {
      this.contact_methods = checkForNulls(contact_methods);
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder is_admin(Boolean is_admin) {
      this.is_admin = is_admin;
      return this;
    }

    public Builder small_image_url(String small_image_url) {
      this.small_image_url = small_image_url;
      return this;
    }

    public Builder status(ProfileStatusV1 status) {
      this.status = status;
      return this;
    }

    @Override
    public ProfileV1 build() {
      return new ProfileV1(this);
    }
  }
}