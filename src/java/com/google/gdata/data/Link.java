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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * External link type.
 */
public class Link extends ExtensionPoint implements ILink {

  /**
   * The Rel class defines constants for some common link relation types.
   */
  public static final class Rel {

    /**
     * Link provides the URI of the feed or entry. If this
     * relation appears on a feed that is the result of performing a
     * query, then this URI includes the same query parameters (or at
     * least querying this URI produces the same result as querying with
     * those parameters).
     */
    public static final String SELF = "self";


    /** Link provides the URI of previous page in a paged feed. */
    public static final String PREVIOUS = "previous";


    /** Link provides the URI of next page in a paged feed. */
    public static final String NEXT = "next";


    /**
     * Link provides the URI of an alternate format of the
     * entry's or feed's contents. The {@code type} property of the link
     * specifies a media type.
     */
    public static final String ALTERNATE = "alternate";

    /**
     * Link provides the URI of a related link to the entry
     */
    public static final String RELATED = "related";

    /**
     * Link provides the URI of the full feed (without any
     * query parameters).
     */
    public static final String FEED = Namespaces.gPrefix + "feed";


    /**
     * Link provides the URI that can be used to post new
     * entries to the feed. This relation does not exist if the feed is
     * read-only.
     */
    public static final String ENTRY_POST = Namespaces.gPrefix + "post";


    /**
     * Link provides the URI that can be used to edit the entry.
     * This relation does not exist if the entry is read-only.
     */
    public static final String ENTRY_EDIT = "edit";

    /**
     * Link provides the URI that can be used to edit the media
     * associated with an entry.  This relation does not exist if
     * there is no associated media or the media is read-only.
     */
    public static final String MEDIA_EDIT = "edit-media";

    /**
     * Previous media edit link relation value that will temporarily be
     * supported to enable back compatibility for Picasa Web.  This rel
     * will be deleted after all usage has been migrated to use
     * {@link #MEDIA_EDIT}.
     * 
     * @deprecated use {@link Rel#MEDIA_EDIT} instead.
     */
    @Deprecated
    public static final String MEDIA_EDIT_BACKCOMPAT = "media-edit";

    /**
     * Link provides the URI that can be used to insert, update
     * and delete entries on this feed. This relation does not exist
     * if the feed is read-only or if batching not enabled on this
     * feed.
     */
    public static final String FEED_BATCH = Namespaces.gPrefix + "batch";

    /**
     * Link provides the URI that of link that provides the data
     * for the content in the feed.
     */
    public static final String VIA = "via";

    /**
     * Relation for links to enclosure (podcasting) files.
     */
    public static final String ENCLOSURE = "enclosure";
  }

  /**
   * The Type class contains several common link content types.
   */
  public static final class Type {


    /** Defines the link type used for Atom content. */
    public static final String ATOM = ContentType.ATOM.getMediaType();


    /** Defines the link type used for HTML content. */
    public static final String HTML = ContentType.TEXT_HTML.getMediaType();
  }

  public Link() {}

  public Link(String rel, String type, String href) {
    this.rel = rel;
    this.type = type;
    setHref(href);
  }
  
  /**
   * Link relation type.  Possible values include {@code self}, {@code
   * prev}, {@code next}, {@code enclosure}, etc.
   */
  protected String rel;
  public String getRel() {
    return rel != null ? rel : Rel.ALTERNATE;
  }
  public void setRel(String v) { rel = v; }

  /** MIME type of the link target. */
  protected String type;
  public String getType() { return type; }
  public void setType(String v) { type = v; }

  /** Link URI. */
  protected String href;
  public String getHref() { return href; }
  public void setHref(String v) { href = v; }

  /** Language of resource pointed to by href. */
  protected String hrefLang;
  public String getHrefLang() { return hrefLang; }
  public void setHrefLang(String v) { hrefLang = v; }

  /** Link title. */
  protected String title;
  public String getTitle() { return title; }
  public void setTitle(String v) { title = v; }

  /** Language of link title. */
  protected String titleLang;
  public String getTitleLang() { return titleLang; }
  public void setTitleLang(String v) { titleLang = v; }

  /** Length of the resource pointed to by href, in bytes. */
  protected long length = -1;
  public long getLength() { return length; }
  public void setLength(long v) { length = v; }

  /** Nested atom:content element or {@code null} if no inlined link content. */
  protected Content content = null;
  public Content getContent() { return content; }
  public void setContent(Content c) { this.content = c; }
  
  /** Etag of linked resource, or {@code null} if unknown */
  protected String etag = null;
  public String getEtag() { return etag; }
  public void setEtag(String v) { etag = v; }
  
  /**
   * Returns whether this link matches the given {@code rel} and {@code type}
   * values.
   *
   * @param relToMatch  {@code rel} value to match or {@code null} to match any
   *                    {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *                    {@code type} value.
   */
  public boolean matches(String relToMatch, String typeToMatch) {
    return (relToMatch == null || relToMatch.equals(getRel()))
        && (typeToMatch == null || typeToMatch.equals(this.type));
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile p,
      String namespace, String localName, Attributes attrs) {
    return new AtomHandler(p);
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile p) throws IOException {
    generateAtom(w, p);
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            Output writer.
   *
   * @param   extProfile
   *            Extension profile.
   *
   * @throws  IOException
   */
  public void generateAtom(XmlWriter w,
                           ExtensionProfile extProfile) throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(3);
    List<XmlWriter.Namespace> nsDecls =
      new ArrayList<XmlWriter.Namespace>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (type != null) {
      attrs.add(new XmlWriter.Attribute("type", type));
    }

    if (href != null) {
      attrs.add(new XmlWriter.Attribute("href", href));
    }

    if (hrefLang != null) {
      attrs.add(new XmlWriter.Attribute("hreflang", hrefLang));
    }

    if (title != null) {
      attrs.add(new XmlWriter.Attribute("title", title));
    }

    if (titleLang != null) {
      attrs.add(new XmlWriter.Attribute("xml:lang", titleLang));
    }

    if (length != -1) {
      attrs.add(new XmlWriter.Attribute("length", String.valueOf(length)));
    }
    
    if (etag != null) {
      nsDecls.add(Namespaces.gNs);
      attrs.add(new XmlWriter.Attribute(Namespaces.gAlias, "etag", etag));
    }

    generateStartElement(w, Namespaces.atomNs, "link", attrs, nsDecls);

    if (content != null) {
      content.generateAtom(w, extProfile);
    }
    
    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.atomNs, "link");
  }

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            Output writer.
   *
   * @throws  IOException
   */
  public void generateRss(XmlWriter w) throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(3);

    if (rel != null && rel.equals("enclosure")) {

      if (type != null) {
        attrs.add(new XmlWriter.Attribute("type", type));
      }

      if (href != null) {
        attrs.add(new XmlWriter.Attribute("url", href));
      }

      if (length != -1) {
        attrs.add(new XmlWriter.Attribute("length", String.valueOf(length)));
      }

      w.simpleElement(Namespaces.rssNs, "enclosure", attrs, null);

    } else if ("comments".equals(rel)) {
      w.simpleElement(Namespaces.rssNs, "comments", null, href);
    } else if (Rel.ALTERNATE.equals(rel)) {
      w.simpleElement(Namespaces.rssNs, "link", null, href);
    } else if (Rel.VIA.equals(rel))  {

      if (href != null) {
        attrs.add(new XmlWriter.Attribute("url", href));
        w.simpleElement(Namespaces.rssNs, "source", attrs, null);
      }
    }
  }

  /** &lt;atom:link&gt; parser. */
  public class AtomHandler extends ExtensionPoint.ExtensionHandler {

    /**
     * {@code true} if the 'href' attribute is required.
     */
    private final boolean linkRequired;

    public AtomHandler(ExtensionProfile extProfile) {
      super(extProfile, Link.class);
      linkRequired = true;
    }

    protected AtomHandler(ExtensionProfile extProfile,
        Class<? extends Link> extendedClass) {
      super(extProfile, extendedClass);
      linkRequired = false;
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("type")) {
          type = value;
        } else if (localName.equals("href")) {
          href = getAbsoluteUri(value);
        } else if (localName.equals("hreflang")) {
          hrefLang = value;
        } else if (localName.equals("title")) {
          title = value;
        } else if (localName.equals("length")) {
          try {
            length = Integer.valueOf(value).longValue();
          } catch (NumberFormatException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.lengthNotInteger);
          }
        }
      } else if (namespace.equals(Namespaces.g)) {
        if (localName.equals("etag")) {
          etag = value;
        }
      }
    }

    @Override
    public ElementHandler getChildHandler(String namespace, String localName,
        Attributes attrs) throws ParseException, IOException {
      
      if (namespace.equals(Namespaces.atom)) {
        if (localName.equals("content")) {
          if (content != null) {
            throw new ParseException(CoreErrorDomain.ERR.duplicateContent);
          }
  
          Content.ChildHandlerInfo chi =
              Content.getChildHandler(extProfile, attrs);
  
          content = chi.content;
          return chi.handler;
        }
      }
      
      return super.getChildHandler(namespace, localName, attrs);
    }
    
    @Override
    public void processEndElement() throws ParseException {

      if (linkRequired && href == null) {
        throw new ParseException(
          CoreErrorDomain.ERR.missingHrefAttribute);
      }

      titleLang = xmlLang;

      // Allow undefined content.
    }
  }
}
