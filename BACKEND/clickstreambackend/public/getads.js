

let AD_DATA = []; 


async function fetchData() {
    try {
        const response = await fetch('/ads/getallads', {
            method: 'GET'
        });
        const fetchedData = await response.json();
        AD_DATA = fetchedData;
        console.log(AD_DATA);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function initialize() {
    await fetchData();
}
function getRandomAd(array=AD_DATA) {
    const randomIndex = Math.floor(Math.random() * array.length);
    return array[randomIndex];
}
// console.log(getRandomAd())

function updateDisplay(data) {
    // Get the elements you want to update
    const adCard = document.getElementById('adCard');
    const imageElement = adCard.querySelector('.card-img-top');
    const titleElement = adCard.querySelector('.card-title');
    const textElement = adCard.querySelector('.card-text');

    // Update the HTML content with the values from the data
    imageElement.src = data.adimage;
    titleElement.innerText = data.adcontent;
    textElement.innerText = "This is the ad content. You can customize it based on your data.";
}