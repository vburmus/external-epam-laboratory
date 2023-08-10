const certificatesContainer = document.querySelector(".certificates");
const tagsContainer = document.querySelector(".tags")
const searchInput = document.getElementById("search-input");

certificates = [];
tags = [];

let certificateCardLimit
const certificateCardIncrease = 4;
let certificatePageCount
let currentCertificatePage

let tagCardLimit
const tagCardIncrease = 5;
let tagPageCount
let currentTagPage

let filter = {
    "input": "",
    "tags": []
}

function retrieveCertificatesFromLocalStorage() {
    const storedData = JSON.parse(localStorage.getItem("certificates"));
    if (storedData) {
        storedData.sort((a, b) => new Date(a.createDate) - new Date(b.createDate));
    }
    return storedData
}

function retrieveTagsFromLocalStorage() {
    const storedData = localStorage.getItem("tags");
    return JSON.parse(storedData);
}
