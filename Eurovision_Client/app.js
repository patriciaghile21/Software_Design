const translations = {
    en: {
        adminPanel: "Admin Panel",
        addNew: "Add New Entry",
        viewAll: "View All Entries",
        search: "Search:",
        sortBy: "Sort by:",
        sortCountryAZ: "Country (A-Z)",
        sortCountryZA: "Country (Z-A)",
        sortArtistAZ: "Artist (A-Z)",
        sortArtistZA: "Artist (Z-A)",
        sortYearAsc: "Year (Ascending)",
        sortYearDesc: "Year (Descending)",
        exportFormat: "Export Format:",
        exportBtn: "Export Data",
        logout: "Logout",
        dbTitle: "Eurovision Entries Database",
        colCountry: "Country",
        colArtist: "Artist",
        colTitle: "Title",
        colYear: "Year",
        colActions: "Actions",
        btnEdit: "Edit",
        btnDelete: "Delete",
        chatHeader: "Live Chat",
        chatSend: "Send"
    },
    ro: {
        adminPanel: "Panou Administrator",
        addNew: "Adaugă Piesă",
        viewAll: "Vezi Toate Piesele",
        search: "Caută:",
        sortBy: "Sortează după:",
        sortCountryAZ: "Țară (A-Z)",
        sortCountryZA: "Țară (Z-A)",
        sortArtistAZ: "Artist (A-Z)",
        sortArtistZA: "Artist (Z-A)",
        sortYearAsc: "An (Crescător)",
        sortYearDesc: "An (Descrescător)",
        exportFormat: "Format Export:",
        exportBtn: "Descarcă Date",
        logout: "Deconectare",
        dbTitle: "Baza de Date Eurovision",
        colCountry: "Țară",
        colArtist: "Artist",
        colTitle: "Titlu",
        colYear: "An",
        colActions: "Acțiuni",
        btnEdit: "Editează",
        btnDelete: "Șterge",
        chatHeader: "Chat Live",
        chatSend: "Trimite"
    }
};

function changeLanguage() {
    const lang = document.getElementById('language-select').value;
    const elements = document.querySelectorAll('[data-i18n]');

    elements.forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (translations[lang] && translations[lang][key]) {
            el.innerText = translations[lang][key];
        }
    });
}

let currentEditId = null;

async function loginUser() {
    const usernameInput = document.getElementById('username').value;
    const passwordInput = document.getElementById('password').value;
    const messageBox = document.getElementById('message-box');

    if (!usernameInput || !passwordInput) {
        messageBox.innerHTML = "<p style='color: red;'>Please enter both username and password.</p>";
        return;
    }

    try {
        const response = await fetch('http://localhost:7002/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: usernameInput,
                password: passwordInput
            })
        });

        if (response.ok) {
            const loggedInUser = await response.json();

            sessionStorage.setItem('userRole', loggedInUser.role);
            sessionStorage.setItem('userCountry', loggedInUser.country);
            sessionStorage.setItem('userEmail', loggedInUser.email);

            window.location.href = 'dashboard.html';
        } else {
            messageBox.innerHTML = "<p style='color: red;'>Invalid username or password!</p>";
        }
    } catch (error) {
        messageBox.innerHTML = "<p style='color: red;'>Connection error.</p>";
    }
}

async function loadEntries() {
    const searchQuery = document.getElementById('search-input').value;
    let url = 'http://localhost:7001/entries';

    if (searchQuery && searchQuery.trim() !== "") {
        url += `?search=${encodeURIComponent(searchQuery)}`;
    }

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'X-User-Role': sessionStorage.getItem('userRole'),
                'X-User-Country': sessionStorage.getItem('userCountry'),
                'X-User-Email': sessionStorage.getItem('userEmail')
            }
        });

        if (response.ok) {
            let entries = await response.json();
            entries = sortData(entries);
            displayEntries(entries);
        } else {
            console.error("Failed to load entries");
        }
    } catch (error) {
        console.error("Error fetching entries:", error);
    }
}

function sortData(entries) {
    const sortOption = document.getElementById('sort-select').value;

    return entries.sort((a, b) => {
        if (sortOption === 'Country (A-Z)') {
            return a.country.localeCompare(b.country);
        }
        if (sortOption === 'Country (Z-A)') {
            return b.country.localeCompare(a.country);
        }
        if (sortOption === 'Artist (A-Z)') {
            return a.artist.localeCompare(b.artist);
        }
        if (sortOption === 'Artist (Z-A)') {
            return b.artist.localeCompare(a.artist);
        }
        if (sortOption === 'Year (Ascending)') {
            return a.releaseYear - b.releaseYear;
        }
        if (sortOption === 'Year (Descending)') {
            return b.releaseYear - a.releaseYear;
        }
        return 0;
    });
}

function displayEntries(entries) {
    const tbody = document.getElementById('entries-body');
    tbody.innerHTML = "";

    const userRole = sessionStorage.getItem('userRole') ? sessionStorage.getItem('userRole').toUpperCase() : '';
    const userCountry = sessionStorage.getItem('userCountry');

    entries.forEach(entry => {
        const row = document.createElement('tr');

        let actionsHtml = "";
        if (userRole === 'ADMIN' || (userRole === 'HOD' && entry.country === userCountry)) {
            actionsHtml = `
                <button class="action-icon-btn edit-btn" data-i18n="btnEdit" onclick="openEditModal(${entry.id}, '${entry.country}', '${entry.artist}', '${entry.title}', ${entry.releaseYear})">Edit</button>
                <button class="action-icon-btn delete-btn" data-i18n="btnDelete" onclick="deleteEntry(${entry.id})">Delete</button>
            `;
        }

        row.innerHTML = `
            <td>${entry.country}</td>
            <td>${entry.artist}</td>
            <td>${entry.title}</td>
            <td>${entry.releaseYear}</td>
            <td>${actionsHtml}</td>
        `;
        tbody.appendChild(row);
    });

    changeLanguage();
}

function resetView() {
    document.getElementById('search-input').value = '';
    document.getElementById('sort-select').value = 'Country (A-Z)';
    loadEntries();
}

function openEditModal(id, country, artist, title, year) {
    currentEditId = id;

    const userRole = sessionStorage.getItem('userRole') ? sessionStorage.getItem('userRole').toUpperCase() : '';
    const countryInput = document.getElementById('entry-country');

    document.getElementById('modal-title').innerText = "Edit Entry";
    countryInput.value = country;
    document.getElementById('entry-artist').value = artist;
    document.getElementById('entry-title').value = title;
    document.getElementById('entry-year').value = year;
    document.getElementById('modal-message').innerHTML = '';

    if (userRole === 'HOD') {
        countryInput.readOnly = true;
    } else {
        countryInput.readOnly = false;
    }

    document.getElementById('entry-modal').style.display = 'flex';
}

function openAddModal() {
    currentEditId = null;

    const userRole = sessionStorage.getItem('userRole') ? sessionStorage.getItem('userRole').toUpperCase() : '';
    const userCountry = sessionStorage.getItem('userCountry');
    const countryInput = document.getElementById('entry-country');

    document.getElementById('modal-title').innerText = "Add New Entry";
    document.getElementById('entry-artist').value = '';
    document.getElementById('entry-title').value = '';
    document.getElementById('entry-year').value = '';
    document.getElementById('modal-message').innerHTML = '';

    if (userRole === 'HOD') {
        countryInput.value = userCountry;
        countryInput.readOnly = true;
    } else {
        countryInput.value = '';
        countryInput.readOnly = false;
    }

    document.getElementById('entry-modal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('entry-modal').style.display = 'none';
}

async function saveNewEntry() {
    const country = document.getElementById('entry-country').value;
    const artist = document.getElementById('entry-artist').value;
    const title = document.getElementById('entry-title').value;
    const year = document.getElementById('entry-year').value;
    const modalMessage = document.getElementById('modal-message');

    if (!country || !artist || !title || !year) {
        modalMessage.innerHTML = "<span style='color: red;'>All fields are required!</span>";
        return;
    }

    const entryData = {
        country: country,
        artist: artist,
        title: title,
        releaseYear: parseInt(year)
    };

    let url = 'http://localhost:7001/entries';
    let methodType = 'POST';

    if (currentEditId !== null) {
        url = `http://localhost:7001/entries/${currentEditId}`;
        methodType = 'PUT';
    }

    try {
        const response = await fetch(url, {
            method: methodType,
            headers: {
                'Content-Type': 'application/json',
                'X-User-Role': sessionStorage.getItem('userRole'),
                'X-User-Country': sessionStorage.getItem('userCountry'),
                'X-User-Email': sessionStorage.getItem('userEmail')
            },
            body: JSON.stringify(entryData)
        });

        if (response.ok) {
            closeModal();
            loadEntries();
        } else {
            const errorData = await response.json();
            modalMessage.innerHTML = `<span style='color: red;'>Error: ${errorData.message} (Code: ${errorData.errorCode})</span>`;
        }
    } catch (error) {
        modalMessage.innerHTML = "<span style='color: red;'>Connection error!</span>";
    }
}

async function deleteEntry(id) {
    if (confirm("Are you sure you want to delete this entry?")) {
        try {
            const response = await fetch(`http://localhost:7001/entries/${id}`, {
                method: 'DELETE',
                headers: {
                    'X-User-Role': sessionStorage.getItem('userRole'),
                    'X-User-Country': sessionStorage.getItem('userCountry'),
                    'X-User-Email': sessionStorage.getItem('userEmail')
                }
            });

            if (response.ok) {
                loadEntries();
            } else {
                const errorData = await response.json();
                alert(`Failed: ${errorData.message}`);
            }
        } catch (error) {
            alert("Connection error while trying to delete.");
        }
    }
}

async function exportData() {
    const format = document.getElementById('export-format').value;
    const url = `http://localhost:7001/entries/export?format=${format}`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'X-User-Role': sessionStorage.getItem('userRole'),
                'X-User-Country': sessionStorage.getItem('userCountry'),
                'X-User-Email': sessionStorage.getItem('userEmail')
            }
        });

        if (response.ok) {
            const blob = await response.blob();
            const downloadUrl = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = downloadUrl;
            a.download = `eurovision_data.${format}`;
            document.body.appendChild(a);
            a.click();
            a.remove();
        }
    } catch (error) {
        console.error(error);
    }
}

function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

let chatWs;

function initChat() {
    chatWs = new WebSocket("ws://localhost:7001/chat");

    chatWs.onmessage = function(event) {
        const msgObj = JSON.parse(event.data);
        displayChatMessage(msgObj);
    };
}

function sendChatMessage() {
    const input = document.getElementById('chat-input');
    const text = input.value.trim();
    if (text === "") return;

    const userEmail = sessionStorage.getItem('userEmail') || 'Unknown User';

    const now = new Date();
    const timeString = now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0');

    const msgObj = {
        sender: userEmail,
        time: timeString,
        text: text
    };

    chatWs.send(JSON.stringify(msgObj));

    input.value = "";
}

function handleChatKeyPress(event) {
    if (event.key === 'Enter') {
        sendChatMessage();
    }
}

function displayChatMessage(msgObj) {
    const messagesContainer = document.getElementById('chat-messages');
    const msgDiv = document.createElement('div');
    msgDiv.classList.add('chat-message');

    const currentUser = sessionStorage.getItem('userEmail');
    if (msgObj.sender === currentUser) {
        msgDiv.classList.add('self');
    }

    msgDiv.innerHTML = `
        <div class="chat-meta"><b>${msgObj.sender}</b> • ${msgObj.time}</div>
        <div class="chat-text">${msgObj.text}</div>
    `;

    messagesContainer.appendChild(msgDiv);

    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function toggleChat() {
    const chatContainer = document.querySelector('.chat-container');
    chatContainer.classList.toggle('collapsed');
}