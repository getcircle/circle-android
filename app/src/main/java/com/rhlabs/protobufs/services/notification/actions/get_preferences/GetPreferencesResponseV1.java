// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/notification/actions/get_preferences.proto
package com.rhlabs.protobufs.services.notification.actions.get_preferences;

import com.rhlabs.protobufs.services.notification.containers.NotificationPreferenceV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GetPreferencesResponseV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final List<NotificationPreferenceV1> DEFAULT_PREFERENCES = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, label = REPEATED, messageType = NotificationPreferenceV1.class)
  public final List<NotificationPreferenceV1> preferences;

  public GetPreferencesResponseV1(Integer version, List<NotificationPreferenceV1> preferences) {
    this.version = version;
    this.preferences = immutableCopyOf(preferences);
  }

  private GetPreferencesResponseV1(Builder builder) {
    this(builder.version, builder.preferences);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetPreferencesResponseV1)) return false;
    GetPreferencesResponseV1 o = (GetPreferencesResponseV1) other;
    return equals(version, o.version)
        && equals(preferences, o.preferences);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (preferences != null ? preferences.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetPreferencesResponseV1> {

    public Integer version;
    public List<NotificationPreferenceV1> preferences;

    public Builder() {
    }

    public Builder(GetPreferencesResponseV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.preferences = copyOf(message.preferences);
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder preferences(List<NotificationPreferenceV1> preferences) {
      this.preferences = checkForNulls(preferences);
      return this;
    }

    @Override
    public GetPreferencesResponseV1 build() {
      return new GetPreferencesResponseV1(this);
    }
  }
}
