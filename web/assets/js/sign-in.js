async function SignIn(){
    //console.log("OK");
    const popup = new Notification();
    const mail = document.getElementById("email").value;
    const pword = document.getElementById("password").value;
    
    const userData={
        email:mail,
        password:pword
    };
    
    const userJson = JSON.stringify(userData);
    //console.log(userJson);
    
    const response = await fetch("userLogin",
    {
        method:"POST",
        header:{
            "Content-Type":"application/json"
        },
        body:userJson
    }
    );
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            window.location = "index.html";
        }else{
            console.log(json.message);
            popup.error({
                message:json.message
            });
        }
    }
}


