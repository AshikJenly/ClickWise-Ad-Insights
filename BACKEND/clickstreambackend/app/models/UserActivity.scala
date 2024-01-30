package models

import java.time.Instant

case class UserActivity(
    event_timestamp:Instant,    
    userId: String,
    sessionId: String,
    pageUrl: String,
    deviceType: String,
    browser: String,
    geoLocation: String,
    eventType: String,
    adClicked: Boolean,
    adId:String,
    durationSeconds: Int
)
