// Action functions
        async function activateFreelancer(id) {
            const popup = new Notification();
            if (confirm('Are you sure you want to activate this freelancer?')) {
                //console.log("OK");
                const response = await fetch("activateFreelancer?id="+id);
                
                if(response.ok){
                    const json = await response.json();
                    if(json.status){
                        popup.success({
                    message:json.message
                });
                const status = document.getElementById("status-indicator");
                status.className = 'status-indicator status-active';
                status.innerHTML = '<i class="fas fa-circle me-1"></i>Active';
                    }else{
                        popup.error({
                    message:json.message
                });
                    }
                }else{
                     popup.error({
                    message:"something went wrong"
                });
                }
            }
        }

        async function deactivateFreelancer(id) {
            const popup = new Notification();
            if (confirm('Are you sure you want to deactivate this freelancer?')) {
                //console.log(id);
                const response = await fetch("deactivateFreelancer?id="+id);
                if(response.ok){
                    const json = await response.json();
                    if(json.status){
                         popup.success({
                    message:json.message
                });
                const status = document.getElementById("status-indicator");
                status.className = 'status-indicator status-inactive';
                status.innerHTML = '<i class="fas fa-circle me-1"></i>Inactive';
                    }else{
                        popup.error({
                    message:json.message
                });
                    }
                }else{
                    popup.error({
                    message:"something went wrong"
                });
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
            console.log('Freelancer profile page loaded');
        });
        
async function loadData(){
   const parameter = new URLSearchParams(window.location.search);
    
    if(parameter.has("frid") && parameter.has("uid")){
        let freelancerId = parameter.get("frid");
        let userId = parameter.get("uid");
        
        const response = await fetch("freelancerData?frid="+freelancerId+"&uid="+userId);
        
        if(response.ok){
            const json = await response.json();
            if(json.status){
                console.log(json);
                
                document.getElementById("profile").src="profile-images\\"+json.freelancer.user.id+"\\image1.png";
                document.getElementById("name").innerHTML=json.freelancer.user.username;
                document.getElementById("profile-id").innerHTML=json.freelancer.user.id;
                document.getElementById("country").innerHTML=json.freelancer.country.name;
                document.getElementById("join").innerHTML=json.userAdv.joined;
                document.getElementById("rate").innerHTML=json.freelancer.rating;
                document.getElementById("skill").innerHTML=json.freelancer.skill;
                document.getElementById("linkedIn").innerHTML=json.freelancer.linkedIn;
                document.getElementById("web").innerHTML=json.freelancer.portfolio;
                document.getElementById("bio").innerHTML=json.freelancer.description;
                document.getElementById("status").innerHTML=json.freelancer.availability;
                
                document.getElementById("activateFreelancer").addEventListener(
                "click", (e) => {
            activateFreelancer(json.freelancer.id);
            e.preventDefault();
        });
        
        document.getElementById("deactivateFreelancer").addEventListener(
                "click", (e) => {
            deactivateFreelancer(json.freelancer.id);
            e.preventDefault();
        });
            }else{
                console.log("error");
            }
        }else{
            console.log("something went wrong");
        }
        
    }
}


