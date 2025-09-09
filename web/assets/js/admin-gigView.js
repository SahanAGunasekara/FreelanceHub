// Image gallery functionality
        function changeImage(thumbnail, newSrc) {
            // Update main image
            const mainImage = document.getElementById('mainImage');
            mainImage.src = newSrc;
            
            // Update active thumbnail
            const thumbnails = document.querySelectorAll('.thumbnail');
            thumbnails.forEach(thumb => thumb.classList.remove('active'));
            thumbnail.classList.add('active');
        }

        // Action functions
async function approveGig() {
    const popup = new Notification();
    if (confirm('Are you sure you want to approve this gig?')) {
                //console.log("OK");
        const parameter = new URLSearchParams(window.location.search);
    
            if(parameter.has("id")){
                let gigId = parameter.get("id");
                  const response = await fetch("approveGig?id="+gigId);
                  if(response.ok){
                      const json = await response.json();
                      if(json.status){
                          popup.success({
                            message:json.message
                        });
                        const statusBadge = document.getElementById("status-badge");
                        statusBadge.innerHTML = '<i class="fas fa-circle me-1"></i>Approved';
                        statusBadge.style.background = 'rgba(40, 167, 69, 0.2)';
                        statusBadge.style.color = '#28a745';
                      }else{
                          popup.error({
                            message:json.message
                        });
                      }
                  }else{
                      popup.error({
                            message:"Something went wrong"
                        });
                  }
            }
                
    }
}

        async function deactivateGig() {
            const popup = new Notification();
            if (confirm('Are you sure you want to deactivate this gig?')) {
                //console.log("OK");
                const parameter = new URLSearchParams(window.location.search);
    
            if(parameter.has("id")){
                let gigId = parameter.get("id");
                const response = await fetch("deactivateGig?id="+gigId);
                
                if(response.ok){
                    const json = await response.json();
                    if(json.status){
                        popup.success({
                            message:json.message
                        });
                        const statusBadge = document.getElementById("status-badge");
                        statusBadge.innerHTML = '<i class="fas fa-circle me-1"></i>Deactivated';
                        statusBadge.style.background = 'rgba(220, 53, 69, 0.2)';
                        statusBadge.style.color = '#dc3545';
                    }else{
                        popup.error({
                            message:json.message
                        });
                    }
                }else{
                    popup.error({
                            message:"Something went wrong"
                        });
                }
            }
            }
        }

        function goBack() {
            // Replace with actual back navigation
            window.history.back();
        }

        // Initialize page
        document.addEventListener('DOMContentLoaded', function() {
            // Add any initialization code here
            console.log('Single gig view page loaded');
            
            // Set first thumbnail as active by default
            const firstThumbnail = document.querySelector('.thumbnail');
            if (firstThumbnail) {
                firstThumbnail.classList.add('active');
            }
        });
        
async function loadData(){
     //console.log("OK");    
     const parameter = new URLSearchParams(window.location.search);
    
    if(parameter.has("id")){
        let gigId = parameter.get("id");
        const response = await fetch("adminGigData?id="+gigId);
        
        if(response.ok){
            const json = await response.json();
            
            if(json.status){
                console.log(json);
                document.getElementById("mainImage").src="Gig-Images\\"+json.gigData.id+"\\image1.png";
                document.getElementById("thumbnail-image1").src="Gig-Images\\"+json.gigData.id+"\\image1.png";
                document.getElementById("thumbnail-image2").src="Gig-Images\\"+json.gigData.id+"\\image2.png";
                document.getElementById("thumbnail-image3").src="Gig-Images\\"+json.gigData.id+"\\image3.png";
                document.getElementById("title").innerHTML=json.gigData.title;
                document.getElementById("tag").innerHTML=json.gigData.tags;
                document.getElementById("price").innerHTML=json.gigData.price;
                document.getElementById("description").innerHTML=json.gigData.description;
                document.getElementById("days").innerHTML=json.gigData.delivery_time;
                document.getElementById("user-profile").src="profile-images\\"+json.gigData.freelancer.user.id+"\\image1.png";
                document.getElementById("user-name").innerHTML=json.gigData.freelancer.user.username;
                document.getElementById("role").innerHTML=json.gigData.freelancer.skill;
                
                const thumbImage1 = document.getElementById("thumbnail-image1");
                const thumbImage2 = document.getElementById("thumbnail-image2");
                const thumbImage3 = document.getElementById("thumbnail-image3");
                
                document.getElementById("thumbnail-image1").addEventListener(
                "click", (e) => {
            changeImage(thumbImage1, "Gig-Images\\"+json.gigData.id+"\\image1.png");
            e.preventDefault();
            });
            
            document.getElementById("thumbnail-image2").addEventListener(
                "click", (e) => {
            changeImage(thumbImage2, "Gig-Images\\"+json.gigData.id+"\\image2.png");
            e.preventDefault();
            });
            
            document.getElementById("thumbnail-image3").addEventListener(
                "click", (e) => {
            changeImage(thumbImage3, "Gig-Images\\"+json.gigData.id+"\\image3.png");
            e.preventDefault();
            });
            }else{
                console.log(json.message);
            }
        }else{
            console.log("something went wrong");
        }
    }
}


