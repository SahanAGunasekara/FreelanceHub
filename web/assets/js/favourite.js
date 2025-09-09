function removeGig(button) {
  const card = button.closest(".gig-card");
  card.remove();

  const container = document.querySelector(".row");
  if (container.children.length === 0) {
    container.innerHTML = `<p class="text-center">You have no favorite gigs yet.</p>`;
  }
}

async function loadFav(){
    //console.log("OK");
    const popup = new Notification();
    const response = await fetch("loadFavourite");
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            console.log(json);
            const fav_main_container = document.getElementById("fav-main");
            fav_main_container.innerHTML="";
            json.favouriteList.forEach(gigs=>{
                const fav_card = `<div class="col gig-card" data-id="1" id="fav-card">
        <div class="card h-100">
                <a href="${'gig.html?id='+gigs.gig.id}">
          <img src="${'Gig-Images\\'+gigs.gig.id+'\\image1.png'}" class="card-img-top" alt="Professional Website Design">
                </a>
          <div class="card-body">
            <h5 class="card-title">${gigs.gig.title}</h5>
            <p class="card-text text-muted">${gigs.gig.description}</p>
            <div class="d-flex justify-content-between align-items-center">
              <span class="fw-bold">Rs.${gigs.gig.price}</span>
              <span class="text-warning">★★★★☆</span>
            </div>
          </div>
          <div class="card-footer text-end">
            <button class="remove-btn" onclick="removeGig(this)" title="Remove from Favorites">🗑️</button>
          </div>
        </div>
      </div>`;
               fav_main_container.innerHTML+=fav_card;
            });
        }else{
            popup.error({
                message:json.message
            });
            console.log(json.message);
        }
    }else{
        popup.error({
                message:"Something went wrong"
            });
        //console.log("Something went wrong");
    }
}



