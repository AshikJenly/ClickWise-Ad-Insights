const data = {
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


function postData() {
    console.log(data);
    fetch('http://localhost:9096/produce/useractivity', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
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
    data.event_timestamp = Date.now()
    data.browser = detectBrowser();
    data.deviceType = detectDeviceType();
    data.geoLocation = Intl.DateTimeFormat().resolvedOptions().timeZone;
    data.pageUrl = window.location.href;
    data.eventType = event_type;
    data.adId = ad_id ;
    data.adClicked = ad_clicked;
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
