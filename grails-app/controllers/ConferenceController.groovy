import org.bigbluebutton.domain.Conference

import org.ossched.CalendarConnection
import com.google.gdata.data.calendar.*;
import java.util.Formatter.DateTime
import java.util.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class ConferenceController extends BaseController {

    def beforeInterceptor = [action:this.&auth]
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        def email = session.email.toString()
        
        // Beginning of modified code
        def outputlist = []

        if (params.past) {
            outputlist = Conference.findAllByEmailAndStartDateTimeLessThan(email, new Date())
        } else {
            outputlist = Conference.findAllByEmailAndStartDateTimeGreaterThanEquals(email, new Date() - 1)
        }
        
        def calendarConnection = new CalendarConnection("username@gmail.com", "password")

        calendarConnection.deleteAllEvents()

        for (def i = 0; i < outputlist.size; i++){
            calendarConnection.createEvent(outputlist[i].conferenceName, outputlist[i].startDateTime, outputlist[i].lengthOfConference)
        }

        return [ conferenceList: outputlist]
        // End of modified code
    }

    def show = {
        def conference = Conference.get( params.id )

        if(!conference) {
            flash.message = "Conference not found with id ${params.id}"
            redirect(action:list)
        }
        else { 
			def startTime = conference.startDateTime
			def endTime = new Date(new Long(conference.startDateTime.time) + new Long(conference.lengthOfConference.intValue())*60*60*1000)
        	//def endTime = conference.endDateTime
            def attendeesList =
        		Attendees.findAllByConferenceNumberAndDateJoinedBetween(conference.conferenceNumber, startTime, endTime)
        	return [ conference : conference, attendeesList : attendeesList ] 
        }
    }

    def delete = {
        def conference = Conference.get( params.id )
        if(conference) {
            conference.delete()
            flash.message = "${conference.conferenceName} has been deleted."
            redirect(action:list)
        }
        else {
            flash.message = "Cannot find conference."
            redirect(action:list)
        }
    }

    def edit = {
        def conference = Conference.get( params.id )

        if(!conference) {
            flash.message = "Cannot find conference ${conference.conferenceName}."
            redirect(action:list)
        }
        else {
            return [ conference : conference ]
        }
    }

    def update = {
        def conference = Conference.get( params.id )
        if(conference) {
            conference.properties = params
            def startTime = new Date(params.startDate)
            conference.startDateTime = startTime
            //def endTime = new Date(params.endDate)
            //conference.endDateTime = endTime
            if(!conference.hasErrors() && conference.save()) {
                flash.message = "The conference has been updated."
                redirect(action:show,id:conference.id)
            }
            else {
                render(view:'edit',model:[conference:conference])
            }
        }
        else {
            flash.message = "Conference not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def conference = new Conference()
        conference.properties = params     
        conference.email = session.email
        conference.fullname = session.fullname
        def now = new Date()
        conference.conferenceName = "$now Conference"   
        return ['conference':conference]
    }

    def save = {
        def conference = new Conference(params)
        def highestConfId = Conference.listOrderByConferenceNumber(max:1, order:"desc")
        def nextConfId
        if (highestConfId) {
            nextConfId = highestConfId[0].conferenceNumber + 1
        } else {
            nextConfId = 8000 + 1
        }
        conference.conferenceNumber = nextConfId
        conference.email = session.email
        conference.fullname = session.fullname
        def startTime = new Date(params.startDate)
        conference.startDateTime = startTime
        //def endTime = new Date(params.endDate)
        //conference.endDateTime = endTime
        if(!conference.hasErrors() && conference.save()) {
            flash.message = "You have successfully created a conference."
            redirect(action:show,id:conference.id)
        }
        else {
            render(view:'create',model:[conference:conference])
        }
    }
}