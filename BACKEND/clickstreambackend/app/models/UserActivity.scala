package models
import java.util.UUID


case class UserActivity(
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
