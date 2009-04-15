<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Conference</title>
        <style>
            #selectable .ui-selecting {
                background: #f6f6f6 url(../images/skin/timeline_hover.gif) bottom repeat-x;
            }

            #selectable .ui-selected {
                background: #d8e6fb url(../images/skin/timeline_select.gif) bottom repeat-x;
                color: #7392c0;
            }

            #selectable {
                width: 1000px;
                list-style-type: none;
                margin: 0;
                padding: 0;
                font-family: sans-serif;
            }

            #selectable li {
                margin: 1px;
                padding-top: 10px;
                float: left;
                font-weight: normal;
                width: 30px;
                height: 80px;
                font-size: 9pt;
                text-align: center;
            }
        </style>
        <link rel="stylesheet" href="/vo/css/ui.all.css" />
        <g:javascript src="date-functions.js"></g:javascript>
        <g:javascript src="jquery-1.3.2.js"></g:javascript>
        <g:javascript src="ui.core.js"></g:javascript>
        <g:javascript src="ui.datepicker.js"></g:javascript>
        <g:javascript src="ui.selectable.js"></g:javascript>
        <g:javascript>
            $(function() {
                $("#datepicker").datepicker({minDate: 0, dateFormat: 'DD, MM dd, yy'});
            });

            function validatePage(){
                var errorsFound = false;

                var confLength = document.getElementById("lengthOfConference").value.replace(/^\s+|\s+$/g,"");
                if (confLength == "0" || confLength == "0.0" || confLength == ""){
                    alert("Please select a valid time slot");
                    errorsFound = true;
                }

                var numOfAttendees = parseInt(document.getElementById("numberOfAttendees").value.replace(/^\s+|\s+$/g,""));
                if (!(numOfAttendees != null && numOfAttendees > 0)){
                    alert("Please enter a valid number of attendees (must be greater than 0)");
                    errorsFound = true;
                }

                if (!errorsFound){
                    getDates();
                    return true;
                } else {
                    return false;
                }
            }

            function getDates(){
                getStartDate();
                getEndDate();
            }

            function getStartDate(){
                var startDateString = Date.parseDate(document.getElementById("datepicker").value + " " + document.getElementById("startTimeValue").value.replace(/^\s+|\s+$/g,""), "l, F d, Y g:i A");
                var startDate = new Date(startDateString);
                //document.getElementById("startDate").value = startDate;
                document.getElementById("startDate").setAttribute("value", startDate);
            }

            function getEndDate(){
                var endDateString = Date.parseDate(document.getElementById("datepicker").value + " " + document.getElementById("endTimeValue").value.replace(/^\s+|\s+$/g,""), "l, F d, Y g:i A");
                var endDate = new Date(endDateString);
                //document.getElementById("endDate").value = endDate;
                document.getElementById("endDate").setAttribute("value", endDate);
            }

            function getDuration(){
                var startTimeStr = Date.parseDate(document.getElementById("startTimeValue").value.replace(/^\s+|\s+$/g,""), "g:i A");
                var endTimeStr = Date.parseDate(document.getElementById("endTimeValue").value.replace(/^\s+|\s+$/g,""), "g:i A");
                var startTime = new Date(startTimeStr);
                var endTime = new Date(endTimeStr);
                var hrs = Math.abs( (endTime.getHours() * 60 + endTime.getMinutes()) - (startTime.getHours() * 60 + startTime.getMinutes()) ) / 60;
                document.getElementById("lengthOfConference").value = hrs.toString();
            }

            // Format the start date to display in the text box
            function formatStartDate(){
                var startDate = new Date(document.getElementById("startDate").value.replace(/^\s+|\s+$/g,""));
                var formattedStartDate = startDate.dateFormat(Date.patterns.LongDatePattern);
                document.getElementById("datepicker").value = formattedStartDate.toString();
            }

            function getExistingStartTime(){
                var startDateString = document.getElementById("startDate").value.replace(/^\s+|\s+$/g,"");
                var startDate = Date.parseDate(startDateString.substring(0,startDateString.indexOf(".")), "Y-m-d H:i:s");
                var startTime = startDate.dateFormat(Date.patterns.ShortTimePattern);
                document.getElementById("startTimeValue").value = startTime.toString();
                document.getElementById("startDate").value = startDate.toString();
                document.getElementById("endDate").value = startDateString;
            }

            function getExistingEndTime(){
                var endDateString = document.getElementById("endDate").value.replace(/^\s+|\s+$/g,"");
                var tempEndDate = Date.parseDate(endDateString.substring(0,endDateString.indexOf(".")), "Y-m-d H:i:s");
                var duration = document.getElementById("lengthOfConference").value.replace(/^\s+|\s+$/g,"");
                var hours = tempEndDate.getHours();
                var minutes = tempEndDate.getMinutes();
                tempEndDate.setHours(hours, minutes + (duration * 60));
                var endTime = tempEndDate.dateFormat(Date.patterns.ShortTimePattern);
                document.getElementById("endTimeValue").value = endTime.toString();
                document.getElementById("endDate").value = tempEndDate.toString();
            }

            function selectExistingTimeslot(startTime, numOfSlots){
                var container = document.getElementById("selectable");
                var elements = container.getElementsByTagName("li");
                for (var i = 0; i < elements.length; i++){
                    var val = elements[i].innerHTML.replace(/^\s+|\s+$/g,"");
                    if (val == startTime){
                        for (var j = i; j < i + parseInt(numOfSlots); j++){
                            elements[j].setAttribute("class", "ui-state-default ui-selected");
                        }
                    }
                }
                document.getElementById("timeSelected").innerHTML = "Selected time slot: " + document.getElementById("startTimeValue").value + " - " + document.getElementById("endTimeValue").value;
            }

            function showSelectedTimeslot(){
                var container = document.getElementById("selectable");
                var elements = container.getElementsByTagName("li");
                var isSelected = false;
                var message = "No time slot selected.";
                var selectedTimes = [];
                var j = 0;
                for (var i = 0; i < elements.length; i++){
                    if (elements[i].className.indexOf("ui-selected") >= 0 ){
                        isSelected = true;
                        selectedTimes[j] = i;
                        j = j + 1;
                    }
                }
                if (isSelected){
                    var startTime = elements[selectedTimes[0]].innerHTML.replace(/^\s+|\s+$/g,"");
                    var endTime = "";
                    if ( selectedTimes[selectedTimes.length - 1] == (elements.length - 1) ){
                        endTime = "10:30 PM";
                    } else {
                        endTime = elements[selectedTimes[selectedTimes.length - 1] + 1].innerHTML.replace(/^\s+|\s+$/g,"");
                    }
                    message = "Selected time slot: " + startTime + " - " + endTime;
                }
                document.getElementById("timeSelected").style.color = "#7799bb";
                document.getElementById("timeSelected").innerHTML = message;
                document.getElementById("startTimeValue").value = startTime;
                document.getElementById("endTimeValue").value = endTime;
                getDuration();
            }
        </g:javascript>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Conference List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Conference</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${conference}">
            <div class="errors">
                <g:renderErrors bean="${conference}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" onsubmit="return validatePage()" >
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
                <div style="height: 5px;"></div>
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="conferenceName">Conference Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'conferenceName','errors')}">
                                    <input type="text" id="conferenceName" name="conferenceName" size="30" value="${fieldValue(bean:conference,field:'conferenceName')}"/>
                                </td>
                            </tr> 
                                                
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="datepicker">Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'startDateTime','errors')}">
                                    <!--<g:datePicker name="startDateTime" value="${conference?.startDateTime}" years="${2008..2020}" precision="day"></g:datePicker>-->
                                    <input type="text" id="datepicker" size="30" readonly="readonly" />
                                </td>
                            </tr> 

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="selectables_container">Time Slot:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'startDateTime','errors')}">
                                    <table cellpadding="0" cellspacing="0" style="border:none;">
                                        <tr>
                                        <td style="padding:0px;">
                                            <ol id="selectable">
                                                <li class="ui-state-default">6:00 AM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">6:30 AM</li>
                                                <li class="ui-state-default">7:00 AM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">7:30 AM</li>
                                                <li class="ui-state-default">8:00 AM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">8:30 AM</li>
                                                <li class="ui-state-default">9:00 AM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">9:30 AM</li>
                                                <li class="ui-state-default">10:00 AM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">10:30 AM</li>
                                                <li class="ui-state-default">11:00 AM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">11:30 AM</li>
                                                <li class="ui-state-default">12:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">12:30 PM</li>
                                                <li class="ui-state-default">1:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">1:30 PM</li>
                                                <li class="ui-state-default">2:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">2:30 PM</li>
                                                <li class="ui-state-default">3:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">3:30 PM</li>
                                                <li class="ui-state-default">4:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">4:30 PM</li>
                                                <li class="ui-state-default">5:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">5:30 PM</li>
                                                <li class="ui-state-default">6:00 PM</li>
                                                <li class="ui-state-default" style="font-size: 7pt;">6:30 PM</li>
                                            </ol>
                                        </td>
                                        </tr>
                                        <tr>
                                            <td width="100%" style="text-align:center;">
                                                <div id="timeSelected" style="color:#7799bb; font-weight:bold;">No time slot selected.</div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                           
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lengthOfConference">Length Of Conference:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'lengthOfConference','errors')}">
                                    <!--<g:select id="lengthOfConference" name="lengthOfConference" from="${conference.constraints.lengthOfConference.inList.collect{it.encodeAsHTML()}}" value="${fieldValue(bean:conference,field:'lengthOfConference')}" ></g:select>-->
                                	<g:textField id="lengthOfConference" name="lengthOfConference" size="3" readonly="readonly" />
                                    hour(s).
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="numberOfAttendees">Number Of Attendees:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'numberOfAttendees','errors')}">
                                    <input type="text" id="numberOfAttendees" name="numberOfAttendees" size="3" value="${fieldValue(bean:conference,field:'numberOfAttendees')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="email">Booked By:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'email','errors')}">
                                    ${conference?.email}
                                </td>
                            </tr> 
                                                
                            <!--tr class="prop">
                                <td valign="top" class="name">
                                    <label for="owner">Owner:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conference,field:'owner','errors')}">
                                    ${conference?.fullname}
                                </td>
                            </tr--> 
                        
                        </tbody>
                    </table>
                </div>
                <g:hiddenField name="startTimeValue" />
                <g:hiddenField name="endTimeValue" />
                <g:hiddenField name="startDate" value="${conference?.startDateTime}" />
                <g:hiddenField name="endDate" />
            </g:form>
        </div>

        <g:render template="instructions" />
        
        <g:javascript>
            $(function() {
                $("#selectable").selectable({
                    stop: function(){
                        showSelectedTimeslot();
                    }
                });
            formatStartDate();
            });
        </g:javascript>
        
    </body>
</html>