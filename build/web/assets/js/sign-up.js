async function signUp(){
    //console.log("OK");
    const popup = new Notification();
    const uname = document.getElementById("username").value;
    const mail = document.getElementById("email").value;
    const pword = document.getElementById("password").value;
    const cpword = document.getElementById("confirmPassword").value;
    const role = document.getElementById("roleSelect").value;
    
//    console.log(uname);
//    console.log(mail);
//    console.log(pword);
//    console.log(cpword);
//    console.log(role);
    
    const userdata = {
      userName:uname,
      email:mail,
      password:pword,
      confirmPassword:cpword,
      userRole:role
    };
    
    const userJson = JSON.stringify(userdata);
    //console.log(userJson);
    
    const response = await fetch("UserRegister",{
        method:"POST",
        headers: {
            "Content-Type": "application/json"
        },
        body:userJson
    });
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            //console.log(response.message);
            window.location = "index.html";
        }else{
            popup.error({
                message:json.message
            });
        }
    }else{
        popup.error({
                message:"User Registration failed"
            });
    }
    
    
}


window.addEventListener("load", async function(){
    //console.log("listener load");
    const response = await fetch("addRole");
    
    if(response.ok){
        const resjson = await response.json();
        
        //console.log(resjson);
        const roleSelect = document.getElementById("roleSelect");
        resjson.forEach(role => {
            let option = document.createElement("option");
            option.innerHTML=role.name;
            option.value=role.id;
            roleSelect.appendChild(option);
        });
    }
});