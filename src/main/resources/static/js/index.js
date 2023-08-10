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

const handleInfiniteScroll = _.throttle(() => {
    const endOfPage = document.body.offsetHeight - (window.innerHeight + window.pageYOffset) <= 150;

    if (endOfPage) {
        addCertificateCards(++currentCertificatePage);
    }
}, 700);


const removeInfiniteScroll = () => {
    document.querySelector(".loader").style.display = "none";
    window.removeEventListener("scroll", handleInfiniteScroll);
};


function filterCertificates() {
    let filteredCertificates = retrieveCertificatesFromLocalStorage()
    let inputValue = filter.input
    if (!inputValue.isEmpty) {
        filteredCertificates = filteredCertificates.filter((certificate) => {
            return certificate.name.includes(inputValue) || certificate.shortDescription.includes(inputValue)
        });
    }
    let filterTags = filter.tags;
    if (filterTags) {
        filteredCertificates = filteredCertificates.filter((certificate) => {
            let gcTags = certificate.tags.map(tag => tag.id);
            return filterTags.every(id => gcTags.includes(id))
        })
    }
    certificates = filteredCertificates
}

window.onload = function () {
    //to remove standard browser scroll restore
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }
    //to handle scroll position
    const storeScrollPosition = () => {
        window.removeEventListener("scroll", handleInfiniteScroll);
        localStorage.setItem("scrollHistory", window.pageYOffset);
        localStorage.setItem("filter", JSON.stringify(filter))
    };

    const restoreScrollPosition = (historyPage, scrollPosition) => {
        while (currentCertificatePage <= historyPage) {
            addCertificateCards(currentCertificatePage++);
        }
        window.scrollTo({
            top: scrollPosition,
            behavior: 'smooth',
        });
    };

    //restore filter if it was
    let historyFilter = localStorage.getItem("filter");
    if (historyFilter) {
        filter = JSON.parse(historyFilter)
        filterCertificates()
    }

    reloadTagPages();
    addTagsCards(currentTagPage)
    reloadCertificatePages()
    //restore scroll position if needed
    let scrollPosition = +localStorage.getItem("scrollHistory")
    let historyPage = Math.ceil(scrollPosition / (500 + 0.02 * window.innerHeight))
    if (historyPage > 1) {
        restoreScrollPosition(historyPage, scrollPosition);
    } else {
        addCertificateCards(currentCertificatePage)
    }

    // show scroll top button
    window.onscroll = () => {
        let myButton = document.getElementById("scroll-top-button");
        if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
            myButton.style.display = "block";
        } else {
            myButton.style.display = "none";
        }
    }
    searchInput.addEventListener("keyup", _.debounce(() => {
        filter.input = searchInput.value
        filterCertificates()
        reloadCertificatePages()
        reloadCertificates()
    }, 500))
    window.addEventListener("unload", storeScrollPosition)
};

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
    tagCard.addEventListener("click", _.debounce( function(){
        //if is not active as filter parameter - add to filter
        if (!filter.tags.includes(tag.id)) {
            tagCard.classList.add("chosen")
            filter.tags.push(tag.id)
        } else {
            filter.tags.splice(filter.tags.indexOf(tag.id), 1)
            tagCard.classList.remove("chosen")
        }
        filterCertificates()
        reloadCertificatePages()
        reloadCertificates()
    },500,{leading:true, trailing:false}));
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
    //remove infinite scroll when last page become visible
    if (pageIndex === certificatePageCount) {
        removeInfiniteScroll()
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

const clearFilters = _.debounce( function (){
    document.querySelectorAll(".tag-container").forEach((tagContainer =>
            tagContainer.classList.remove("chosen")
    ));
    filter.tags = []
    filter.input = ""
    searchInput.value = ""
    certificates = retrieveCertificatesFromLocalStorage()
    reloadCertificatePages()
    reloadCertificates()
},500,{leading:true, trailing:false});

const scrollTopFunction = _.debounce(function (){
    window.scrollTo({
        top: 0,
        behavior: 'smooth',
    });
},500,{leading:true, trailing:false});