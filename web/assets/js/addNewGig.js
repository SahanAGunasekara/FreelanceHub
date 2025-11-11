// Global variables
        let tags = [];
        let uploadedImages = {};

        // Tags functionality
        document.getElementById('tagsInput').addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ',') {
                e.preventDefault();
                addTag();
            }
        });

        function addTag() {
            const input = document.getElementById('tagsInput');
            const tagValue = input.value.trim();
            
            if (tagValue && !tags.includes(tagValue)) {
                tags.push(tagValue);
                renderTags();
                input.value = '';
            }
        }

        function removeTag(tagToRemove) {
            tags = tags.filter(tag => tag !== tagToRemove);
            renderTags();
        }

        function renderTags() {
            const container = document.getElementById('tagsContainer');
            container.innerHTML = '';
            
            tags.forEach(tag => {
                const badge = document.createElement('span');
                badge.className = 'tag-badge';
                badge.innerHTML = `${tag} <span class="remove-tag" onclick="removeTag('${tag}')">&times;</span>`;
                container.appendChild(badge);
            });
        }

        // Image upload functionality
        document.querySelectorAll('.image-upload-box').forEach(box => {
            const input = box.querySelector('input[type="file"]');
            const index = box.getAttribute('data-index');

            // Click to upload
            box.addEventListener('click', () => {
                input.click();
            });

            // File selection
            input.addEventListener('change', (e) => {
                handleImageUpload(e.target.files[0], index, box);
            });

            // Drag and drop
            box.addEventListener('dragover', (e) => {
                e.preventDefault();
                box.classList.add('dragover');
            });

            box.addEventListener('dragleave', () => {
                box.classList.remove('dragover');
            });

            box.addEventListener('drop', (e) => {
                e.preventDefault();
                box.classList.remove('dragover');
                const files = e.dataTransfer.files;
                if (files.length > 0) {
                    handleImageUpload(files[0], index, box);
                }
            });
        });
        
        let img1="";
        let img2="";
        let img3="";

        function handleImageUpload(file, index, box) {
            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();
                if(index==0){
                        img1=file;
                    }else if(index==1){
                        img2=file;
                    }else if(index==2){
                        img3=file;
                    }
                reader.onload = (e) => {
                    
                    uploadedImages[index] = {
                        
                        file: file,
                        dataUrl: e.target.result
                    };
                    
                    // Update box content to show preview
                    box.innerHTML = `
                        <div class="image-container">
                            <img src="${e.target.result}" class="image-preview" alt="Preview">
                            <button type="button" class="remove-image" onclick="removeImage(${index})">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    `;
                };
                reader.readAsDataURL(file);
            }
        }

        function removeImage(index) {
            delete uploadedImages[index];
            const box = document.querySelector(`[data-index="${index}"]`);
            box.innerHTML = `
                <i class="fas fa-cloud-upload-alt fa-2x text-muted mb-2"></i>
                <p class="mb-1">Upload Image ${parseInt(index) + 1}</p>
                <small class="text-muted">Click or drag & drop</small>
                <input type="file" class="d-none" accept="image/*" data-index="${index}">
            `;
            
            // Re-attach event listeners
            const input = box.querySelector('input[type="file"]');
            box.addEventListener('click', () => input.click());
            input.addEventListener('change', (e) => handleImageUpload(e.target.files[0], index, box));
        }

        

        // Navigation function
        function goBack() {
            // In a real application, this would navigate to the dashboard
            window.location.href = 'freelancer-dashboard.html';
        }
        
async function loadData(){
    //console.log("Selector loading")
    const response = await fetch("loadCategory");
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            //console.log(json);
            const selector = document.getElementById("category") ;
            json.category.forEach(category=>{
                let option = document.createElement("option");
                option.innerHTML=category.name;
                option.value=category.id;
                selector.appendChild(option);
            });
       }else{
            console.log(json.message);
        }
    }else{
        console.log("Try again");
    }
}

async function createGig(){
    //console.log(tags);
    const popup = new Notification();
    const title = document.getElementById("gigTitle").value;
    const price = document.getElementById("price").value;
    const deliveryTime = document.getElementById("deliveryTime").value;
    const category = document.getElementById("category").value;
    const description = document.getElementById("description").value;
    
//     console.log(img1);
//     console.log(img2);
//     console.log(img3);
    
    
    const form = new FormData();
    form.append("title",title);
    form.append("price",price);
    form.append("delivery",deliveryTime);
    form.append("categoryId",category);
    form.append("description",description);
    form.append("tags",tags);
    form.append("image1",img1);
    form.append("image2",img2);
    form.append("image3",img3);
    //form.append("Images",JSON.stringify(uploadedImages));
    
    
    const response = await fetch("saveGig",{
        method:"POST",
        body:form
    });
    
    if(response.ok){
        const json = await response.json();
        if(json.status){
            popup.success({
                message:"Product saved Successfully"
            });
            //console.log("Succesfully saved");
            
            document.getElementById("gigTitle").value=" ";
            document.getElementById("price").value="0.00";
            document.getElementById("deliveryTime").value=0;
            document.getElementById("category").value=0;
            document.getElementById("description").value=" ";
            document.getElementById("tagsContainer").innerHTML=" ";
            document.getElementById("description").value=" ";
            img1="";
            img2="";
            img3="";
            
            
        }else{
            popup.error({
                message:json.message
            });
            //console.log(json.message);
        }
    }else{
        popup.error({
                message:"Please try again"
            });
        //console.log("Please try again");
    }
    
}




