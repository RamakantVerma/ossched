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


package com.google.gdata.data.calendar;

import com.google.gdata.util.common.xml.XmlWriter;

/**
 * GData namespace definitions related to Google Calendar.
 *
 * 
 */
public class Namespaces {

  private Namespaces() {}

  /** Google Calendar (GCAL) namespace */
  public static final String gCal = "http://schemas.google.com/gCal/2005";

  /** Google Calendar (GCAL) namespace prefix */
  public static final String gCalPrefix = gCal + "#";

  /** Google Calendar (GCAL) namespace alias */
  public static final String gCalAlias = "gCal";

  /** XML writer namespace for Google Calendar (GCAL) */
  public static final XmlWriter.Namespace gCalNs = new
      XmlWriter.Namespace(gCalAlias, gCal);

}
