const DATA = {
    event_timestamp: Date.now(),
    userId: "a",
    sessionId: 'lkajdi84893jdalkaoooqp',
    pageUrl: "",
    deviceType: "",
    browser: "",
    geoLocation: "",
    eventType: "",
    adClicked: false,
    adId: "",
    durationSeconds: 10
};

let startTime;

function startTracking() {
    startTime = new Date();
}

function stopTracking() {
    if (startTime) {
        const endTime = new Date();
        const timeSpentInSeconds = (endTime - startTime) / 1000;

        // Send the time spent to the server or log it
        DATA.durationSeconds = parseInt(timeSpentInSeconds)
        console.log(`Time spent on the page: ${timeSpentInSeconds} seconds`);
    }
}
window.addEventListener('load', startTracking);

function postData(isAd = false) {
    stopTracking()
    console.log(DATA);
    fetch('http://20.193.151.118:9096/produce/useractivity', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(DATA)
        })
        .then(response => response.json())
        .then(responseData => {
            console.log(responseData);
            // Handle the response as needed
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateUserActivityData(event_type,ad_clicked,ad_id) {
    DATA.event_timestamp = Date.now()
    DATA.browser = detectBrowser();
    DATA.deviceType = detectDeviceType();
    DATA.geoLocation = Intl.DateTimeFormat().resolvedOptions().timeZone;
    DATA.pageUrl = window.location.href;
    DATA.eventType = event_type;
    DATA.adId = ad_id ;
    DATA.adClicked = ad_clicked;
}

function detectBrowser() {
    let userAgent = navigator.userAgent;
    let browserName;

    if (userAgent.match(/firefox|fxios/i)) {
        browserName = "Firefox";
    } else if (userAgent.match(/opr/i)) {
        browserName = "Opera";
    } else if (userAgent.match(/edg/i)) {
        browserName = "Edge";
    } else if (userAgent.match(/chrome|chromium|crios/i)) {
        browserName = "Chrome";
    } else if (userAgent.match(/safari/i)) {
        browserName = "Safari";
    } else {
        browserName = "Unknown Browser";
    }

    return browserName;
}

function detectDeviceType() {
    let userAgent = navigator.userAgent;
    let deviceType;

    if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(userAgent)) {
        deviceType = "Mobile";
    } else if (/Mac|Windows|Linux/i.test(userAgent)) {
        deviceType = "Laptop/Desktop";
    } else if (/Tablet/i.test(userAgent)) {
        deviceType = "Tablet";
    } else {
        deviceType = "Unknown";
    }

    return deviceType;
}

window.addEventListener('beforeunload',postData);
