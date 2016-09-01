/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-07-08 17:28:43 UTC)
 * on 2016-08-31 at 02:48:54 UTC 
 * Modify at your own risk.
 */

package com.delvinglanguages.server.delvingApi;

/**
 * Service definition for DelvingApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link DelvingApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class DelvingApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.22.0 of the delvingApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://delving-890219.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "delvingApi/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public DelvingApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  DelvingApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getPublicLanguages".
   *
   * This request holds the parameters needed by the delvingApi server.  After setting any optional
   * parameters, call the {@link GetPublicLanguages#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetPublicLanguages getPublicLanguages() throws java.io.IOException {
    GetPublicLanguages result = new GetPublicLanguages();
    initialize(result);
    return result;
  }

  public class GetPublicLanguages extends DelvingApiRequest<com.delvinglanguages.server.delvingApi.model.LanguageCollection> {

    private static final String REST_PATH = "public_languages";

    /**
     * Create a request for the method "getPublicLanguages".
     *
     * This request holds the parameters needed by the the delvingApi server.  After setting any
     * optional parameters, call the {@link GetPublicLanguages#execute()} method to invoke the remote
     * operation. <p> {@link GetPublicLanguages#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected GetPublicLanguages() {
      super(DelvingApi.this, "GET", REST_PATH, null, com.delvinglanguages.server.delvingApi.model.LanguageCollection.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetPublicLanguages setAlt(java.lang.String alt) {
      return (GetPublicLanguages) super.setAlt(alt);
    }

    @Override
    public GetPublicLanguages setFields(java.lang.String fields) {
      return (GetPublicLanguages) super.setFields(fields);
    }

    @Override
    public GetPublicLanguages setKey(java.lang.String key) {
      return (GetPublicLanguages) super.setKey(key);
    }

    @Override
    public GetPublicLanguages setOauthToken(java.lang.String oauthToken) {
      return (GetPublicLanguages) super.setOauthToken(oauthToken);
    }

    @Override
    public GetPublicLanguages setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetPublicLanguages) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetPublicLanguages setQuotaUser(java.lang.String quotaUser) {
      return (GetPublicLanguages) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetPublicLanguages setUserIp(java.lang.String userIp) {
      return (GetPublicLanguages) super.setUserIp(userIp);
    }

    @Override
    public GetPublicLanguages set(String parameterName, Object value) {
      return (GetPublicLanguages) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getServerUpdate".
   *
   * This request holds the parameters needed by the delvingApi server.  After setting any optional
   * parameters, call the {@link GetServerUpdate#execute()} method to invoke the remote operation.
   *
   * @param lastSync
   * @return the request
   */
  public GetServerUpdate getServerUpdate(java.lang.Long lastSync) throws java.io.IOException {
    GetServerUpdate result = new GetServerUpdate(lastSync);
    initialize(result);
    return result;
  }

  public class GetServerUpdate extends DelvingApiRequest<com.delvinglanguages.server.delvingApi.model.UpdateWrapper> {

    private static final String REST_PATH = "getServerUpdate";

    /**
     * Create a request for the method "getServerUpdate".
     *
     * This request holds the parameters needed by the the delvingApi server.  After setting any
     * optional parameters, call the {@link GetServerUpdate#execute()} method to invoke the remote
     * operation. <p> {@link GetServerUpdate#initialize(com.google.api.client.googleapis.services.Abst
     * ractGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param lastSync
     * @since 1.13
     */
    protected GetServerUpdate(java.lang.Long lastSync) {
      super(DelvingApi.this, "GET", REST_PATH, null, com.delvinglanguages.server.delvingApi.model.UpdateWrapper.class);
      this.lastSync = com.google.api.client.util.Preconditions.checkNotNull(lastSync, "Required parameter lastSync must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetServerUpdate setAlt(java.lang.String alt) {
      return (GetServerUpdate) super.setAlt(alt);
    }

    @Override
    public GetServerUpdate setFields(java.lang.String fields) {
      return (GetServerUpdate) super.setFields(fields);
    }

    @Override
    public GetServerUpdate setKey(java.lang.String key) {
      return (GetServerUpdate) super.setKey(key);
    }

    @Override
    public GetServerUpdate setOauthToken(java.lang.String oauthToken) {
      return (GetServerUpdate) super.setOauthToken(oauthToken);
    }

    @Override
    public GetServerUpdate setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetServerUpdate) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetServerUpdate setQuotaUser(java.lang.String quotaUser) {
      return (GetServerUpdate) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetServerUpdate setUserIp(java.lang.String userIp) {
      return (GetServerUpdate) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key("last_sync")
    private java.lang.Long lastSync;

    /**

     */
    public java.lang.Long getLastSync() {
      return lastSync;
    }

    public GetServerUpdate setLastSync(java.lang.Long lastSync) {
      this.lastSync = lastSync;
      return this;
    }

    @Override
    public GetServerUpdate set(String parameterName, Object value) {
      return (GetServerUpdate) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeLanguage".
   *
   * This request holds the parameters needed by the delvingApi server.  After setting any optional
   * parameters, call the {@link RemoveLanguage#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public RemoveLanguage removeLanguage(java.lang.Integer id) throws java.io.IOException {
    RemoveLanguage result = new RemoveLanguage(id);
    initialize(result);
    return result;
  }

  public class RemoveLanguage extends DelvingApiRequest<Void> {

    private static final String REST_PATH = "remove_language";

    /**
     * Create a request for the method "removeLanguage".
     *
     * This request holds the parameters needed by the the delvingApi server.  After setting any
     * optional parameters, call the {@link RemoveLanguage#execute()} method to invoke the remote
     * operation. <p> {@link RemoveLanguage#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected RemoveLanguage(java.lang.Integer id) {
      super(DelvingApi.this, "DELETE", REST_PATH, null, Void.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public RemoveLanguage setAlt(java.lang.String alt) {
      return (RemoveLanguage) super.setAlt(alt);
    }

    @Override
    public RemoveLanguage setFields(java.lang.String fields) {
      return (RemoveLanguage) super.setFields(fields);
    }

    @Override
    public RemoveLanguage setKey(java.lang.String key) {
      return (RemoveLanguage) super.setKey(key);
    }

    @Override
    public RemoveLanguage setOauthToken(java.lang.String oauthToken) {
      return (RemoveLanguage) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveLanguage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveLanguage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveLanguage setQuotaUser(java.lang.String quotaUser) {
      return (RemoveLanguage) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveLanguage setUserIp(java.lang.String userIp) {
      return (RemoveLanguage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Integer id;

    /**

     */
    public java.lang.Integer getId() {
      return id;
    }

    public RemoveLanguage setId(java.lang.Integer id) {
      this.id = id;
      return this;
    }

    @Override
    public RemoveLanguage set(String parameterName, Object value) {
      return (RemoveLanguage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeLanguageItem".
   *
   * This request holds the parameters needed by the delvingApi server.  After setting any optional
   * parameters, call the {@link RemoveLanguageItem#execute()} method to invoke the remote operation.
   *
   * @param itemId
   * @param langId
   * @param type
   * @return the request
   */
  public RemoveLanguageItem removeLanguageItem(java.lang.Integer itemId, java.lang.Integer langId, java.lang.Integer type) throws java.io.IOException {
    RemoveLanguageItem result = new RemoveLanguageItem(itemId, langId, type);
    initialize(result);
    return result;
  }

  public class RemoveLanguageItem extends DelvingApiRequest<Void> {

    private static final String REST_PATH = "remove_language_item";

    /**
     * Create a request for the method "removeLanguageItem".
     *
     * This request holds the parameters needed by the the delvingApi server.  After setting any
     * optional parameters, call the {@link RemoveLanguageItem#execute()} method to invoke the remote
     * operation. <p> {@link RemoveLanguageItem#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param itemId
     * @param langId
     * @param type
     * @since 1.13
     */
    protected RemoveLanguageItem(java.lang.Integer itemId, java.lang.Integer langId, java.lang.Integer type) {
      super(DelvingApi.this, "DELETE", REST_PATH, null, Void.class);
      this.itemId = com.google.api.client.util.Preconditions.checkNotNull(itemId, "Required parameter itemId must be specified.");
      this.langId = com.google.api.client.util.Preconditions.checkNotNull(langId, "Required parameter langId must be specified.");
      this.type = com.google.api.client.util.Preconditions.checkNotNull(type, "Required parameter type must be specified.");
    }

    @Override
    public RemoveLanguageItem setAlt(java.lang.String alt) {
      return (RemoveLanguageItem) super.setAlt(alt);
    }

    @Override
    public RemoveLanguageItem setFields(java.lang.String fields) {
      return (RemoveLanguageItem) super.setFields(fields);
    }

    @Override
    public RemoveLanguageItem setKey(java.lang.String key) {
      return (RemoveLanguageItem) super.setKey(key);
    }

    @Override
    public RemoveLanguageItem setOauthToken(java.lang.String oauthToken) {
      return (RemoveLanguageItem) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveLanguageItem setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveLanguageItem) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveLanguageItem setQuotaUser(java.lang.String quotaUser) {
      return (RemoveLanguageItem) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveLanguageItem setUserIp(java.lang.String userIp) {
      return (RemoveLanguageItem) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key("item_id")
    private java.lang.Integer itemId;

    /**

     */
    public java.lang.Integer getItemId() {
      return itemId;
    }

    public RemoveLanguageItem setItemId(java.lang.Integer itemId) {
      this.itemId = itemId;
      return this;
    }

    @com.google.api.client.util.Key("lang_id")
    private java.lang.Integer langId;

    /**

     */
    public java.lang.Integer getLangId() {
      return langId;
    }

    public RemoveLanguageItem setLangId(java.lang.Integer langId) {
      this.langId = langId;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer type;

    /**

     */
    public java.lang.Integer getType() {
      return type;
    }

    public RemoveLanguageItem setType(java.lang.Integer type) {
      this.type = type;
      return this;
    }

    @Override
    public RemoveLanguageItem set(String parameterName, Object value) {
      return (RemoveLanguageItem) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateLanguage".
   *
   * This request holds the parameters needed by the delvingApi server.  After setting any optional
   * parameters, call the {@link UpdateLanguage#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.delvinglanguages.server.delvingApi.model.Language}
   * @return the request
   */
  public UpdateLanguage updateLanguage(com.delvinglanguages.server.delvingApi.model.Language content) throws java.io.IOException {
    UpdateLanguage result = new UpdateLanguage(content);
    initialize(result);
    return result;
  }

  public class UpdateLanguage extends DelvingApiRequest<Void> {

    private static final String REST_PATH = "update_language";

    /**
     * Create a request for the method "updateLanguage".
     *
     * This request holds the parameters needed by the the delvingApi server.  After setting any
     * optional parameters, call the {@link UpdateLanguage#execute()} method to invoke the remote
     * operation. <p> {@link UpdateLanguage#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link com.delvinglanguages.server.delvingApi.model.Language}
     * @since 1.13
     */
    protected UpdateLanguage(com.delvinglanguages.server.delvingApi.model.Language content) {
      super(DelvingApi.this, "PUT", REST_PATH, content, Void.class);
    }

    @Override
    public UpdateLanguage setAlt(java.lang.String alt) {
      return (UpdateLanguage) super.setAlt(alt);
    }

    @Override
    public UpdateLanguage setFields(java.lang.String fields) {
      return (UpdateLanguage) super.setFields(fields);
    }

    @Override
    public UpdateLanguage setKey(java.lang.String key) {
      return (UpdateLanguage) super.setKey(key);
    }

    @Override
    public UpdateLanguage setOauthToken(java.lang.String oauthToken) {
      return (UpdateLanguage) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateLanguage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateLanguage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateLanguage setQuotaUser(java.lang.String quotaUser) {
      return (UpdateLanguage) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateLanguage setUserIp(java.lang.String userIp) {
      return (UpdateLanguage) super.setUserIp(userIp);
    }

    @Override
    public UpdateLanguage set(String parameterName, Object value) {
      return (UpdateLanguage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateLanguageItem".
   *
   * This request holds the parameters needed by the delvingApi server.  After setting any optional
   * parameters, call the {@link UpdateLanguageItem#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.delvinglanguages.server.delvingApi.model.LanguageItem}
   * @return the request
   */
  public UpdateLanguageItem updateLanguageItem(com.delvinglanguages.server.delvingApi.model.LanguageItem content) throws java.io.IOException {
    UpdateLanguageItem result = new UpdateLanguageItem(content);
    initialize(result);
    return result;
  }

  public class UpdateLanguageItem extends DelvingApiRequest<Void> {

    private static final String REST_PATH = "updateLanguageItem";

    /**
     * Create a request for the method "updateLanguageItem".
     *
     * This request holds the parameters needed by the the delvingApi server.  After setting any
     * optional parameters, call the {@link UpdateLanguageItem#execute()} method to invoke the remote
     * operation. <p> {@link UpdateLanguageItem#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param content the {@link com.delvinglanguages.server.delvingApi.model.LanguageItem}
     * @since 1.13
     */
    protected UpdateLanguageItem(com.delvinglanguages.server.delvingApi.model.LanguageItem content) {
      super(DelvingApi.this, "PUT", REST_PATH, content, Void.class);
    }

    @Override
    public UpdateLanguageItem setAlt(java.lang.String alt) {
      return (UpdateLanguageItem) super.setAlt(alt);
    }

    @Override
    public UpdateLanguageItem setFields(java.lang.String fields) {
      return (UpdateLanguageItem) super.setFields(fields);
    }

    @Override
    public UpdateLanguageItem setKey(java.lang.String key) {
      return (UpdateLanguageItem) super.setKey(key);
    }

    @Override
    public UpdateLanguageItem setOauthToken(java.lang.String oauthToken) {
      return (UpdateLanguageItem) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateLanguageItem setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateLanguageItem) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateLanguageItem setQuotaUser(java.lang.String quotaUser) {
      return (UpdateLanguageItem) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateLanguageItem setUserIp(java.lang.String userIp) {
      return (UpdateLanguageItem) super.setUserIp(userIp);
    }

    @Override
    public UpdateLanguageItem set(String parameterName, Object value) {
      return (UpdateLanguageItem) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link DelvingApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link DelvingApi}. */
    @Override
    public DelvingApi build() {
      return new DelvingApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link DelvingApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setDelvingApiRequestInitializer(
        DelvingApiRequestInitializer delvingapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(delvingapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
