

let adData = []; 


async function fetchData() {
    try {
        const response = await fetch('/ads/getallads', {
            method: 'GET'
        });
        const fetchedData = await response.json();
        adData = fetchedData;
        console.log(adData);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function initialize() {
    await fetchData();
}
