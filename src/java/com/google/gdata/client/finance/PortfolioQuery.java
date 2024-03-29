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


package com.google.gdata.client.finance;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed of all the user's portfolios.
 *
 * 
 */
public class PortfolioQuery extends Query {

  /** Flag to inline positions under the portfolio feed. */
  private Boolean inlinePositions = false;

  /** Flag indicating whether returns and performance stats should be included
   * in the portfolio and position feed entries. */
  private Boolean includeReturns = false;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public PortfolioQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the flag to inline positions under the portfolio feed.
   *
   * @return flag to inline positions under the portfolio feed.
   */
  public Boolean getInlinePositions() {
    return inlinePositions;
  }

  /**
   * Sets the flag to inline positions under the portfolio feed.
   *
   * @param inlinePositions flag to inline positions under the portfolio feed or
   *     <code>null</code> to reset this parameter to default value
   *     <code>false</code>.
   */
  public void setInlinePositions(Boolean inlinePositions) {
    if (inlinePositions == null) {
      inlinePositions = false;
    }
    // check if setting to existing value
    if (!this.inlinePositions.equals(inlinePositions)) {
      // set to new value for customer parameter
      this.inlinePositions = inlinePositions;
      setStringCustomParameter("positions",
          !inlinePositions ? null : inlinePositions.toString());
    }
  }

  /**
   * Returns the flag indicating whether returns and performance stats should be
   * included in the portfolio and position feed entries.
   *
   * @return flag indicating whether returns and performance stats should be
   *     included in the portfolio and position feed entries.
   */
  public Boolean getIncludeReturns() {
    return includeReturns;
  }

  /**
   * Sets the flag indicating whether returns and performance stats should be
   * included in the portfolio and position feed entries.
   *
   * @param includeReturns flag indicating whether returns and performance stats
   *     should be included in the portfolio and position feed entries or
   *     <code>null</code> to reset this parameter to default value
   *     <code>false</code>.
   */
  public void setIncludeReturns(Boolean includeReturns) {
    if (includeReturns == null) {
      includeReturns = false;
    }
    // check if setting to existing value
    if (!this.includeReturns.equals(includeReturns)) {
      // set to new value for customer parameter
      this.includeReturns = includeReturns;
      setStringCustomParameter("returns",
          !includeReturns ? null : includeReturns.toString());
    }
  }

}
