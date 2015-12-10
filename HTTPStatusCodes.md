Since we are dealing with HTTP requests, it is a good idea to know what kinds of messages the HTTP protocol would be generating when we interact with it. The following table lists the different status codes that could be generated during the interaction using the HTTP protocol as well as a short description of what each of them means.

| **Code:** | **Explanation** |
|:----------|:----------------|
| 200 OK    | No error        |
| 201 CREATED | Creation of a resource was successful |
| 304 NOT MODIFIED | The resource hasn't changed since the time specified in the request's If-Modified-Since header |
| 400 BAD REQUEST | Invalid request URI or header, or unsupported nonstandard parameter |
| 401 UNAUTHORIZED | Authorization required |
| 403 FORBIDDEN | Unsupported standard parameter, or authentication or authorization failed |
| 404 NOT FOUND | Resource (such as a feed or entry) not found |
| 409 CONFLICT | Specified version number doesn't match resource's latest version number |
| 500 INTERNAL SERVER ERROR | Internal error. This is the default code that is used for all unrecognized errors |

Source: http://code.google.com/apis/gdata/reference.html