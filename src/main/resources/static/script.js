
const API_URL = "/api/donations";

// Load donation summary
async function loadSummary() {
    const summaryDiv = document.getElementById("summary");

    try {
        const response = await fetch(`${API_URL}/summary`);

        if (!response.ok) {
            throw new Error("Could not load donation summary");
        }

        const data = await response.json();

        summaryDiv.innerHTML = `
            <div class="summary-card">
                <h3>Total Donations</h3>
                <p>${data.totalDonations}</p>
            </div>

            <div class="summary-card">
                <h3>Available</h3>
                <p>${data.availableDonations}</p>
            </div>

            <div class="summary-card">
                <h3>Claimed</h3>
                <p>${data.claimedDonations}</p>
            </div>

            <div class="summary-card">
                <h3>Picked Up</h3>
                <p>${data.pickedUpDonations}</p>
            </div>
        `;
    } catch (error) {
        summaryDiv.textContent =
            "Unable to load summary. Please check that the backend is running.";
        console.error(error);
    }
}


// Load donations according to the selected status
async function loadDonations() {
    const donationList = document.getElementById("donationList");
    const statusFilter = document.getElementById("statusFilter");

    const selectedStatus = statusFilter
        ? statusFilter.value
        : "ALL";

    let url = API_URL;

    if (selectedStatus !== "ALL") {
        url = `${API_URL}/status/${selectedStatus}`;
    }

    donationList.textContent = "Loading donations...";

    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("Could not load donations");
        }

        const donations = await response.json();

        displayDonations(donations);

    } catch (error) {
        donationList.textContent =
            "Unable to load donations. Please check that the backend is running.";
        console.error(error);
    }
}


// Display donation cards
function displayDonations(donations) {
    const donationList = document.getElementById("donationList");

    if (!donations || donations.length === 0) {
        donationList.textContent = "No matching food donations found.";
        return;
    }

    donationList.innerHTML = "";

    donations.forEach(donation => {
        const card = document.createElement("div");
        card.className = "donation-card";

        const title = document.createElement("h3");
        title.textContent = donation.foodName || "Food donation";

        const donor = document.createElement("p");
        donor.textContent = `Donor: ${donation.donorName}`;

        const quantity = document.createElement("p");
        quantity.textContent = `Quantity: ${donation.quantity}`;

        const location = document.createElement("p");
        location.textContent =
            `Pickup Location: ${donation.pickupLocation}`;

        const status = document.createElement("span");
        status.className = "status";
        status.textContent = donation.status;

        card.append(title, donor, quantity, location, status);

        // Show Claim Food button for available donations
        if (donation.status === "AVAILABLE") {
            const claimButton = document.createElement("button");
            claimButton.textContent = "Claim Food";

            claimButton.addEventListener("click", () => {
                claimDonation(donation.id);
            });

            card.appendChild(claimButton);
        }

        // Show Confirm Pickup button for claimed donations
        if (donation.status === "CLAIMED") {
            const pickupButton = document.createElement("button");
            pickupButton.textContent = "Confirm Pickup";

            pickupButton.addEventListener("click", () => {
                confirmPickup(donation.id);
            });

            card.appendChild(pickupButton);
        }

        donationList.appendChild(card);
    });
}


// Claim a donation
async function claimDonation(id) {
    try {
        const response = await fetch(`${API_URL}/${id}/claim`, {
            method: "PATCH"
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(
                result.message || "Unable to claim donation"
            );
        }

        alert("Food donation claimed successfully!");

        await refreshPageData();

    } catch (error) {
        alert(error.message);
    }
}


// Confirm pickup
async function confirmPickup(id) {
    try {
        const response = await fetch(`${API_URL}/${id}/pickup`, {
            method: "PATCH"
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(
                result.message || "Unable to confirm pickup"
            );
        }

        alert("Food pickup confirmed successfully!");

        await refreshPageData();

    } catch (error) {
        alert(error.message);
    }
}


// Refresh summary and donation list
async function refreshPageData() {
    await Promise.all([
        loadSummary(),
        loadDonations()
    ]);
}


// Search Food Donations
document.getElementById("searchForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const searchType =
            document.getElementById("searchType").value;

        const searchTerm =
            document.getElementById("searchInput").value.trim();

        const searchMessage =
            document.getElementById("searchMessage");

        if (!searchTerm) {
            searchMessage.textContent =
                "Please enter a search term.";
            return;
        }

        let searchUrl;

        if (searchType === "foodName") {
            searchUrl =
                `${API_URL}/search?foodName=${encodeURIComponent(searchTerm)}`;
        } else {
            searchUrl =
                `${API_URL}/location/${encodeURIComponent(searchTerm)}`;
        }

        searchMessage.textContent = "Searching...";

        try {
            const response = await fetch(searchUrl);

            if (!response.ok) {
                throw new Error("Unable to search donations.");
            }

            const donations = await response.json();

            displayDonations(donations);

            searchMessage.textContent =
                donations.length > 0
                    ? `Found ${donations.length} donation(s).`
                    : "No matching donations found.";

        } catch (error) {
            searchMessage.textContent = error.message;
            console.error(error);
        }
    }
);


// Show all donations and reset search and status filter
document.getElementById("clearSearchBtn").addEventListener(
    "click",
    async function () {
        document.getElementById("searchForm").reset();

        document.getElementById("searchMessage").textContent = "";

        const statusFilter =
            document.getElementById("statusFilter");

        if (statusFilter) {
            statusFilter.value = "ALL";
        }

        await loadDonations();
    }
);


// Filter donations whenever the selected status changes
document.getElementById("statusFilter").addEventListener(
    "change",
    async function () {
        document.getElementById("searchMessage").textContent = "";
        document.getElementById("searchForm").reset();

        await loadDonations();
    }
);


// Refresh button
document.getElementById("refreshBtn").addEventListener(
    "click",
    refreshPageData
);


// Add a new food donation
document.getElementById("donationForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const formMessage =
            document.getElementById("formMessage");

        const donation = {
            foodName: document.getElementById("foodName").value.trim(),
            quantity: document.getElementById("quantity").value.trim(),
            donorName: document.getElementById("donorName").value.trim(),
            pickupLocation: document.getElementById("pickupLocation").value.trim(),
            status: "AVAILABLE"
        };

        formMessage.textContent = "Adding donation...";
        formMessage.style.color = "#247447";

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(donation)
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(
                    result.message ||
                    "Unable to add donation. Please check your details."
                );
            }

            formMessage.textContent =
                "Food donation added successfully!";

            formMessage.style.color = "green";

            document.getElementById("donationForm").reset();

            await refreshPageData();

        } catch (error) {
            formMessage.textContent = error.message;
            formMessage.style.color = "red";
            console.error(error);
        }
    }
);


// Load data when the page opens
refreshPageData();
