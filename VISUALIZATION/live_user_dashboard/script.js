// Data from your provided JSON
const url = "http://20.193.151.118:9096/api/datas/mongo/agg";
const liveUsersCount = document.querySelector(".live-users__heading");
const main = document.querySelector(".main");
const loader = document.querySelector(".loadingspinner");
const ctx = document.getElementById("myChart");
const ctx1 = document.getElementById("donetChart");

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
    console.log("hello");
  }

  return updateChart(data);
};

const updateChart = (data) => {
  updateLiveUsers(data);
  const timestamp = data.map((item) => new Date(item._id.start.$date));
  const hours = timestamp.map((item) => item.getUTCHours());
  new Chart(ctx1, {
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

  new Chart(ctx, {
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

// setInterval(getData, 2000);
