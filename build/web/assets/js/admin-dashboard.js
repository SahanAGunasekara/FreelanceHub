 // DOM elements
        const navLinks = document.querySelectorAll('.nav-link');
        const tabContents = document.querySelectorAll('.tab-content');
        const pageTitle = document.getElementById('pageTitle');
        const sidebar = document.getElementById('sidebar');

        // Tab switching functionality
        navLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                
                // Remove active class from all links and tabs
                navLinks.forEach(l => l.classList.remove('active'));
                tabContents.forEach(tab => tab.classList.remove('active'));
                
                // Add active class to clicked link
                this.classList.add('active');
                
                // Show corresponding tab
                const tabId = this.getAttribute('data-tab') + '-tab';
                const targetTab = document.getElementById(tabId);
                targetTab.classList.add('active');
                
                // Update page title
                pageTitle.textContent = this.textContent.trim();
                
                // Smooth scroll to the content
                setTimeout(() => {
                    targetTab.scrollIntoView({ 
                        behavior: 'smooth', 
                        block: 'start' 
                    });
                }, 100);
            });
        });

        // Print functionality
        function printTable() {
            window.print();
//            const printId = document.getElementById("printable");
//            printElement(printId);
        }
        
//        function printElement() {
//            var element = document.getElementById("printable");
//            var clonedElement = element.cloneNode(true);
//            var iframe = document.createElement('iframe');
//            iframe.style.display = 'none';
//            document.body.appendChild(iframe);
//            var iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
//            iframeDoc.body.appendChild(clonedElement);
//            iframe.contentWindow.print();
//            document.body.removeChild(iframe);
//         }

        // Add category functionality
        async function addCategory() {
            //console.log("OK");
            const popup = new Notification();
            const category = document.getElementById("categoryName").value;
            const pword = document.getElementById("password").value;
            
            const catData={
                category:category,
                password:pword
            };
            
            const categoryData = JSON.stringify(catData);
            
            const response = await fetch("addCategory",{
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body:categoryData
            });
            
            if(response.ok){
                const json = await response.json();
                if(json.status){
                    popup.success({
                    message:json.message
                });
                document.getElementById("categoryName").value="";
                document.getElementById("password").value="";
                }else{
                    popup.error({
                    message:json.message
                });
                }
            }else{
                popup.error({
                    message:"error"
                });
            }
        }

        // Mobile sidebar toggle
        function toggleSidebar() {
            sidebar.classList.toggle('show');
        }

        // Close sidebar when clicking outside on mobile
        document.addEventListener('click', function(e) {
            if (window.innerWidth <= 768) {
                if (!sidebar.contains(e.target) && !e.target.classList.contains('mobile-toggle')) {
                    sidebar.classList.remove('show');
                }
            }
        });

        // Handle window resize
        window.addEventListener('resize', function() {
            if (window.innerWidth > 768) {
                sidebar.classList.remove('show');
            }
        });

        // Initialize dashboard
        document.addEventListener('DOMContentLoaded', function() {
            // Set initial active tab
            const activeTab = document.querySelector('.nav-link.active');
            if (activeTab) {
                pageTitle.textContent = activeTab.textContent.trim();
            }
        });

async function loadUserData(){
    //console.log("OK");
    const response = await fetch("loadAdminPageData");
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            console.log(json.userList);
            console.log(json.freelancerList);
            console.log(json.gigList);
            const table_Body  = document.getElementById("table-body");
            table_Body.innerHTML="";
            json.userList.forEach(user=>{
                let table_row =`<tr>
                                        <td><img src="${'profile-images\\'+user.id+'\\image1.png'}" class="profile-img" alt="${user.username}"></td>
                                        <td>${user.username}</td>
                                        <td>${user.email}</td>
                                        <td><span class="badge bg-primary">${user.role.name}</span></td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                                            <button class="btn btn-sm btn-outline-danger">Delete</button>
                                        </td>
                                    </tr>`;
                table_Body.innerHTML+=table_row;
                
            });
            
            const card_container = document.getElementById("card-container");
            card_container.innerHTML="";
            json.freelancerList.forEach(freelancer=>{
                let card=`<div class="freelancer-card">
                            <div class="card-header">
                                <img src="${'profile-images\\'+freelancer.user.id+'\\image1.png'}" class="card-profile-img" alt="John">
                                <div class="card-info">
                                    <h5>${freelancer.user.username}</h5>
                                    <p>${freelancer.skill}</p>
                                    <small>Joined: Jan 15, 2024</small>
                                </div>
                            </div>
                            <div class="d-flex justify-content-between">
                                <button class="btn btn-sm btn-outline-primary" onclick="viewProfile(${freelancer.id},${freelancer.user.id});">View Profile</button>
                                <button class="btn btn-sm btn-outline-success">Approve</button>
                            </div>
                        </div>`;
                card_container.innerHTML+=card;
               
            });
            
            const gig_container = document.getElementById("gig-container");
            gig_container.innerHTML="";
            json.gigList.forEach(gig=>{
                let gigCard=`<div class="gig-card">
                               <a href="${'admin-gigView.html?id=' + gig.id}">
                            <img src="${'Gig-Images\\'+gig.id+'\\image1.png'}" class="gig-image" alt="Web Design">
                            </a>
                            <h5>${gig.title}</h5>
                            <p>${gig.description}</p>
                            <div class="d-flex justify-content-between">
                                <span class="badge bg-success">${gig.status.status}</span>
                                <button class="btn btn-sm btn-outline-primary">Edit</button>
                            </div>
                        </div>`;
                gig_container.innerHTML+=gigCard;
            });
            
            document.getElementById("total-users").innerHTML=json.userCount;
            document.getElementById("total-freelancers").innerHTML=json.freelancerCount;
            document.getElementById("active-gigs").innerHTML=json.activeGigCount;
        }else{
            console.log("Error");
        }
    }else{
        console.log("Something went wrong");
    }
}

function viewProfile(frid,uid){
    window.location="freelancer-profile.html?frid=" + frid + "&uid=" + uid;
}

