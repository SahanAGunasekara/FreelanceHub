// script.js
// Placeholder for custom JS. Bootstrap handles most interactivity.
// You can add custom scripts here if needed in the future. 


function changePage(){
    window.location ="Search.html";
}


document.addEventListener('DOMContentLoaded', function() {
    // Sidebar Toggle
    const sidebarCollapse = document.getElementById('sidebarCollapse');
    const sidebar = document.getElementById('sidebar');
    
    if (sidebarCollapse) {
        sidebarCollapse.addEventListener('click', function() {
            sidebar.classList.toggle('active');
        });
    }

    // Handle window resize
    window.addEventListener('resize', function() {
        if (window.innerWidth <= 768) {
            sidebar.classList.add('active');
        } else {
            sidebar.classList.remove('active');
        }
    });

    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Add hover effect to cards
    const cards = document.querySelectorAll('.project-card, .freelancer-card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Handle notification clicks
    const notificationBell = document.querySelector('.notifications');
    if (notificationBell) {
        notificationBell.addEventListener('click', function() {
            // Add your notification panel logic here
            alert('Notifications panel would open here');
        });
    }

    // Handle message clicks
    

    // Handle profile image click
    const profileImage = document.querySelector('.profile img');
    if (profileImage) {
        profileImage.addEventListener('click', function() {
            // Add your profile dropdown logic here
            alert('Profile dropdown would open here');
        });
    }

    // Handle "Create New Project" button
    const createProjectBtn = document.querySelector('.welcome-section .btn-primary');
    if (createProjectBtn) {
        createProjectBtn.addEventListener('click', function() {
            // Add your project creation logic here
            alert('Project creation form would open here');
        });
    }

    // Handle "Invite to Job" buttons
    const inviteButtons = document.querySelectorAll('.freelancer-card .btn-primary');
    inviteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const freelancerName = this.closest('.card-body').querySelector('.card-title').textContent;
            // Add your invitation logic here
            alert(`Invitation would be sent to ${freelancerName}`);
        });
    });

    // Handle search functionality
    const searchInput = document.querySelector('.search-container input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                // Add your search logic here
                alert(`Searching for: ${this.value}`);
            }
        });
    }
}); 

document.addEventListener('DOMContentLoaded', function() {
    // Sidebar Toggle
    const sidebarCollapse = document.getElementById('sidebarCollapse');
    const sidebar = document.getElementById('sidebar');
    
    if (sidebarCollapse) {
        sidebarCollapse.addEventListener('click', function() {
            sidebar.classList.toggle('active');
        });
    }

    // Handle window resize
    window.addEventListener('resize', function() {
        if (window.innerWidth <= 768) {
            sidebar.classList.add('active');
        } else {
            sidebar.classList.remove('active');
        }
    });

    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Add hover effect to cards
    const cards = document.querySelectorAll('.project-card, .freelancer-card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Handle notification clicks
    const notificationBell = document.querySelector('.notifications');
    if (notificationBell) {
        notificationBell.addEventListener('click', function() {
            // Add your notification panel logic here
            alert('Notifications panel would open here');
        });
    }

    // Handle message clicks
    const messageIcon = document.querySelector('.messages');
    if (messageIcon) {
        messageIcon.addEventListener('click', function() {
            const messagePanel = new bootstrap.Modal(document.getElementById('messagePanel'));
            messagePanel.show();
        });
    }

    // Handle profile image click
    const profileImage = document.querySelector('.profile img');
    if (profileImage) {
        profileImage.addEventListener('click', function() {
            // Add your profile dropdown logic here
            alert('Profile dropdown would open here');
        });
    }

    // Handle "Create New Project" button
    const createProjectBtn = document.querySelector('.welcome-section .btn-primary');
    if (createProjectBtn) {
        createProjectBtn.addEventListener('click', function() {
            // Add your project creation logic here
            alert('Project creation form would open here');
        });
    }

    // Handle "Invite to Job" buttons
    const inviteButtons = document.querySelectorAll('.freelancer-card .btn-primary');
    inviteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const freelancerName = this.closest('.card-body').querySelector('.card-title').textContent;
            // Add your invitation logic here
            alert(`Invitation would be sent to ${freelancerName}`);
        });
    });

    // Handle search functionality
    const searchInput = document.querySelector('.search-container input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                // Add your search logic here
                alert(`Searching for: ${this.value}`);
            }
        });
    }

    // Message Panel Functionality
    const messagePanel = document.getElementById('messagePanel');
    if (messagePanel) {
        // Handle conversation selection
        const conversationItems = document.querySelectorAll('.conversation-item');
        conversationItems.forEach(item => {
            item.addEventListener('click', function() {
                // Remove active class from all items
                conversationItems.forEach(i => i.classList.remove('active'));
                // Add active class to clicked item
                this.classList.add('active');
                
                // Update chat header with selected user's info
                const userName = this.querySelector('h6').textContent;
                const userImage = this.querySelector('img').src;
                const chatHeader = document.querySelector('.chat-header');
                chatHeader.querySelector('h6').textContent = userName;
                chatHeader.querySelector('img').src = userImage;
            });
        });

        // Handle message sending
        const chatInput = document.querySelector('.chat-input input');
        const sendButton = document.querySelector('.chat-input .btn');
        
        function sendMessage() {
            const message = chatInput.value.trim();
            if (message) {
                const chatMessages = document.querySelector('.chat-messages');
                const messageHTML = `
                    <div class="message sent">
                        <div class="message-content">
                            <p>${message}</p>
                            <small class="text-muted">${new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</small>
                        </div>
                    </div>
                `;
                chatMessages.insertAdjacentHTML('beforeend', messageHTML);
                chatInput.value = '';
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        }

        if (sendButton) {
            sendButton.addEventListener('click', sendMessage);
        }

        if (chatInput) {
            chatInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    sendMessage();
                }
            });
        }

        // Handle conversation search
        const conversationSearch = document.querySelector('.search-messages input');
        if (conversationSearch) {
            conversationSearch.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase();
                const conversations = document.querySelectorAll('.conversation-item');
                
                conversations.forEach(conversation => {
                    const userName = conversation.querySelector('h6').textContent.toLowerCase();
                    const messagePreview = conversation.querySelector('p').textContent.toLowerCase();
                    
                    if (userName.includes(searchTerm) || messagePreview.includes(searchTerm)) {
                        conversation.style.display = 'flex';
                    } else {
                        conversation.style.display = 'none';
                    }
                });
            });
        }
    }
}); 

document.addEventListener('DOMContentLoaded', function() {
    // Sidebar Toggle
    const sidebarCollapse = document.getElementById('sidebarCollapse');
    const sidebar = document.getElementById('sidebar');
    
    if (sidebarCollapse) {
        sidebarCollapse.addEventListener('click', function() {
            sidebar.classList.toggle('active');
        });
    }

    // Handle window resize
    window.addEventListener('resize', function() {
        if (window.innerWidth <= 768) {
            sidebar.classList.add('active');
        } else {
            sidebar.classList.remove('active');
        }
    });

    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Handle notification clicks
    const notificationBell = document.querySelector('.notifications');
    if (notificationBell) {
        notificationBell.addEventListener('click', function() {
            // Add your notification panel logic here
            alert('Notifications panel would open here');
        });
    }

    // Handle profile image click
    const profileImage = document.querySelector('.profile img');
    if (profileImage) {
        profileImage.addEventListener('click', function() {
            // Add your profile dropdown logic here
            alert('Profile dropdown would open here');
        });
    }

    

    // Handle Add Skill button
    const addSkillBtn = document.querySelector('.skills-card .btn-outline-primary');
    if (addSkillBtn) {
        addSkillBtn.addEventListener('click', function() {
            const skill = prompt('Enter your skill:');
            if (skill) {
                const skillsContainer = document.querySelector('.skills-tags');
                const skillBadge = document.createElement('span');
                skillBadge.className = 'badge bg-primary me-2 mb-2';
                skillBadge.textContent = skill;
                skillsContainer.insertBefore(skillBadge, addSkillBtn);
            }
        });
    }

    // Handle View Details buttons in Active Projects
    const viewDetailsButtons = document.querySelectorAll('.table .btn-outline-primary');
    viewDetailsButtons.forEach(button => {
        button.addEventListener('click', function() {
            const projectName = this.closest('tr').querySelector('h6').textContent;
            // Add your project details logic here
            alert(`Viewing details for project: ${projectName}`);
        });
    });

    // Handle Portfolio Actions
    const portfolioItems = document.querySelectorAll('.portfolio-item');
    portfolioItems.forEach(item => {
        const viewButton = item.querySelector('.fa-eye').parentElement;
        const editButton = item.querySelector('.fa-edit').parentElement;

        viewButton.addEventListener('click', function(e) {
            e.stopPropagation();
            const projectName = item.querySelector('h6').textContent;
            // Add your portfolio view logic here
            alert(`Viewing portfolio item: ${projectName}`);
        });

        editButton.addEventListener('click', function(e) {
            e.stopPropagation();
            const projectName = item.querySelector('h6').textContent;
            // Add your portfolio edit logic here
            alert(`Editing portfolio item: ${projectName}`);
        });
    });

    // Handle Add Project button
    const addProjectBtn = document.querySelector('.card-header .btn-primary');
    if (addProjectBtn) {
        addProjectBtn.addEventListener('click', function() {
            // Add your project creation logic here
            alert('Project creation form would open here');
        });
    }

    // Handle search functionality
    const searchInput = document.querySelector('.search-container input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                // Add your job search logic here
                alert(`Searching for jobs: ${this.value}`);
            }
        });
    }

    // Add hover effect to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            if (!this.classList.contains('profile-card')) {
                this.style.transform = 'translateY(-5px)';
            }
        });
        card.addEventListener('mouseleave', function() {
            if (!this.classList.contains('profile-card')) {
                this.style.transform = 'translateY(0)';
            }
        });
    });
}); 

function loadDashboard(){
    //console.log("OK");
    window.location="dashboard.html";
}

async function signOut(){
    //console.log("OK");
    const popup = new Notification();
    const response = await fetch("SignOut");
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            window.location="sign-in.html";
        }else{
            window.location.reload;
        }
    }else{
        popup.error({
                message:"Logout failed"
            });
    }
}

async function addSessionFav(){
    //console.log("OK");
    const response = await fetch("addSesssionFav");
    
    if(response.ok){
        
    }else{
        
    }
}

async function loadSearch(){
    const keyword = document.getElementById("keyword").value;
    
    const response = await fetch("loadSearchIndex?key="+keyword);
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            console.log(json);
            const load_card_container = document.getElementById("container");
            load_card_container.innerHTML="";
            json.gigList.forEach(gig=>{
                let card =`<div class="col-lg-3 col-md-6 mb-4" id="similer-gig mt-5 ms-4">
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
            <span class="text-success fw-bold">Rs.${gig.price}</span>
            <small class="text-muted">by ${gig.freelancer.user.username}</small>
          </div>
        </div>
      </div>
    </div>`;
                load_card_container.innerHTML+=card;
            });
        }else{
            console.log("error");
        }
    }else{
        console.log("something went wrong");
    }
}

window.addEventListener('load',async function(){
    //console.log("OK");
    
    const response = await fetch("loadProfileData");
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            console.log(json.userPData);
            document.getElementById("profImg").src="profile-images\\"+json.userPData.id+"\\image1.png";
            document.getElementById("pImage").src="profile-images\\"+json.userPData.id+"\\image1.png";
            document.getElementById("dName").innerHTML=json.userPData.username;
            document.getElementById("profImg").src="profile-images\\"+json.userPData.id+"\\image1.png";
        }else{
            console.log(json.message);
        }
    }else{
        console.log("Something went wrong");
    }
    
});



