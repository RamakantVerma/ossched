In order to interact with the calendar and utilize its functions, the client program would have to first authenticate itself to a Google account. For the purposes of authentication, two methods could be used and the method employed is dependent on the type of client being used. For a standalone program, it’s suggested that a "Client Login" method be used. In this case, the application would take in the user’s login information and send it to Google for authentication and then a token for a session would be issued to the application if the login was successful. For web applications the AuthSub authentication method is suggested. With this approach, the user is directed to a website where they would then login. If the login is successful, the AuthSub system redirects to the next URL as specified in the "next" parameter for the AuthSub request. Appended to that URL would be the token for that authentication.


For our purposes, since we are dealing with a web based application, it looks like we would be using the AuthSub method.


Some common things that you might want to do with the calendar:
  * Access / pull up one of your personal calendars
    * Uses the HTTP GET command
  * Create a new calendar
    * Uses the HTTP POST command
  * Update an existing calendar
    * Uses the HTTP PUT command
  * Delete a calendar
    * Uses the HTTP DELETE command


All of these methods are used in conjunction with an XML format that would have all the necessary entities for the purposes of completing the request.


Calendars types in Google Calendar:
  * _Primary Calendar:_ Created at time user signs up for Google calendar
  * _Secondary Calendar:_ Additional calendars a user makes
  * _Imported Calendar:_ calendars a user subscribes to


In order to view someone else’s calendar, you must be a subscriber of that calendar, at which time it would be available in the users "Imported Calendars" list.


In addition to subscribing to someone else’s calendar, you can also share calendars. When sharing calendars, it’s a good idea to have some kind of access control in order to control who can modify the entries in the calendar and who may only read those entries. For this purpose, there exists an Access Control Lists (or ACL) which define different "roles" for users sharing the calendar. The ACL’s have a couple required elements. First, the "scope" parameter indicates to what the new rule will apply to, whether it be a user, or a group of users registered to a certain domain (eg. @gmail.com), or a "default" value which means that the rule would apply to all users. Some of the "roles" associated with the rule being defined include:
  * _read:_ user can only read the calendar)
  * _owner:_ owner of the calendar with full privileges
  * _editor:_ can fully edit the calendar but cannot change any access control settings
  * _free/busy:_ user can only see whether the user is free or busy for a timeslot. That is, the user does not see the specific details for the event, if for that timeslot they are "busy"