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

package com.delvinglanguages.server.delvingApi.model;

/**
 * Model definition for Language.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the delvingApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Language extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer code;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long dbid;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean isPublic;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer nDrawerWords;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer nTests;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer nThemes;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer nWords;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer settings;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("user_id")
  private java.lang.String userId;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getCode() {
    return code;
  }

  /**
   * @param code code or {@code null} for none
   */
  public Language setCode(java.lang.Integer code) {
    this.code = code;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getDbid() {
    return dbid;
  }

  /**
   * @param dbid dbid or {@code null} for none
   */
  public Language setDbid(java.lang.Long dbid) {
    this.dbid = dbid;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Language setId(java.lang.Integer id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getIsPublic() {
    return isPublic;
  }

  /**
   * @param isPublic isPublic or {@code null} for none
   */
  public Language setIsPublic(java.lang.Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNDrawerWords() {
    return nDrawerWords;
  }

  /**
   * @param nDrawerWords nDrawerWords or {@code null} for none
   */
  public Language setNDrawerWords(java.lang.Integer nDrawerWords) {
    this.nDrawerWords = nDrawerWords;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNTests() {
    return nTests;
  }

  /**
   * @param nTests nTests or {@code null} for none
   */
  public Language setNTests(java.lang.Integer nTests) {
    this.nTests = nTests;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNThemes() {
    return nThemes;
  }

  /**
   * @param nThemes nThemes or {@code null} for none
   */
  public Language setNThemes(java.lang.Integer nThemes) {
    this.nThemes = nThemes;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNWords() {
    return nWords;
  }

  /**
   * @param nWords nWords or {@code null} for none
   */
  public Language setNWords(java.lang.Integer nWords) {
    this.nWords = nWords;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * @param name name or {@code null} for none
   */
  public Language setName(java.lang.String name) {
    this.name = name;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getSettings() {
    return settings;
  }

  /**
   * @param settings settings or {@code null} for none
   */
  public Language setSettings(java.lang.Integer settings) {
    this.settings = settings;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUserId() {
    return userId;
  }

  /**
   * @param userId userId or {@code null} for none
   */
  public Language setUserId(java.lang.String userId) {
    this.userId = userId;
    return this;
  }

  @Override
  public Language set(String fieldName, Object value) {
    return (Language) super.set(fieldName, value);
  }

  @Override
  public Language clone() {
    return (Language) super.clone();
  }

}
