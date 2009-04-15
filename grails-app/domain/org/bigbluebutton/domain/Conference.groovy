package org.bigbluebutton.domain

class Conference implements Comparable {
	Date dateCreated
	Date lastUpdated
	String conferenceName
	Integer conferenceNumber
	Integer numberOfAttendees = new Integer(3)
	Date startDateTime = new Date()
	Double lengthOfConference
	String email
	String fullname
			
	static constraints = {
		conferenceName(maxLength:50, blank:false)
		conferenceNumber(maxLength:10, unique:true, blank:false)
		//lengthOfConference(inList:[0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4])
		numberOfAttendees()
		email(email:true)
	}

    static mapping = {
        attendees type: 'text'
    }
    	
	String toString() {"${this.conferenceName}"}

    int compareTo(obj) {
        obj.id.compareTo(id)
    }

}
