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
 * Model definition for LanguageItem.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the delvingApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class LanguageItem extends com.google.api.client.json.GenericJson {

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
  @com.google.api.client.util.Key("language_id")
  private java.lang.Integer languageId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer type;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("user_id")
  private java.lang.String userId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String wrapper;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getDbid() {
    return dbid;
  }

  /**
   * @param dbid dbid or {@code null} for none
   */
  public LanguageItem setDbid(java.lang.Long dbid) {
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
  public LanguageItem setId(java.lang.Integer id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getLanguageId() {
    return languageId;
  }

  /**
   * @param languageId languageId or {@code null} for none
   */
  public LanguageItem setLanguageId(java.lang.Integer languageId) {
    this.languageId = languageId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getType() {
    return type;
  }

  /**
   * @param type type or {@code null} for none
   */
  public LanguageItem setType(java.lang.Integer type) {
    this.type = type;
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
  public LanguageItem setUserId(java.lang.String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getWrapper() {
    return wrapper;
  }

  /**
   * @param wrapper wrapper or {@code null} for none
   */
  public LanguageItem setWrapper(java.lang.String wrapper) {
    this.wrapper = wrapper;
    return this;
  }

  @Override
  public LanguageItem set(String fieldName, Object value) {
    return (LanguageItem) super.set(fieldName, value);
  }

  @Override
  public LanguageItem clone() {
    return (LanguageItem) super.clone();
  }

}
