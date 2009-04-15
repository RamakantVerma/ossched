/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ossched;

import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class CalendarConnection {

    private CalendarService myService;

    public CalendarConnection(String email, String password) {
        myService = new CalendarService("exampleCo-exampleApp-1.0");
        try {
            myService.setUserCredentials(email, password);
        } catch (AuthenticationException ex) {

        }
    }

    public void createEvent(String name, Date startDate, Double duration) {

        URL eventFeedUrl = null;
        try {
            eventFeedUrl = new URL("http://www.google.com/calendar/feeds/default/private/full");
        } catch (MalformedURLException ex) {

        }

        CalendarEventEntry newEntry = new CalendarEventEntry();

        newEntry.setTitle(new PlainTextConstruct(name));
        //newEntry.setContent(new PlainTextConstruct(startDate.toString() + endDate.toString()));

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(startDate);
        Double durationInMinutes = duration * 60;
        end.add(Calendar.MINUTE, durationInMinutes.intValue());

        String year = Integer.toString(start.get(Calendar.YEAR));
        String month = Integer.toString(start.get(Calendar.MONTH) + 1);
        if (month.length() == 1){
            month = "0" + month;
        }
        String day = Integer.toString(start.get(Calendar.DATE));
        if (day.length() == 1){
            day = "0" + day;
        }
        String hour = Integer.toString(start.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1){
            hour = "0" + hour;
        }
        String minute = Integer.toString(start.get(Calendar.MINUTE));
        if (minute.length() == 1){
            minute = "0" + minute;
        }
        String second = Integer.toString(start.get(Calendar.SECOND));
        if (second.length() == 1){
            second = "0" + second;
        }

        String startDateString = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;

        year = Integer.toString(end.get(Calendar.YEAR));
        month = Integer.toString(end.get(Calendar.MONTH) + 1);
        if (month.length() == 1){
            month = "0" + month;
        }
        day = Integer.toString(end.get(Calendar.DATE));
        if (day.length() == 1){
            day = "0" + day;
        }
        hour = Integer.toString(end.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1){
            hour = "0" + hour;
        }
        minute = Integer.toString(end.get(Calendar.MINUTE));
        if (minute.length() == 1){
            minute = "0" + minute;
        }
        second = Integer.toString(end.get(Calendar.SECOND));
        if (second.length() == 1){
            second = "0" + second;
        }

        String endDateString = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
        
        DateTime startDateTime = DateTime.parseDateTime(startDateString);
        DateTime endDateTime = DateTime.parseDateTime(endDateString);

        When eventTimes = new When();
        eventTimes.setStartTime(startDateTime);
        eventTimes.setEndTime(endDateTime);

        newEntry.addTime(eventTimes);

        try {
            CalendarEventEntry insertedEntry = myService.insert(eventFeedUrl, newEntry);
        } catch (IOException ex) {
            Logger.getLogger(CalendarConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(CalendarConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void deleteAllEvents() throws ServiceException, IOException {

        List<CalendarEventEntry> eventsToDelete = new ArrayList<CalendarEventEntry>();
        eventsToDelete = this.getAllEvents();

        if (!eventsToDelete.isEmpty()) {
            if (eventsToDelete.get(0) != null){
                for (int i = 0; i < eventsToDelete.size(); i++){
                    URL deleteUrl = new URL(eventsToDelete.get(i).getEditLink().getHref());
                    myService.delete(deleteUrl);
                }
            }
        }
    
    }

    public List<CalendarEventEntry> getAllEvents() {

        URL eventFeedUrl = null;
        try {
            eventFeedUrl = new URL("http://www.google.com/calendar/feeds/default/private/full");
        } catch (MalformedURLException ex) {

        }

        CalendarEventFeed eventFeed = null;
        try {
            eventFeed = myService.getFeed(eventFeedUrl, CalendarEventFeed.class);
        } catch (IOException ex) {

        } catch (ServiceException ex) {

        }

        List<CalendarEventEntry> output = new ArrayList<CalendarEventEntry>();

        for (int i = 0; i < eventFeed.getEntries().size(); i++) {
          CalendarEventEntry event = eventFeed.getEntries().get(i);
          output.add(event);
        }

        return output;
    }

}
