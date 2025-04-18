    function showPage(page) {
        document.getElementById('dashboardContainer').style.display = 'none';
        document.getElementById('transactionContainer').style.display = 'none';
        document.getElementById('paymentsContainer').style.display = 'none';

        document.getElementById(page).style.display = 'block';
    }

    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("transaction-entry-form");

        // Handle Submit (POST)
        document.getElementById("submit-btn").addEventListener("click", function (e) {
            e.preventDefault();

            const data = {
                transactionType: document.getElementById("entry-type").value,
                date: document.getElementById("entry-date").value,
                amount: document.getElementById("entry-amount").value,
                account: document.getElementById("entry-account").value,
                entity: document.getElementById("entry-entity").value,
                category: document.getElementById("entry-category").value,
                details: document.getElementById("entry-description").value,
                person: document.getElementById("entry-person").value,
            };
            console.log("Payload JSON string:", JSON.stringify(data, null, 2));
            fetch("/api/v1/transaction/saveTransaction", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        // Handle error response
                        return response.json().then(err => { throw err });
                    }
                    return response.json();
                })
                .then(data => {
                    alert("Saved successfully!");
                    form.reset();
                })
                .catch(error => {
                    console.error("Error saving data:", error);  // This will show proper error object
                    alert(error.error || "Failed to save data.");  // Show backend message if exists
                });
        });

        // Handle Filter
        document.getElementById("filter-btn").addEventListener("click", function (e) {
            e.preventDefault();

            const params = new URLSearchParams({
                start: document.getElementById("show-start-date").value,
                end: document.getElementById("show-end-date").value,
            });

            console.log("Query Params I'm sending:", params.toString());

            fetch("/api/v1/transaction/getTransactionsByDateRange?" + params.toString())
                .then(response => response.json())
                .then(data => {
                    const tableBody = document.querySelector("#results-table tbody");
                    tableBody.innerHTML = ""; // clear previous results

                    if (data.length === 0) {
                        tableBody.innerHTML = `<tr><td colspan="5" class="text-center">No records found</td></tr>`;
                        return;
                    }
                    console.log("Data from backend:", data);
                    console.log("Data from backend:", JSON.stringify(data, null, 2));
                    data.forEach(item => {
                        const row = `
                                        <tr>
                                            <td>${item.date}</td>
                                            <td>${item.transactionType}</td>
                                            <td>${item.amount}</td>
                                            <td>${item.account}</td>
                                            <td>${item.category}</td>
                                        </tr>
                                    `;
                        tableBody.insertAdjacentHTML("beforeend", row);
                    });
                })
                .catch(error => {
                    console.error("Error fetching data:", error);
                    alert("Failed to fetch data.");
                });
        });

        // Handle Get all transactions
        document.getElementById("get-all-transactions-btn").addEventListener("click", function (e) {
            e.preventDefault();

            fetch("/api/v1/transaction/getAllTransactions")
                .then(response => response.json())
                .then(data => {
                    console.log(data);  // Log the data to inspect its structure
                    const tableBody = document.querySelector("#results-table tbody");
                    tableBody.innerHTML = ""; // clear previous results

                    if (data.length === 0) {
                        tableBody.innerHTML = `<tr><td colspan="5" class="text-center">No records found</td></tr>`;
                        return;
                    }

                    data.forEach(item => {
                        const row = `
                                        <tr>
                                            <td>${item.type}</td>
                                            <td>${item.date}</td>
                                            <td>${item.amount}</td>
                                            <td>${item.name}</td>
                                            <td>${item.category}</td>
                                        </tr>
                                    `;
                        tableBody.insertAdjacentHTML("beforeend", row);
                    });
                })
                .catch(error => {
                    console.error("Error fetching data:", error);
                    alert("Failed to fetch data.");
                });
        });

    });

    const ctx = document.getElementById('chartCanvas').getContext('2d');
    let currentChart;

    const chartsData = {
        category: {
            title: 'Spending by Category',
            type: 'bar',
            labels: ['Food', 'Travel', 'Shopping', 'Bills', 'Entertainment'],
            data: [500, 200, 300, 400, 150],
        },
        merchant: {
            title: 'Top Merchants',
            type: 'pie',
            labels: ['Amazon', 'Walmart', 'Costco', 'Uber', 'Starbucks'],
            data: [800, 600, 500, 300, 200],
        },
        month: {
            title: 'Spending by Month (YTD)',
            type: 'bar',
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep'],
            data: [1200, 1100, 1500, 1300, 1250, 1600, 1400, 1700, 1500],
        }
    };

    function showChart(type) {
        const chartInfo = chartsData[type];
        document.getElementById('chartTitle').innerText = chartInfo.title;

        document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
        event.target.classList.add('active');

        if (currentChart) {
            currentChart.destroy();
        }

        currentChart = new Chart(ctx, {
            type: chartInfo.type,
            data: {
                labels: chartInfo.labels,
                datasets: [{
                    label: chartInfo.title,
                    data: chartInfo.data,
                    backgroundColor: [
                        '#0d6efd', '#6610f2', '#6f42c1', '#d63384', '#fd7e14',
                        '#20c997', '#198754', '#0dcaf0', '#ffc107'
                    ],
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: chartInfo.type === 'pie'
                    }
                }
            }
        });
    }

    function fetchBalance() {
        const month = document.getElementById("monthSelect").value;
        const year = document.getElementById("yearSelect").value;

        if (!month || !year) {
            alert("Please select both month and year.");
            return;
        }

        // Replace with your actual API endpoint
        const url = `/api/v1/transaction/getMonthlyBalance?month=${month}&year=${year}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                const { asankaPaid, divyaPaid, balanceAmount, whoOwes, monthAndYear } = data;

                document.getElementById("asankaPaid").textContent =
                    `Asanka has paid $${asankaPaid} in ${months[month - 1]}, ${year}.`;
                document.getElementById("divyaPaid").textContent =
                    `Divya has paid $${divyaPaid} in ${months[month - 1]}, ${year}.`;

                const balanceText = balanceAmount === 0
                    ? `Both are settled. Balance is $0.`
                    : `${whoOwes} owes $${balanceAmount} to ${whoOwes === 'Asanka' ? 'Divya' : 'Asanka'}`;

                document.getElementById("balanceResult").textContent = balanceText;
                document.getElementById("resultSection").classList.remove("d-none");
            })
            .catch(error => {
                alert("Error fetching balance data.");
                console.error(error);
            });
    }

    function flagAsBalanced() {
        const month = document.getElementById("monthSelect").value;
        const year = document.getElementById("yearSelect").value;

        // Replace with your actual POST endpoint
        const url = `/api/balance/flag`;
        const payload = { month, year };

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (res.ok) {
                    alert("Balance flagged as settled.");
                } else {
                    throw new Error("Failed to flag as balanced.");
                }
            })
            .catch(err => {
                alert("Error flagging as balanced.");
                console.error(err);
            });
    }
