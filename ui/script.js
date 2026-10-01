// Dashboard for the CPSC 4620 pizza database.
//
// A browser can't connect to MySQL directly, so this page uses a snapshot of the
// sample data in database/PizzaDB_portfolio.sql. The numbers match what the
// profitbyordertype, profitbypizza, and toppingpopularity views and the queries in
// analytics/business_questions.sql return for that data.
// To refresh after changing the database, re-run those queries and update DATA.

const DATA = {
    "customers": 4,
    "orders": 7,
    "revenue": 283.93,
    "cost": 62.98,
    "profit": 220.95,
    "profitByOrderType": [
        [
            "delivery",
            99.39
        ],
        [
            "pickup",
            90.34
        ],
        [
            "dinein",
            31.22
        ]
    ],
    "profitByPizza": [
        [
            "Large Thin",
            69.49
        ],
        [
            "Large Original",
            69.48
        ],
        [
            "XLarge Original",
            68.8
        ],
        [
            "XLarge Gluten-Free",
            20.86
        ],
        [
            "Medium Pan",
            10.62
        ],
        [
            "Small Original",
            5.53
        ]
    ],
    "toppingPopularity": [
        [
            "Pepperoni",
            10
        ],
        [
            "Regular Cheese",
            10
        ],
        [
            "Four Cheese Blend",
            7
        ],
        [
            "Chicken",
            3
        ],
        [
            "Mushrooms",
            3
        ],
        [
            "Banana Peppers",
            2
        ],
        [
            "Black Olives",
            2
        ],
        [
            "Green Pepper",
            2
        ],
        [
            "Ham",
            2
        ],
        [
            "Onion",
            2
        ],
        [
            "Pineapple",
            2
        ],
        [
            "Roma Tomato",
            2
        ],
        [
            "Sausage",
            2
        ],
        [
            "Bacon",
            1
        ],
        [
            "Feta Cheese",
            1
        ],
        [
            "Goat Cheese",
            1
        ],
        [
            "Jalapenos",
            0
        ]
    ],
    "inventory": [
        [
            "Mushrooms",
            52,
            50
        ],
        [
            "Black Olives",
            39,
            25
        ],
        [
            "Pineapple",
            15,
            0
        ],
        [
            "Chicken",
            56,
            25
        ],
        [
            "Banana Peppers",
            36,
            0
        ],
        [
            "Pepperoni",
            100,
            50
        ],
        [
            "Sausage",
            100,
            50
        ],
        [
            "Ham",
            78,
            25
        ],
        [
            "Green Pepper",
            79,
            25
        ],
        [
            "Goat Cheese",
            54,
            0
        ],
        [
            "Onion",
            85,
            25
        ],
        [
            "Jalapenos",
            64,
            0
        ],
        [
            "Feta Cheese",
            75,
            0
        ],
        [
            "Roma Tomato",
            86,
            10
        ],
        [
            "Bacon",
            89,
            0
        ],
        [
            "Four Cheese Blend",
            150,
            25
        ],
        [
            "Regular Cheese",
            250,
            50
        ]
    ]
};

const money = (n) => "$" + n.toFixed(2);

function setText(id, text) {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
}

function drawCharts() {
    if (typeof Chart === "undefined") return; // Chart.js failed to load (offline)
    const green = "#2e7d32";
    const grid = { color: "rgba(0,0,0,0.06)" };
    const dollars = { ticks: { callback: (v) => "$" + v }, grid };

    new Chart(document.getElementById("orderTypeChart"), {
        type: "bar",
        data: {
            labels: DATA.profitByOrderType.map((r) => r[0]),
            datasets: [{ label: "Profit", data: DATA.profitByOrderType.map((r) => r[1]), backgroundColor: green }],
        },
        options: { plugins: { legend: { display: false } }, scales: { y: dollars } },
    });

    const topToppings = DATA.toppingPopularity.slice(0, 8);
    new Chart(document.getElementById("toppingChart"), {
        type: "bar",
        data: {
            labels: topToppings.map((r) => r[0]),
            datasets: [{ label: "Times used", data: topToppings.map((r) => r[1]), backgroundColor: "#c0392b" }],
        },
        options: {
            indexAxis: "y",
            plugins: { legend: { display: false } },
            scales: { x: { ticks: { precision: 0 }, grid } },
        },
    });

    new Chart(document.getElementById("pizzaChart"), {
        type: "bar",
        data: {
            labels: DATA.profitByPizza.map((r) => r[0]),
            datasets: [{ label: "Profit", data: DATA.profitByPizza.map((r) => r[1]), backgroundColor: "#e67e22" }],
        },
        options: { plugins: { legend: { display: false } }, scales: { y: dollars } },
    });
}

// Inventory: lowest stock buffer first. A topping is flagged when current stock
// is within 25% of its minimum, or at zero when its minimum is zero.
function drawInventory() {
    const rows = DATA.inventory.slice(0, 6).map(([name, current, min]) => {
        const low = min > 0 ? current <= min * 1.25 : current <= 0;
        const status = low ? "Near minimum" : "OK";
        const color = low ? "#c0392b" : "#2e7d32";
        return `<tr><td>${name}</td><td>${current}</td><td>${min}</td>` +
               `<td style="color:${color};font-weight:bold">${status}</td></tr>`;
    });
    const box = document.getElementById("inventory-results");
    if (!box) return;
    box.innerHTML =
        `<table style="width:100%;border-collapse:collapse;text-align:left">` +
        `<thead><tr><th>Topping</th><th>In stock</th><th>Minimum</th><th>Status</th></tr></thead>` +
        `<tbody>${rows.join("")}</tbody></table>` +
        `<p style="font-size:13px;color:#777;margin-top:10px">Showing the 6 toppings with the smallest buffer above their minimum.</p>`;
}

setText("customer-count", DATA.customers);
setText("order-count", DATA.orders);
setText("revenue", money(DATA.revenue));
setText("profit", money(DATA.profit));
drawCharts();
drawInventory();
