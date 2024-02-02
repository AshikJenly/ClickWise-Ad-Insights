// Fetch data from your MongoDB endpoint (replace with your actual API endpoint)
async function fetchData() {
    try {

        const response = await fetch("mongodb://forspark:J6cznn1Wd7ukY4lNNxqhhyJxVRm4aBIH8WrAqrHspTjbKTW5KvrIEqHdL9SvsyLmZziicHF4M3v8ACDbna0CdA==@forspark.mongo.cosmos.azure.com:10255/click.test?ssl=true&retrywrites=false&replicaSet=globaldb&maxIdleTimeMS=120000&appName=@forspark@");
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

// Update the chart with new data
async function updateChart() {
    const data = await fetchData();

    const labels = data.map(entry => `${entry._id.start.getHours()}:${entry._id.start.getMinutes()}`);
    const totalUsersVisitedData = data.map(entry => entry.total_users_visited);

    const ctx = document.getElementById('myChart').getContext('2d');
    const myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Total Users Visited',
                data: totalUsersVisitedData,
                borderColor: 'rgb(75, 192, 192)',
                borderWidth: 2,
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: [{
                    type: 'linear',
                    position: 'bottom'
                }]
            }
        }
    });
}

// Fetch data and update chart every 5 seconds (adjust the interval as needed)
setInterval(updateChart, 5000);

// Initial chart setup
updateChart();
