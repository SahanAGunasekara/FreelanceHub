// Profile Image Upload
        document.getElementById('avatarInput').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('profileAvatar').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });

 

        // Delete Account Confirmation
        document.getElementById('deleteConfirmation').addEventListener('input', function(e) {
            const confirmButton = document.getElementById('confirmDelete');
            confirmButton.disabled = e.target.value !== 'DELETE';
        });
        
        document.getElementById('confirmDelete').addEventListener('click', function() {
            alert('Account deletion initiated. This process cannot be undone.');
            // Simulate account deletion
            console.log('Account deletion confirmed');
        });

        // Register as Freelancer
        document.getElementById('registerFreelancer').addEventListener('click', function() {
            window.location="registerFreelancer.html";
        });

        // Notification Preferences
        document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                console.log(`${this.id}: ${this.checked}`);
            });
        });

        // Social Account Connections
        document.querySelectorAll('.social-account').forEach(account => {
            account.addEventListener('click', function() {
                if (!this.classList.contains('connected')) {
                    this.classList.add('connected');
                    this.querySelector('small').textContent = 'Connected';
                    console.log('Social account connected');
                }
            });
        });

        // Initialize tooltips and other Bootstrap components
        document.addEventListener('DOMContentLoaded', function() {
            // Add any additional initialization here
            //console.log('User profile page loaded successfully!');
        });
 
 async function loadData(){
     const response = await fetch("loaduserProfile");
     
     if(response.ok){
         const json = await response.json();
         if (json.status){
             console.log(json);
             document.getElementById("profileAvatar").src="profile-images\\"+json.userId+"\\image1.png";
             document.getElementById("username").innerHTML=json.userName;
             document.getElementById("joinDate").innerHTML=json.joined;
             document.getElementById("fullName").value=json.userName;
             document.getElementById("usernameInput").value=json.userName;
             document.getElementById("email").value=json.email;
             document.getElementById("phone").value=json.mobile;
             document.getElementById("bio").value=json.bio;
             document.getElementById("country").value=json.cid;
             
             if (json.verifyStatus == "verified"){
                 document.getElementById("verifyStatus").innerHTML=json.verifyStatus;
                 document.getElementById("email").disabled=true;
             }else{
                 document.getElementById("verifyStatus").innerHTML="Unerified";
                 document.getElementById("verifyStatus").className="verification-status unverified";
             }
             
             const selector = document.getElementById("country");
             json.countryList.forEach(country=>{
                 let option = document.createElement("option");
                 option.innerHTML=country.name;
                 option.value=country.id;
                 selector.appendChild(option);
             });
             
         }else{
             console.log(json.message);
         }
     }else{
         console.log("error");
     }
 }
 
 async function updateUser(){
     //console.log("Ok");
     const popup = new Notification();
     const userName = document.getElementById("usernameInput").value;
     const email = document.getElementById("email").value;
     const mobile = document.getElementById("phone").value;
     const countryId = document.getElementById("country").value;
     const description = document.getElementById("bio").value;
     const dpImage = document.getElementById("avatarInput").files[0];
     
     
     const form = new FormData();
     form.append("username",userName);
     form.append("email",email);
     form.append("mobile",mobile);
     form.append("country",countryId);
     form.append("bio",description);
     form.append("image",dpImage);
     
     const response = await fetch("updateUser",{
         method:"POST",
         body:form
     });
     
     if (response.ok) {
        const json = await response.json();
        
        if(json.status){
            popup.success({
                message:"uccessfully Updated"
            });
            //console.log("Successfully Updated");
        }else{
            popup.warning({
                message:json.message
            });
            //console.log(json.message);
        }
    }else{
        popup.error({
                message:"error"
            });
        //console.log("error");
    }
     
 }
 
 async function sendCode(){
     //console.log("Send Code");
     const popup = new Notification();
    const response = await fetch("verify");
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            popup.success({
                message:json.message
            });
        }else{
            popup.error({
                message:"Something went wrong"
            });
        }
    }
 }
 
 async function verifyAccount(){
     const verifyCode = document.getElementById("verifyCode").value;
     
     const response = await fetch("verifyAccount?code="+verifyCode);
     
     if(response.ok){
         const json = await response.json();
         if(json.status){
             console.log("Email verified succesfully");
         }else{
             console.log(json.message);
         }
         
     }else{
         console.log("Error");
     }
 }
 
 async function sendPcode(){
     const response = await fetch("changePasswordCode");
     
     if(response.ok){
         const json = await response.json();
         if(json.status){
             console.log("Code send Successfully. Check your mail");
         }else{
             console.log(json.message);
         }
     }else{
         console.log("Error sending code");
     }
 }
 
 async function changePassword(){
     let fullCode="";
     for (let i = 1; i <= 6; i++) {
        fullCode+=document.getElementById("scode" + i).value;
     }
    //console.log(fullCode);
    
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    
    const passObject = {
        passCode:fullCode,
        newPasswrd:newPassword,
        confPasswrd:confirmPassword
    };
    
    const passerdJson = JSON.stringify(passObject);
    
    const response  = await fetch("updateNewPassword",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:passerdJson
    });
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            console.log("password updated successfully");
        }else{
            console.log(json.message);
        }
    }else{
        console.log("Error");
    }
    
    const modal = bootstrap.Modal.getInstance(document.getElementById('changePasswordModal'));
    modal.hide();
    document.getElementById('passwordForm').reset();
 }
 
 async function freelancerDashboard(){
     //console.log("redirection OK");
     const popup = new Notification();
     const response = await fetch("navigatefreelanceDashboard");
     
     if(response.ok){
         const json = await response.json();
         if(json.status){
             window.location="freelancerDashboard.html";
         }else{
             //console.log(json.message);
             popup.error({
                message:json.message
            });
         }
     }else{
         //console.log("navigation fail");
         popup.error({
                message:"navigation fail"
            });
     }
 }
 