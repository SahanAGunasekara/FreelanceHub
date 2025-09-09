function changeGigImg(imgElem) {
  document.getElementById('mainGigImg').src = imgElem.src;
  document.querySelectorAll('.gig-thumb-img').forEach(img => img.classList.remove('active'));
  imgElem.classList.add('active');
}

const favBtn = document.querySelector('.fav-btn');
favBtn.addEventListener('click', function() {
  const icon = this.querySelector('i');
  if (icon.classList.contains('bi-heart')) {
    icon.classList.remove('bi-heart');
    icon.classList.add('bi-heart-fill');
  } else {
    icon.classList.remove('bi-heart-fill');
    icon.classList.add('bi-heart');
  }
});

async function loadData(){
    //console.log("OK");
    const parameter = new URLSearchParams(window.location.search);
    
    if(parameter.has("id")){
        let gigId = parameter.get("id");
        const response = await fetch("loadGigData?id="+gigId);
        
        if(response.ok){
            const json = await response.json();
            
            if(json.status){
                console.log(json);
                document.getElementById("mainGigImg").src="Gig-Images\\"+json.gigData.id+"\\image1.png";
                document.getElementById("img1").src="Gig-Images\\"+json.gigData.id+"\\image1.png";
                document.getElementById("img2").src="Gig-Images\\"+json.gigData.id+"\\image2.png";
                document.getElementById("img3").src="Gig-Images\\"+json.gigData.id+"\\image3.png";
                
                document.getElementById("title").innerHTML=json.gigData.title; 
                document.getElementById("categorybdg").innerHTML=json.gigData.category.name;
                document.getElementById("tagbdg").innerHTML=json.gigData.tags;
                document.getElementById("price").innerHTML="Rs."+json.gigData.price;
                document.getElementById("description").innerHTML=json.gigData.description;
                document.getElementById("holder-name").innerHTML=json.gigData.freelancer.user.username;
                document.getElementById("delivery").innerHTML=json.gigData.delivery_time;
                document.getElementById("btn-price").innerHTML=json.gigData.price;
                
                document.getElementById("profileImg").src="profile-images\\"+json.gigData.freelancer.user.id+"\\image1.png";
                
                document.getElementById("fav-gig").addEventListener(
                "click", (e) => {
            addToFav(json.gigData.id);
            e.preventDefault();
        });
        
        document.getElementById("proceed-btn").addEventListener(
                "click",(e)=>{
                    proceedTochat(json.gigData.id);
            e.preventDefault();
                }
                        );
                
                let similer_gig_main = document.getElementById("similar-gig-main");
                let similer_gig = document.getElementById("similer-gig");
                similer_gig_main.innerHTML="";
                json.gigList.forEach(gig=>{
                    let single_gig_design=`<div class="col-lg-3 col-md-6 mb-4" id="similer-gig">
      <div class="card h-100">
         <a href="${'gig.html?id=' + gig.id}" id="similer-gig-a1">
        <img src="${'Gig-Images\\'+gig.id+'\\image1.png'}" class="card-img-top" alt="Similar Gig 1">
         </a>
        <div class="card-body">
          <h6 class="card-title">${gig.title}</h6>
          <div class="mb-2">
            <span class="badge bg-primary">${gig.category.name}</span>
            <span class="badge bg-secondary">${gig.tags}</span>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <span class="text-success fw-bold">$${gig.price}</span>
            <small class="text-muted">by ${gig.freelancer.user.username}</small>
          </div>
        </div>
      </div>
    </div>`;
                    similer_gig_main.innerHTML+=single_gig_design;
                });
                
            }else{
                console.log(json.message);
            }
        }else{
            console.log("Something went wrong try again");
        }
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
    
    function proceedTochat(id){
        //console.log(id);
        window.location="chat.html?id="+id;
    }



