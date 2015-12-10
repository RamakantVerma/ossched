### Google Data (GData) ###
  * Standard protocol for reading/writing data on the web
  * It can use either of 2 web syndication formats

As per Wikipedia (http://en.wikipedia.org/wiki/Web_syndication) "_Web syndication_ is a form of syndication in which website material is made available to multiple other sites. Most commonly, web syndication refers to making web feeds available from a site in order to provide other people with a summary of the website's recently added content"

For our purposes, the web feeds that are referred to would be the Calendars and their entries.

There are 2 primary forms of web syndication - Atom & RSS (both XML-based):
  * RSS - older/been around longer
  * Atom - Most recent, more features
    * Provides the Atom Publishing Protocol (HTTP protocol for editing/publishing web resources). The APP allows the use of HTTP commands (GET, POST etc...) and is returned information in the syndication format that is being used.

GData uses Atom 1.0 and RSS 2.0 as well as the Atom Publishing Protocol (APP).
Atom provides mechanism extensions to extend the functionality of the protocol according to developer needs. GData uses these extensions to allow queries.

**XML and HTML:**

XML (Extensible Markup Language) and HTML are not the same. They were created with different goals. The focus of HTML is display data and the focus of XML is to structure and organize data for the purposes of transport. XML doesn’t actually "do" anything.

**Data Formats:**

To manage the information that is contained for the different APIs, Google has created a collection of elements that are common to some applications. Since there are different applications that manage the same type of information, they have created their own standard for this data type (called kinds). There are 3 “kinds” associated with the GData protocol and they are: the Contact kind, the Event kind, and the Message kind, each with their own data structure in order to organize entry information. Some of the entries are just common ATOM and RSS elements, while the others are part of the Google data namespace.

The Gdata namespace reference (http://code.google.com/apis/gdata/elements.html#gdReference) lists the entries used by the "kinds" types as well as additional data entries for the managing of data. In XML code, the Google data namespace is referred to using the "gd" alias. For example, for contact information such as a telephone number, we would refer to it in XML code with the following structure:  

&lt;gd:phoneNumber&gt;

