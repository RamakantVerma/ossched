/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.client.authn.oauth;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper methods to support the entire OAuth lifecycle, including generating 
 * the user authorization url, exchanging the user authenticated request token 
 * for an access token, and generating the Authorization http header.
 * 
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 *
 * 
 */
public class OAuthHelper {

  private String requestTokenUrl;
  private String userAuthorizationUrl;
  private String accessTokenUrl;

  private OAuthHttpClient httpClient;
  private OAuthSigner signer;

  /**
   * An abstract helper class for generating a string of key/value pairs that 
   * are separated by string delimiters.  For example, suppose there is a set of
   * key value pairs: key1/value1, key2/value2, etc.  Using the 
   * {@link QueryKeyValuePair} class, the resulting string would look like:
   * key1=value1&key2=value2.  There is no trailing ampersand at the end and 
   * each key and value will be encoded according to the OAuth spec
   * (<a href="http://oauth.net/core/1.0/#encoding_parameters">Section 5.1</a>).
   * 
   * 
   */
  private static abstract class KeyValuePair {
    private List<String> keys;
    private List<String> values;
    private String keyValueStartDelimiter;
    private String keyValueEndDelimiter;
    private String pairDelimiter;

    /**
     * Create a new instance.  The string delimiters specified as inputs are 
     * used by the {@link #toString} method to generating the string.
     * 
     * @param keyValueStartDelimiter the delimiter placed between the key and 
     *        the value
     * @param keyValueEndDelimiter the delimiter placed after the value
     * @param pairDelimiter the delimiter placed in between each key/value pair
     */
    protected KeyValuePair(String keyValueStartDelimiter, 
        String keyValueEndDelimiter, String pairDelimiter) {
      this.keyValueStartDelimiter = keyValueStartDelimiter;
      this.keyValueEndDelimiter = keyValueEndDelimiter;
      this.pairDelimiter = pairDelimiter;
      keys = new ArrayList<String>();
      values = new ArrayList<String>();
    }

    /**
     * Add a key/value pair
     * 
     * @param key the key of the pair
     * @param value the value of the pair
     */
    public void add(String key, String value) {
      keys.add(key);
      values.add(value);
    }

    /**
     * Get the key at the input position.
     * 
     * @param i the position to retrieve the key
     * @return the key at the input position
     */
    public String getKey(int i) {
      return keys.get(i);
    }

    /**
     * Get the value at the input position.
     * 
     * @param i the position to retrieve the value
     * @return the value at the input position
     */
    public String getValue(int i) {
      return values.get(i);
    }

    /**
     * Get the number of key/value pairs.
     * 
     * @return the number of key/value pairs.
     */
    public int size() {
      return keys.size();
    }

    /**
     * Concatenates the key/value pairs into a string.  For example, suppose 
     * there is a set of key value pairs: key1/value1, key2/value2, etc.  Using 
     * the {@link QueryKeyValuePair} class, the resulting string would look 
     * like: key1=value1&key2=value2.  There is no trailing ampersand at the end
     * and each key and value will be encoded according to the OAuth spec 
     * (<a href="http://oauth.net/core/1.0/#encoding_parameters">Section 
     * 5.1</a>).
     */
    @Override
    public String toString() {
      StringBuilder keyValueString = new StringBuilder();
      for (int i = 0, length = size(); i < length; i++) {
        if (i > 0) {
          keyValueString.append(pairDelimiter);
        }
        keyValueString.append(OAuthUtil.encode(getKey(i)))
            .append(keyValueStartDelimiter)
            .append(OAuthUtil.encode(getValue(i))).append(keyValueEndDelimiter);
      }
      return keyValueString.toString();
    }
  }

  /**
   * Generates a key/value string appropriate for a url's query string.  For 
   * example: key1=value1&key2=value2&key3=value3.
   * 
   * 
   */
  private static class QueryKeyValuePair extends KeyValuePair {
    public QueryKeyValuePair() {
      super("=", "", "&");
    }
  }

  /**
   * Generates a key/value string appropriate for an Authorization header.  
   * For example: key1="value1", key2="value2", key3="value3".
   * 
   * 
   */
  private static class HeaderKeyValuePair extends KeyValuePair {
    public HeaderKeyValuePair() {
      super("=\"", "\"", ", ");
    }
  }

  /**
   * Create a new {@link OAuthHelper} object.
   * 
   * @param requestTokenUrl the url used to obtain an unauthorized request token
   * @param userAuthorizationUrl the url used to obtain user authorization for 
   *        consumer access
   * @param accessTokenUrl the url used to exchange the user-authorized request 
   *        token for an access token
   * @param signer the {@link OAuthSigner} to use when signing the request
   */
  public OAuthHelper(String requestTokenUrl, String userAuthorizationUrl, 
      String accessTokenUrl, OAuthSigner signer) {
    this(requestTokenUrl, userAuthorizationUrl, accessTokenUrl, signer, 
        new OAuthHttpClient());
  }

  /**
   * Create a new {@link OAuthHelper} object.  This version of the constructor 
   * is primarily for testing purposes, where a mocked {@link OAuthHttpClient} 
   * and {@link OAuthSigner} can be specified.
   * 
   * @param requestTokenUrl the url used to obtain an unauthorized request token
   * @param userAuthorizationUrl the url used to obtain user authorization for 
   *        consumer access
   * @param accessTokenUrl the url used to exchange the user-authorized request 
   *        token for an access token
   * @param signer the {@link OAuthSigner} to use when signing the request
   * @param httpClient the {@link OAuthHttpClient} to use when making http 
   *        requests
   */
  OAuthHelper(String requestTokenUrl, String userAuthorizationUrl, 
      String accessTokenUrl, OAuthSigner signer, OAuthHttpClient httpClient) {
    this.requestTokenUrl = requestTokenUrl;
    this.userAuthorizationUrl = userAuthorizationUrl;
    this.accessTokenUrl = accessTokenUrl;
    this.signer = signer;
    this.httpClient = httpClient;
  }

  /** Get the access token url */
  public String getAccessTokenUrl() {
    return accessTokenUrl;
  }

  /** Set the access token url */
  public void setAccessTokenUrl(String url) {
    accessTokenUrl = url;
  }

  /** Get the request token url */
  public String getRequestTokenUrl() {
    return requestTokenUrl;
  }

  /** Set the request token url */
  public void setRequestTokenUrl(String url) {
    requestTokenUrl = url;
  }

  /** Get the user authorization url */
  public String getUserAuthorizationUrl() {
    return userAuthorizationUrl;
  }

  /** Set the user authorization url */
  public void setUserAuthorizationUrl(String url) {
    userAuthorizationUrl = url;
  }

  /**
   * Generates the url which the user should visit in order to authenticate and 
   * authorize with the Service Provider. The url will look something like this:
   * https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=CMiJx-LdFxCRkJbvBw&oauth_callback=http%3A%2F%2Fwww.google.com%2F
   * <p>
   * The following parameter is required in {@link OAuthParameters}: 
   * <ul>
   * <li>oauth_consumer_key
   * </ul>
   * <p>
   * The following parameter is optional:
   * <ul>
   * <li>oauth_callback
   * </ul>
   * <p>
   * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Step 1</a>
   * 
   * @param oauthParameters the OAuth parameters necessary for this request
   * @return The full authorization url the user should visit.  The method also 
   *         modifies the oauthParameters object by adding the request token and
   *         token secret.
   * @throws OAuthException if there is an error with the OAuth request
   */
  public String getUserAuthorizationUrl(OAuthParameters oauthParameters) 
      throws OAuthException {

    // STEP 1: Validate the input parameters
    oauthParameters.assertOAuthConsumerKeyExists();

    // STEP 2: Generates a signed URL that allows the consumer to retrieve the 
    // unauthorized request token.
    URL url = getOAuthUrl(requestTokenUrl, "GET", oauthParameters);

    // STEP 3: Retrieve the unauthorized request token and store it in the 
    // oauthParameters
    String response = httpClient.getResponse(url);
    Map<String, String> queryString = OAuthUtil.parseQuerystring(response);
    oauthParameters.setOAuthToken(
        queryString.get(OAuthParameters.OAUTH_TOKEN_KEY));
    oauthParameters.setOAuthTokenSecret(
        queryString.get(OAuthParameters.OAUTH_TOKEN_SECRET_KEY));

    // STEP 4: Format and return the user authorization url.
    KeyValuePair queryParams = new QueryKeyValuePair();
    queryParams.add(OAuthParameters.OAUTH_TOKEN_KEY, 
        oauthParameters.getOAuthToken());
    if (oauthParameters.getOAuthCallback().length() > 0) {
      queryParams.add(OAuthParameters.OAUTH_CALLBACK_KEY, 
          oauthParameters.getOAuthCallback());
    }

    // clear the request-specific parameters set in getOAuthUrl(), such as 
    // nonce, timestamp and signature, which are only needed for a single 
    // request.
    oauthParameters.reset();

    return (new StringBuilder()).append(userAuthorizationUrl).append("?")
        .append(queryParams.toString()).toString();
  }

  /**
   * Exchanges the user-authorized request token for an access token. This 
   * method parses the user-authorized request token from the authorization 
   * response url, and passes it on to 
   * {@link #getAccessToken(String, OAuthParameters)}.
   * <p>
   * The following parameters are required in {@link OAuthParameters}:
   * <ul>
   * <li>oauth_consumer_key
   * </ul>
   * @see <a href="http://oauth.net/core/1.0/#auth_step3">OAuth Step 3</a>
   * 
   * @param url the url to parse the request token from
   * @param oauthParameters OAuth parameters for this request
   * @return the access token
   * @throws OAuthException if there is an error with the OAuth request
   */
  public String getAccessToken(URL url, OAuthParameters oauthParameters)
      throws OAuthException {
    return getAccessToken(url.getQuery(), oauthParameters);
  }

  /**
   * Exchanges the user-authorized request token for an access token. This 
   * method parses the user-authorized request token from the authorization 
   * response's query string, and passes it on to 
   * {@link #getAccessToken(OAuthParameters)} (The query string is everything 
   * in the authorization response URL after the question mark).
   * <p>
   * The following parameters are required in {@link OAuthParameters}:
   * <ul>
   * <li>oauth_consumer_key
   * </ul>
   * @see <a href="http://oauth.net/core/1.0/#auth_step3">OAuth Step 3</a>
   * 
   * @param queryString the query string containing the request token
   * @param oauthParameters OAuth parameters for this request
   * @return the access token
   * @throws OAuthException if there is an error with the OAuth request
   */
  public String getAccessToken(String queryString, 
      OAuthParameters oauthParameters) throws OAuthException {
    // parse the querystring, and store the parsed values in oauthParameters.
    Map<String, String> params = OAuthUtil.parseQuerystring(queryString);
    oauthParameters.setOAuthToken(params.get(OAuthParameters.OAUTH_TOKEN_KEY));
    oauthParameters.setOAuthTokenSecret(
        params.get(OAuthParameters.OAUTH_TOKEN_SECRET_KEY));
    return getAccessToken(oauthParameters);
  }

  /**
   * Exchanges the user-authorized request token for an access token. 
   * Typically, this method is called immediately after you extract the 
   * user-authorized request token from the authorization response, but it can 
   * also be triggered by a user action indicating they've successfully 
   * completed authorization with the service provider.
   * <p>
   * The following parameters are required in {@link OAuthParameters}:
   * <ul>
   * <li>oauth_consumer_key
   * <li>oauth_token
   * </ul>
   * @see <a href="http://oauth.net/core/1.0/#auth_step3">OAuth Step 3</a>
   * 
   * @param oauthParameters OAuth parameters for this request
   * @return The access token.  This method also replaces the request token 
   *         with the access token in the oauthParameters object.
   * @throws OAuthException if there is an error with the OAuth request
   */
  public String getAccessToken(OAuthParameters oauthParameters) 
      throws OAuthException {

    // // STEP 1: Validate the input parameters
    oauthParameters.assertOAuthConsumerKeyExists();
    oauthParameters.assertOAuthTokenExists();

    // STEP 2: Generate the OAuth request url based on the input parameters.
    URL url = getOAuthUrl(accessTokenUrl, "GET", oauthParameters);

    // STEP 3: Make a request for the access token, and store it in 
    // oauthParameters
    String response = httpClient.getResponse(url);
    Map<String, String> queryString = OAuthUtil.parseQuerystring(response);
    oauthParameters.setOAuthToken(
        queryString.get(OAuthParameters.OAUTH_TOKEN_KEY));
    oauthParameters.setOAuthTokenSecret(
        queryString.get(OAuthParameters.OAUTH_TOKEN_SECRET_KEY));


    // clear the request-specific parameters set in getOAuthUrl(), such as 
    // nonce, timestamp and signature, which are only needed for a single 
    // request.
    oauthParameters.reset();

    return oauthParameters.getOAuthToken();
  }

  /**
   * Generates the string to be used as the HTTP authorization header.  A 
   * typical authorization header will look something like this:
   * <p>
   * OAuth realm="", oauth_signature="SOME_LONG_STRING", oauth_nonce="123456", 
   * oauth_signature_method="RSA-SHA1", oauth_consumer_key="www.example.com", 
   * oauth_token="abc123", oauth_timestamp="123456"
   * <p>
   * The following parameters are required in {@link OAuthParameters}:
   * <ul>
   * <li>oauth_consumer_key
   * <li>oauth_token
   * </ul>
   * @see <a href="http://oauth.net/core/1.0/#auth_header_authorization">OAuth 
   *      Authorization Header</a>
   * 
   * @param requestUrl the url of the request
   * @param httpMethod the http method of the request (for example GET)
   * @param oauthParameters OAuth parameters for this request
   * @return the full authorization header
   * @throws OAuthException if there is an error with the OAuth request
   */
  public String getAuthorizationHeader(String requestUrl, String httpMethod, 
      OAuthParameters oauthParameters) throws OAuthException {

    // validate input parameters
    oauthParameters.assertOAuthConsumerKeyExists();
    oauthParameters.assertOAuthTokenExists();

    // add request-specific parameters
    addCommonRequestParameters(requestUrl, httpMethod, oauthParameters);

    // Add the "realm" item to the header
    KeyValuePair headerParams = new HeaderKeyValuePair();
    headerParams.add(OAuthParameters.REALM_KEY, oauthParameters.getRealm());

    // Add the signature to the header
    headerParams.add(OAuthParameters.OAUTH_SIGNATURE_KEY, 
        oauthParameters.getOAuthSignature());

    // Add all other base parameters to the header
    for (Map.Entry<String, String> e : 
        oauthParameters.getBaseParameters().entrySet()) {
      if (e.getValue().length() > 0) {
        headerParams.add(e.getKey(), e.getValue());
      }
    }

    // clear the request-specific parameters set in 
    // addCommonRequestParameters(), such as nonce, timestamp and signature, 
    // which are only needed for a single request.
    oauthParameters.reset();

    return (new StringBuilder()).append(OAuthParameters.OAUTH_KEY).append(" ")
        .append(headerParams.toString()).toString();
  }

  /**
   * Returns a properly formatted and signed OAuth request url, with the 
   * appropriate parameters.
   * 
   * @param baseUrl the url to make the request to
   * @param httpMethod the http method of this request (for example, "GET")
   * @param oauthParameters OAuth parameters for this request
   * @return the OAuth request url
   * @throws OAuthException if there is an error with the OAuth request
   */
  private URL getOAuthUrl(String baseUrl, String httpMethod, 
      OAuthParameters oauthParameters) throws OAuthException {
    // add request-specific parameters
    addCommonRequestParameters(baseUrl, httpMethod, oauthParameters);

    // add all query string information
    KeyValuePair queryParams = new QueryKeyValuePair();
    for (Map.Entry<String, String> e : 
        oauthParameters.getBaseParameters().entrySet()) {
      if (e.getValue().length() > 0) {
        queryParams.add(e.getKey(), e.getValue());
      }
    }
    queryParams.add(OAuthParameters.OAUTH_SIGNATURE_KEY, 
        oauthParameters.getOAuthSignature());

    // build the url string
    StringBuilder fullUrl = new StringBuilder(baseUrl);
    fullUrl.append(baseUrl.indexOf("?") > 0 ? "&" : "?");
    fullUrl.append(queryParams.toString());

    try {
      return new URL(fullUrl.toString());
    } catch (MalformedURLException mue) {
      throw new OAuthException(mue);
    }
  }

  /**
   * Generate and add request-specific parameters that are common to all OAuth 
   * requests (if the user did not already specify them in the oauthParameters
   * object). The following parameters are added to the oauthParameter set:
   * <ul>
   * <li>oauth_signature
   * <li>oauth_signature_method
   * <li>oauth_timestamp
   * <li>oauth_nonce
   * 
   * @param baseUrl the url to make the request to
   * @param httpMethod the http method of this request (for example, "GET")
   * @param oauthParameters OAuth parameters for this request
   * @throws OAuthException if there is an error with the OAuth request
   */
  private void addCommonRequestParameters(String baseUrl, String httpMethod,
      OAuthParameters oauthParameters) throws OAuthException {
    // add the signature method if it doesn't already exist.
    if (oauthParameters.getOAuthSignatureMethod().length() == 0) {
      oauthParameters.setOAuthSignatureMethod(signer.getSignatureMethod());
    }
    // add the nonce if it doesn't already exist.
    if (oauthParameters.getOAuthTimestamp().length() == 0) {
      oauthParameters.setOAuthTimestamp(OAuthUtil.getTimestamp());
    }
    // add the timestamp if it doesn't already exist.
    if (oauthParameters.getOAuthNonce().length() == 0) {
      oauthParameters.setOAuthNonce(OAuthUtil.getNonce());
    }
    // add the signature if it doesn't already exist.
    // The signature is calculated by the {@link OAuthSigner}.
    if (oauthParameters.getOAuthSignature().length() == 0) {
      String baseString = OAuthUtil.getSignatureBaseString(baseUrl, httpMethod,
          oauthParameters.getBaseParameters());
      oauthParameters.setOAuthSignature(
          signer.getSignature(baseString, oauthParameters));
    }
  }
}
