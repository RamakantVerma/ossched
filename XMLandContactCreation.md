The following is an XML code fragment, which will be used as an example for the purposes of explaining the parameters and different parameter options for the creation of a Contact “kind”. The code contains several different parameters which are specific to different details about this contact.

_source for code fragment_: http://code.google.com/apis/gdata/elements.html

```
<entry xmlns='http://www.w3.org/2005/Atom' xmlns:gd='http://schemas.google.com/g/2005'>
  <category scheme='http://schemas.google.com/g/2005#kind' 
      term='http://schemas.google.com/contact/2008#contact'/>
  <title>Elizabeth Bennet</title>
  <content>My good friend, Liz.  A little quick to judge sometimes, but nice girl.</content>
  <gd:email rel='http://schemas.google.com/g/2005#work' primary='true' address='liz@gmail.com'/>
  <gd:email rel='http://schemas.google.com/g/2005#home' address='liz@example.org'/>
  <gd:phoneNumber rel='http://schemas.google.com/g/2005#work' primary='true'>
    (206)555-1212
  </gd:phoneNumber>
  <gd:phoneNumber rel='http://schemas.google.com/g/2005#home'>
    (206)555-1213
  </gd:phoneNumber>
  <gd:phoneNumber rel='http://schemas.google.com/g/2005#mobile'>
    (206) 555-1212
  </gd:phoneNumber>
  <gd:im rel='http://schemas.google.com/g/2005#home' 
      protocol='http://schemas.google.com/g/2005#GOOGLE_TALK' 
      address='liz@gmail.com'/>
  <gd:postalAddress rel='http://schemas.google.com/g/2005#work' primary='true'>
    1600 Amphitheatre Pkwy 
    Mountain View, CA 94043
  </gd:postalAddress>
  <gd:postalAddress rel='http://schemas.google.com/g/2005#home'>
    800 Main Street
    Mountain View, CA 94041
  </gd:postalAddress>
  <gd:organization>
    <gd:orgName>Google, Inc.</gd:orgName>
    <gd:orgTitle>Tech Writer</gd:orgTitle>
  </gd:organization>
</entry>
```

In the 2nd line, we can see that the category scheme for this entry is of the GData “kind” and the next line (`term=`) indicates that this entry is a kind is of type Contact:

```
<category scheme='http://schemas.google.com/g/2005#kind' term='http://schemas.google.com/contact/2008#contact'/>
```

The next line is the `<title>` field which indicates the contact's name. The field is ended using the `</title>` tag.

Another field that we can see is the e-mail field (6th line):
```
<gd:email rel='http://schemas.google.com/g/2005#work' primary='true' address='liz@gmail.com'/>
```

_(note the 'gd:' alias attached to the parameter indicating that it's part of the Google namespace)_

The value for “rel=” simply defines the different options for that particular parameter. The value for “rel=” in the case of the e-mail address specifies if this is going to be a work e-mail address or a home e-mail address etc (work e-mail address in this case). Another option for the e-mail address is whether or not this will be a primary e-mail address (as indicated by `primary=’true’`). The different options for the different parameters are all defined at [Common Elements: "Kinds" - Google Data APIs](http://code.google.com/apis/gdata/elements.html). Having understood up to this point makes it easy to figure out what the rest of the code is storing. It stores home and work telephone numbers, instant messenger information, postal address for both work and home, and job related information.

It is important to note that we say that this code fragment is "storing" the information and not displaying it or anything like that (such as in HTML) because XML doesn't actually "do" anything. It is merely a structure for storing data for the purposes of transport.