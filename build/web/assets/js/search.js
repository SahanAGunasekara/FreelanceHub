 // Price range slider interactivity
    const priceRange = document.getElementById('priceRange');
    const priceValue = document.getElementById('priceValue');
    priceRange.addEventListener('input', function() {
      priceValue.textContent = `$0 - $${this.value}`;
    });

    // Favourite heart toggle
    document.querySelectorAll('.fav-btn').forEach(btn => {
      btn.addEventListener('click', function() {
        const icon = this.querySelector('i');
        if (icon.classList.contains('bi-heart')) {
          icon.classList.remove('bi-heart');
          icon.classList.add('bi-heart-fill');
        } else {
          icon.classList.remove('bi-heart-fill');
          icon.classList.add('bi-heart');
        }
      });
    });
    
    async function loadData(){
        //console.log("OK");
        const response = await fetch("loadSearch");
        
        if(response.ok){
            const json = await response.json();
            if(json.status){
                console.log(json);
                updateGigView(json);
                loadSelector("categoryFilter",json.categoryList,"name");
                loadSelector("countryFilter",json.countryList,"name");
                
            }else{
                console.log("Error");
            }
        }else{
            console.log("Something went wronng");
        }
        
    }
    
    function loadSelector(id,list,name){
        const selector = document.getElementById(id);
                list.forEach(category=>{
                    
                    let option = document.createElement("option");
                    option.innerHTML=category[name];
                    option.value=category.id;
                    selector.appendChild(option);
                });
    }
    
    async function filterGigs(firstResult){
        //console.log("OK");
        const category = document.getElementById("categoryFilter").value;
        const selectedTime = document.querySelector('input[name="deliveryTime"]:checked');
        const selectedDeliveryTime = selectedTime? selectedTime.value: "1";
        const country = document.getElementById("countryFilter").value;
        const language = document.getElementById("languageFilter").value;
        const available = document.querySelector('input[name="availability"]:checked');
        const availability = available? available.value: "0";
        const rating = document.querySelector('input[name="rating"]:checked');
        const rate = rating? rating.value: "0";
        const priceRange1 = document.getElementById("priceRange").min;
        const priceRange2 = document.getElementById("priceRange").value;
        
        const filterData={
            firstResult:firstResult,
            gigCategory:category,
            deliveryTime:selectedDeliveryTime,
            country:country,
            language:language,
            availability:availability,
            rating:rate,
            startingPrice:priceRange1,
            endPriceRange:priceRange2
        };
        
        const filterJsondata = JSON.stringify(filterData);
        
        const response = await fetch("searchGigs",{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:filterJsondata
        });
        
        if(response.ok){
            const json = await response.json();
            if(json.status){
                console.log(json);
                updateGigView(json);
            }else{
                console.log("error");
            }
            
        }else{
            console.log("Something went wrong");
        }
        
    }
    
    const result_container  = document.getElementById("result-container");
    const st_pagination_button  = document.getElementById("st-pagination-button");
    let current_page = 0;
    function updateGigView(json){
        const container = document.getElementById("results");
        container.innerHTML="";
        json.gigList.forEach(gig=>{
            let result_container_clone = result_container.cloneNode(true);
            result_container_clone.querySelector("#fav-item").addEventListener(
                "click", (e) => {
            addToFav(gig.id);
            e.preventDefault();
        });
            result_container_clone.querySelector("#result-container-a").href="gig.html?id="+gig.id;
            result_container_clone.querySelector("#img").src="Gig-Images//" + gig.id + "//image1.png";
            result_container_clone.querySelector("#title").innerHTML=gig.title;
            result_container_clone.querySelector("#price").innerHTML="$"+gig.price;
            result_container_clone.querySelector("#description").innerHTML=gig.description;
            container.appendChild(result_container_clone);
        });
        
        let st_pagination_container = document.getElementById("st-pagination-container");
        st_pagination_container.innerHTML="";
        let all_gig_count = json.gigCount;
        let gigs_per_page = 6;
        let pages = Math.ceil(all_gig_count / gigs_per_page);
        
        //previous-button
    if (current_page !== 0) {
        let st_pagination_button_prev_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_prev_clone.innerHTML = "Prev";
        st_pagination_button_prev_clone.addEventListener(
                "click", (e) => {
            current_page--;
            filterGigs(current_page * gigs_per_page);
            e.preventDefault();
        });
        st_pagination_container.appendChild(st_pagination_button_prev_clone);
    }
    
    // pagination-buttons
    for (let i = 0; i < pages; i++) {
        let st_pagination_button_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_clone.innerHTML = i + 1;
        st_pagination_button_clone.addEventListener(
                "click", (e) => {
            current_page = i;
            filterGigs(i * gigs_per_page);
            e.preventDefault();
        });

        if (i === Number(current_page)) {
            st_pagination_button_clone.className = " btn btn-primary me-3";
        } else {
            st_pagination_button_clone.className = "btn btn-success me-3";
        }
        st_pagination_container.appendChild(st_pagination_button_clone);
    }
    
    // next-button
    if (current_page !== (pages - 1)) {
        let st_pagination_button_next_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_next_clone.innerHTML = "Next";
        st_pagination_button_next_clone.addEventListener(
                "click", (e) => {
            current_page++;
            filterGigs(current_page * gigs_per_page);
            e.preventDefault();
        });
        st_pagination_container.appendChild(st_pagination_button_next_clone);
    }
    
    }
    
    async function addToFav(gigId){
        const popup = new Notification();
        const response = await fetch("AddToFav?id="+gigId);
        
        if(response.ok){
            const json = await response.json();
            if(json.status){
                popup.success({
                    message:json.message
                });
                //console.log(json.message);
            }else{
                popup.error({
                    message:json.message
                });
                //console.log(json.message);
            }
            
        }else{
            console.log("something went wrong");
        }
    }
    
    


