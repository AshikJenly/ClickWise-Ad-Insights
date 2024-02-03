// Data from your provided JSON
const url = "http://localhost:9096/api/datas/mongo/agg";
const liveUsersCount = document.querySelector(".live-users__heading");
const main = document.querySelector(".main");
const loader = document.querySelector(".loadingspinner");
const ctx = document.getElementById("myChart");
const ctx1 = document.getElementById("donetChart");
var chart1;
var chart2;
const hideLoader = () => {
  loader.classList.add("hide");
  main.classList.remove("hide");
};
const updateLiveUsers = (data) => {
  
  const liveUser = data[data.length - 1].total_users_visited;
  //   if (liveUser < 10) {
  //     liveUsersCount.innerHTML = `10+`;
  //     return;
  //   }
  liveUsersCount.innerHTML = `${liveUser}+`;
};

const getData = async () => {

  const res = await fetch(url);
  const data = await res.json();
  if (data) {
    hideLoader();
    console.log('hello');
  }

  return updateChart(data);
};

const updateChart = (data) => {
if(chart1){
  chart1.destroy();
  chart2.destroy();
}
  
  updateLiveUsers(data);
  const timestamp = data.map((item) => new Date(item._id.start.$date));
  const hours = timestamp.map((item) => item.getUTCHours());
   chart1=new Chart(ctx1, {
    type: "doughnut",
    data: {
      labels: ["live users", "unique users"],
      datasets: [
        {
          label: "No. of users",
          data: [
            data[data.length - 1].total_users_visited,
            data[data.length - 2].total_unique_users_visited,
          ],
          borderWidth: 1,
        },
      ],
    },
    options: {
      
      animation: {
        duration: 2000, // Animation duration in milliseconds
        easing: 'easeInOutQuart', // Easing function for the animation (optional)
    },
      responsive: true,
      plugins: {
        legend: {
          position: "top",
        },
        title: {
          display: true,
          text: "Live Chart",
        },
      },
    },
  });

 chart2 = new Chart(ctx, {
    type: "bar",
    data: {
      labels: hours,
      datasets: [
        {
          label: "No. of users",
          data: data.map((item) => item.total_users_visited),
          borderWidth: 1,
          order: 1,
        },
      ],
    },
    options: {
      animation: {
        duration: 2000, // Animation duration in milliseconds
        easing: 'easeInOutQuart', // Easing function for the animation (optional)
    },
      responsive: true,
      plugins: {
        legend: {
          position: "top",
        },
      },
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });
};



setTimeout(() => {
  getData();
}, 1000);

setInterval(getData, 6000);
