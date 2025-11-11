// Sample gigs data
        const sampleGigs = [
            {
                id: 1,
                title: "Professional Web Design",
                category: "Web Design",
                status: "active",
                dateCreated: "2024-01-15",
                price: "$500"
            },
            {
                id: 2,
                title: "Mobile App Development",
                category: "Mobile Development",
                status: "active",
                dateCreated: "2024-01-10",
                price: "$1,200"
            },
            {
                id: 3,
                title: "Logo Design Package",
                category: "Graphic Design",
                status: "paused",
                dateCreated: "2024-01-05",
                price: "$200"
            },
            {
                id: 4,
                title: "E-commerce Website",
                category: "Web Development",
                status: "completed",
                dateCreated: "2023-12-20",
                price: "$800"
            }
        ];

        // Initialize dashboard
        document.addEventListener('DOMContentLoaded', function() {
            renderGigs();
            initializeSidebar();
        });

        // Render gigs
        function renderGigs() {
            const gigsList = document.getElementById('gigsList');
            gigsList.innerHTML = '';

            sampleGigs.forEach(gig => {
                const gigCard = createGigCard(gig);
                gigsList.appendChild(gigCard);
            });
        }

        // Create gig card
        function createGigCard(gig) {
            const card = document.createElement('div');
            card.className = 'gig-card';
            card.innerHTML = `
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <div class="gig-title">${gig.title}</div>
                    <span class="status-badge status-${gig.status}">${gig.status.charAt(0).toUpperCase() + gig.status.slice(1)}</span>
                </div>
                <div class="gig-category">${gig.category}</div>
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <small class="text-muted">Created: ${new Date(gig.dateCreated).toLocaleDateString()}</small>
                    </div>
                    <div class="col-md-6 text-end">
                        <button class="btn btn-view btn-action" onclick="viewGig(${gig.id})">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-edit btn-action" onclick="editGig(${gig.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-delete btn-action" onclick="deleteGig(${gig.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `;
            return card;
        }

        // Add new gig
        function addNewGig() {
            // In a real application, this would navigate to add-gig.html or open a modal
            alert('Redirecting to Add New Gig page...');
            console.log('Navigate to add-gig.html');
        }

        // View gig details
        function viewGig(id) {
            const gig = sampleGigs.find(g => g.id === id);
            alert(`Viewing details for: ${gig.title}`);
            console.log('View gig:', gig);
        }

        // Edit gig
        function editGig(id) {
            const gig = sampleGigs.find(g => g.id === id);
            alert(`Editing: ${gig.title}`);
            console.log('Edit gig:', gig);
        }

        // Delete gig
        function deleteGig(id) {
            if (confirm('Are you sure you want to delete this gig?')) {
                const index = sampleGigs.findIndex(g => g.id === id);
                if (index > -1) {
                    sampleGigs.splice(index, 1);
                    renderGigs();
                    alert('Gig deleted successfully!');
                }
            }
        }

        // Sidebar functionality
        function initializeSidebar() {
            const sidebarToggle = document.getElementById('sidebarToggle');
            const sidebar = document.getElementById('sidebar');

            sidebarToggle.addEventListener('click', function() {
                sidebar.classList.toggle('show');
            });

            // Close sidebar when clicking outside on mobile
            document.addEventListener('click', function(e) {
                if (window.innerWidth <= 768) {
                    if (!sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                        sidebar.classList.remove('show');
                    }
                }
            });

            // Handle navigation clicks
            document.querySelectorAll('.nav-link').forEach(link => {
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    
                    // Remove active class from all links
                    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
                    
                    // Add active class to clicked link
                    this.classList.add('active');
                    
                    // Handle navigation (in a real app, this would navigate to different pages)
                    const href = this.getAttribute('href');
                    console.log('Navigate to:', href);
                    
                    // Close sidebar on mobile after navigation
                    if (window.innerWidth <= 768) {
                        sidebar.classList.remove('show');
                    }
                });
            });
        }

        // Update stats dynamically (example)
        function updateStats() {
            // This could be called periodically to update stats
            console.log('Updating dashboard stats...');
        }
        
async function loadfreelancerData(){
    //console.log("frelancer date loading");
    const response  = await fetch ("loadFreelancerData");
    
    if (response.ok) {
        const json = await response.json();
        if(json.status){
            console.log(json);
            document.getElementById("profile").src="profile-images\\"+json.freelancer.user.id+"\\image1.png";
            document.getElementById("name").innerHTML=json.freelancer.user.username;
            document.getElementById("title").innerHTML=json.freelancer.title;
            //document.getElementById("profile2").src="profile-images\\"+json.freelancer.user.id+"\\image1.png";
        }else{
            console.log(json.message);
            window.location="sign-in.html";
        }
    }else{
        console.log("Please try again");
    }
}

function addNewGig(){
    window.location="addNewGig.html";
}