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

function reloadCertificatePages() {
    currentCertificatePage = 1
    certificateCardLimit = certificates.length
    certificatePageCount = Math.ceil(certificateCardLimit / certificateCardIncrease)
    window.addEventListener("scroll", handleInfiniteScroll)
    document.querySelector(".loader").style.display = "inline-block";
}

function reloadTagPages() {
    tags = retrieveTagsFromLocalStorage()
    currentTagPage = 1
    tagCardLimit = tags.length
    tagPageCount = Math.ceil(tagCardLimit / tagCardIncrease)
}

function reloadCertificates() {
    certificatesContainer.innerHTML = ""
    addCertificateCards(currentCertificatePage)
}


/***
 * @param date - date, until certificate is valid
 * @return  number date duration
 * ***/
function getDateDiffInDays(date) {
    return Math.floor((new Date(date) - Date.now()) / (1000 * 60 * 60 * 24))
}

/***
 * Creates a certificate card with index
 * @param index - certificate index
 * ***/
const createCertificateCard = (index) => {
    const certificateDiv = document.createElement("div");
    certificateDiv.classList.add("certificate-container");
    let certificate = certificates[index]
    certificateDiv.innerHTML = `
            <div class="image-container">
                <img src="../images/gc.png" class="scaled-image" alt="Certificate-${index}">
            </div>
            <div class="info-container">
                <div class="name">
                    <a href = "item-details.html"><h3>${certificate.name}</h3></a>
                </div>
                <div class="heart">
                    <a href=""><span class="material-icons">favorite_border</span></a>
                </div>
                <div class="description">
                    <h6>${certificate.shortDescription}</h6>
                </div>
                <div class="duration">
                    <p>Duration: ${getDateDiffInDays(certificate.duration)}</p>
                </div>
                <div class="divider">
                    <hr>
                    <h4 >$${certificate.price}</h4>
                    <button class="add-to-cart-button">Add to cart</button>
                </div>
            </div>     
    `;
    certificatesContainer.appendChild(certificateDiv);
}

/***
 * Creates a tag card with index and adds an eventListener on click
 * @param index - tag index
 * ***/
const createTagCard = (index) => {
    const tagCard = document.createElement("a");
    tagCard.classList.add("tag-container")
    let tag = tags[index]
    tagCard.innerHTML = `
            <div class="image-container">
                <img src="../images/gc.png" class="scaled-image"  alt="Tag-${index}">
            </div>
           <p>${tag.name}</p>
    `;
    if (filter.tags.includes(tag.id)) {
        tagCard.classList.add("chosen")
    }
    tagsContainer.appendChild(tagCard);

}

/***
 * Creates a index cards on current page
 * @param pageIndex - page index index
 * ***/
const addCertificateCards = (pageIndex) => {
    const startRange = (pageIndex - 1) * certificateCardIncrease;
    const endRange = pageIndex === certificatePageCount ? certificateCardLimit : pageIndex * certificateCardIncrease;
    for (let i = startRange; i <= endRange - 1; i++) {
        createCertificateCard(i);
    }

};


/***
 * Creates a tags cards on current page
 * @param pageIndex - page index index
 * ***/
const addTagsCards = (pageIndex) => {
    //functions for  tags scroll buttons
    function incrementTagPage() {
        tagsContainer.innerHTML = ""
        addTagsCards(++currentTagPage)
    }

    function decrementTagPage() {
        tagsContainer.innerHTML = ""
        addTagsCards(--currentTagPage)
    }

    //horizontal scroll buttons for tags
    function addLeftButton() {
        let button = document.createElement("a");
        button.innerHTML = `
        <span class="material-icons">
            keyboard_double_arrow_left
        </span>`
        button.classList.add("scroll-button")
        button.classList.add("scroll-button-left")
        button.href = "#"
        document.querySelector(".tags").appendChild(button)
        button.addEventListener("click", decrementTagPage)
    }

    function addRightButton() {
        let button2 = document.createElement("a");
        button2.innerHTML = `
        <span class="material-icons">
            keyboard_double_arrow_right
        </span>`
        button2.classList.add("scroll-button")
        button2.href = "#"
        button2.classList.add("scroll-button-right")
        button2.addEventListener("click", incrementTagPage)
        document.querySelector(".tags").appendChild(button2)
    }

    if (pageIndex !== 1) {
        addLeftButton();
    }

    const startRange = (pageIndex - 1) * tagCardIncrease;
    const endRange = pageIndex >= tagPageCount ? tagCardLimit : pageIndex * tagCardIncrease;
    for (let i = startRange; i <= endRange - 1; i++) {
        createTagCard(i);
    }

    if (pageIndex !== tagPageCount && tagPageCount > 1) {
        addRightButton();
    }
};
