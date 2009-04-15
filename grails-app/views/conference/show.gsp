

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Conference</title>
        <link rel="stylesheet" href="/vo/css/ui.all.css" />
        <g:javascript src="date-functions.js"></g:javascript>
        <g:javascript src="jquery-1.3.2.js"></g:javascript>
        <g:javascript src="ui.core.js"></g:javascript>
        <g:javascript src="ui.dialog.js"></g:javascript>
        <g:javascript>
            $.ui.dialog.defaults.bgiframe = true;
            $(function() {
                $("#emailBody").dialog({ autoOpen: false, width: 680 });
            });

            function showInvitationText(){
                $("#emailBody").dialog("open");
            }
        </g:javascript>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Conference List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Conference</g:link></span>
        </div>
        <div class="body">
            <h1>Show Conference</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>   
            
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${conference?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
            <div style="height: 5px;"></div>
            <div class="dialog">
                <table>
                    <tbody>            
                        <tr class="prop">
                            <td valign="top" class="name">Conference Name:</td>
                            
                            <td valign="top" class="value">${conference.conferenceName}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Conference ID:</td>
                            
                            <td valign="top" class="value">${conference.conferenceNumber}#</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Start Date Time:</td>
                            
                            <td valign="top" class="value"><g:formatDate format="EEE, d MMMM yyyy 'at' h:mm a" date="${conference.startDateTime}"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Length Of Conference:</td>
                            
                            <td valign="top" class="value">${conference.lengthOfConference} hour(s)</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Number Of Attendees:</td>
                            
                            <td valign="top" class="value">${conference.numberOfAttendees}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Booked By:</td>
                            
                            <td valign="top" class="value">${conference.email}</td>
                            
                        </tr>
                    
                        <!--tr class="prop">
                            <td valign="top" class="name">Date Created:</td>
                            
                            <td valign="top" class="value">${conference.dateCreated}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Last Updated:</td>
                            
                            <td valign="top" class="value">${conference.lastUpdated}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Owner:</td>
                            
                            <td valign="top" class="value">${conference?.fullname}</td>
                            
                        </tr-->
                        <tr class="prop">
                            <td valign="top" class="name">Invitation:</td>
                            
                            <td valign="top" class="value">
                                <div id="emailLink"></div><br><br>
                                <div id="invitationText"><a href="javascript:showInvitationText()">Show Invitation Text</a></div>
                                <g:hiddenField name="confName" value="${conference.conferenceName}"></g:hiddenField>
                                <g:hiddenField name="confId" value="${conference.conferenceNumber}"></g:hiddenField>
                                <g:hiddenField name="emailBCC" value="${session.email}"></g:hiddenField>
                                <div id="confDate" style="display:none;"><g:formatDate format="EEE, d MMMM yyyy 'at' h:mm a" date="${conference.startDateTime}"/></div>
                                
                                <div id="emailBody" title="Invitation Text" style="width:600px">
                                    <p class="doublespace">
                                        Please join me for a conference call to discuss (insert topic)
                                        on <g:formatDate format="EEE, d MMMM yyyy 'at' h:mm a" date="${conference.startDateTime}"/>.
                                    <p>
                                    <p class="doublespace">
                                        Please RSVP at your earliest convenience.
                                    </p>

                                    <p class="doublespace">
                                        <b>Below is the information you will need to join the conference:</b>
                                    <p>
                                    <p class="doublespace">
                                    <ul class="none">
                                      <li>Phone Number: 613-731-1141 </li>
                                      <li>Conference Number: ${conference.conferenceNumber}# (ensure the digits are followed by the # sign)</li>
                                    </ul>
                                    </p>
                                    <p class="doublespace">
                                        I look forward to speaking with you.
                                    </p>
                                    <p class="doublespace">
                                        Kindest regards,
                                    <p>
                                    <p class="doublespace">(insert name)</p>
                                </div>
                                
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
        </div>
<!--
        <div class="body">
            <h1>Attendees List</h1>
            <div class="list">
                <table>
                    <thead>
                        <tr>                       
                   	        <g:sortableColumn property="callerName" title="Caller Name" />                        
                   	        <g:sortableColumn property="callerNumber" title="Caller Number" />
                   	        <g:sortableColumn property="dateJoined" title="Date Joined" />
                        	<g:sortableColumn property="dateLeft" title="Date Left" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${attendeesList}" status="i" var="attendees">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>${attendees.callerName?.encodeAsHTML()}</td>
                        
                            <td>${attendees.callerNumber?.encodeAsHTML()}</td>
                        
                            <td>${attendees.dateJoined?.encodeAsHTML()}</td>
                        	<td>${attendees.dateLeft?.encodeAsHTML()}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
-->
		<g:render template="instructions" />

        <g:javascript>
            var confId = document.getElementById("confId").value;
            var confName = document.getElementById("confName").value;
            var confDate = document.getElementById("confDate").innerHTML;
            //var confDateFormatted = confDate.dateFormat(Date.patterns.LongDatePattern);
            var emailBCC = document.getElementById("emailBCC").value;
            var emailBody = "Please join me for a conference call to discuss (insert topic) on " + confDate + "%0D%0A%0D%0A";
            emailBody = emailBody + "Please RSVP at your earliest convenience." + "%0D%0A%0D%0A";
            emailBody = emailBody + "Below is the information you will need to join the conference:" + "%0D%0A";
            emailBody = emailBody + "Phone Number: 613-731-1141" + "%0D%0A";
            emailBody = emailBody + "Conference Number: " + confId + "%23 (ensure the digits are followed by the %23 sign)" + "%0D%0A%0D%0A";
            emailBody = emailBody + "I look forward to speaking with you." + "%0D%0A%0D%0A";
            emailBody = emailBody + "Kindest regards," + "%0D%0A%0D%0A";
            emailBody = emailBody + "(insert name)";
            var emailLink = "<a href='mailto:?bcc=";
            emailLink = emailLink + emailBCC + "&subject=Conference " + confId.toString() + "%23 - " + confName + "&body=" + emailBody + "'>Send Invitation Email</a>";
            document.getElementById("emailLink").innerHTML = emailLink;
        </g:javascript>
    </body>
</html>
