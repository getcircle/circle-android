// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./src/protobufs/services/user/actions/complete_authorization.proto
package com.rhlabs.protobufs.services.user.actions.complete_authorization;

import com.rhlabs.protobufs.services.user.containers.IdentityV1;
import com.rhlabs.protobufs.services.user.containers.OAuth2DetailsV1;
import com.rhlabs.protobufs.services.user.containers.OAuthSDKDetailsV1;
import com.rhlabs.protobufs.services.user.containers.token.ClientTypeV1;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT32;

public final class CompleteAuthorizationRequestV1 extends Message {
  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VERSION = 1;
  public static final IdentityV1.ProviderV1 DEFAULT_PROVIDER = IdentityV1.ProviderV1.INTERNAL;
  public static final ClientTypeV1 DEFAULT_CLIENT_TYPE = ClientTypeV1.IOS;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer version;

  @ProtoField(tag = 2, type = ENUM)
  public final IdentityV1.ProviderV1 provider;

  @ProtoField(tag = 3)
  public final OAuth2DetailsV1 oauth2_details;

  @ProtoField(tag = 4)
  public final OAuthSDKDetailsV1 oauth_sdk_details;

  @ProtoField(tag = 5, type = ENUM)
  public final ClientTypeV1 client_type;

  public CompleteAuthorizationRequestV1(Integer version, IdentityV1.ProviderV1 provider, OAuth2DetailsV1 oauth2_details, OAuthSDKDetailsV1 oauth_sdk_details, ClientTypeV1 client_type) {
    this.version = version;
    this.provider = provider;
    this.oauth2_details = oauth2_details;
    this.oauth_sdk_details = oauth_sdk_details;
    this.client_type = client_type;
  }

  private CompleteAuthorizationRequestV1(Builder builder) {
    this(builder.version, builder.provider, builder.oauth2_details, builder.oauth_sdk_details, builder.client_type);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CompleteAuthorizationRequestV1)) return false;
    CompleteAuthorizationRequestV1 o = (CompleteAuthorizationRequestV1) other;
    return equals(version, o.version)
        && equals(provider, o.provider)
        && equals(oauth2_details, o.oauth2_details)
        && equals(oauth_sdk_details, o.oauth_sdk_details)
        && equals(client_type, o.client_type);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = version != null ? version.hashCode() : 0;
      result = result * 37 + (provider != null ? provider.hashCode() : 0);
      result = result * 37 + (oauth2_details != null ? oauth2_details.hashCode() : 0);
      result = result * 37 + (oauth_sdk_details != null ? oauth_sdk_details.hashCode() : 0);
      result = result * 37 + (client_type != null ? client_type.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<CompleteAuthorizationRequestV1> {

    public Integer version;
    public IdentityV1.ProviderV1 provider;
    public OAuth2DetailsV1 oauth2_details;
    public OAuthSDKDetailsV1 oauth_sdk_details;
    public ClientTypeV1 client_type;

    public Builder() {
    }

    public Builder(CompleteAuthorizationRequestV1 message) {
      super(message);
      if (message == null) return;
      this.version = message.version;
      this.provider = message.provider;
      this.oauth2_details = message.oauth2_details;
      this.oauth_sdk_details = message.oauth_sdk_details;
      this.client_type = message.client_type;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder provider(IdentityV1.ProviderV1 provider) {
      this.provider = provider;
      return this;
    }

    public Builder oauth2_details(OAuth2DetailsV1 oauth2_details) {
      this.oauth2_details = oauth2_details;
      return this;
    }

    public Builder oauth_sdk_details(OAuthSDKDetailsV1 oauth_sdk_details) {
      this.oauth_sdk_details = oauth_sdk_details;
      return this;
    }

    public Builder client_type(ClientTypeV1 client_type) {
      this.client_type = client_type;
      return this;
    }

    @Override
    public CompleteAuthorizationRequestV1 build() {
      return new CompleteAuthorizationRequestV1(this);
    }
  }
}
