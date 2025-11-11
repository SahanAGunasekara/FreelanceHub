// Dynamic Education Rows
        (function() {
            const educationList = document.getElementById('educationList');
            const addBtn = document.getElementById('addEducation');
            let index = 1;

            addBtn.addEventListener('click', function() {
                const row = document.createElement('div');
                row.className = 'row g-3 edu-pair';
                row.setAttribute('data-index', index);
                row.innerHTML = `
                    <div class="col-md-8">
                        <label class="form-label" for="edu-${index}-qualification">Educational Qualification</label>
                        <input type="text" id="edu-${index}-qualification" class="form-control" placeholder="e.g., MSc in Data Science">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="edu-${index}-level">Level</label>
                        <select id="edu-${index}-level" class="form-select">
                            <option value="">Select level</option>
                            <option>Diploma</option>
                            <option>Bachelor</option>
                            <option>Master</option>
                            <option>PhD</option>
                        </select>
                    </div>`;
                educationList.appendChild(row);
                index++;
            });
        })();

        

async function loadData(){
    
    const response = await fetch("loaduserProfile");
    
    if(response.ok){
        const json = await response.json();
        //console.log(json);
        
        const cSelector = document.getElementById("country");
        json.countryList.forEach(country=>{
                 let option = document.createElement("option");
                 option.innerHTML=country.name;
                 option.value=country.id;
                 cSelector.appendChild(option);
             });
    }else{
        console.log(error);
    }
}

async function registerFreelancer(){
    const popup = new Notification();
    
    const title = document.getElementById("title").value;
    const availability = document.getElementById("availability").checked;
    const skills = document.getElementById("skills").value;
    const linkedIn_url = document.getElementById("linkedin").value;
    const portfolio_url = document.getElementById("portfolio").value;
    const countryId = document.getElementById("country").value;
    const description = document.getElementById("description").value;
    const porfolio = document.getElementById("portfolioPdf").files[0];
    
    const educationData = [];
    const rows = document.querySelectorAll(".edu-pair");
    
    
    rows.forEach(row => {
        const index = row.getAttribute("data-index");
        const qualification = document.getElementById(`edu-${index}-qualification`).value.trim();
        const level = document.getElementById(`edu-${index}-level`).value;

        if (qualification !== "" && level !== "") {
            educationData.push({
                qualification: qualification,
                level: level
            });
//            console.log(qualification);
//            console.log(level);
            
        }
    });
    
    const qualificationData = JSON.stringify(educationData);
    
    
    const form = new FormData();
    form.append("title",title);
    form.append("availability",availability);
    form.append("skills",skills);
    form.append("linkedInURL",linkedIn_url);
    form.append("portfolioURL",portfolio_url);
    form.append("country",countryId);
    form.append("description",description);
    form.append("portfolioPDF",porfolio);
    form.append("qualifications",qualificationData);
    
    const response  = await fetch("saveFreelancer",{
        method:"POST",
        body:form
    });
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            console.log("Freelancer Saved succesfully");
            popup.success({
                    message:"Freelancer Saved succesfully"
                });
            //console.log(json.message);
        }else{
            console.log(json.message);
            popup.error({
                message:json.message
            });
        }
    }else{
        console.log("Something went wrong");
        popup.error({
                message:"Something went wrong"
            });
    }
    
}