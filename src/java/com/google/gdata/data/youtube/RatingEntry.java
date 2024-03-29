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


package com.google.gdata.data.youtube;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.extensions.Rating;

/**
 * An entry in the rating feed.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_RATING)
public class RatingEntry extends BaseEntry<RatingEntry> {

  public RatingEntry() {
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_RATING);
  }

  public RatingEntry(BaseEntry base) {
    super(base);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_RATING);
  }

  /** Gets the gd:rating tag. */
  public Rating getRating() {
    return getExtension(Rating.class);
  }

  /** Sets the gd:rating tag. */
  public void setRating(Rating rating) {
    if (rating == null) {
      removeExtension(Rating.class);
    } else {
      setExtension(rating);
    }
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(RatingEntry.class, Rating.getDefaultDescription(false));
    extProfile.declareArbitraryXmlExtension(RatingEntry.class);
  }
  
}
