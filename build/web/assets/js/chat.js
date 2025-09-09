
payhere.onCompleted = function onCompleted(orderId) {
        const popup = Notification();
        popup.success({
            message: "Payment completed. OrderID:" + orderId
         });
    };

    // Payment window closed
    payhere.onDismissed = function onDismissed() {
        // Note: Prompt user to pay again or show an error page
        console.log("Payment dismissed");
    };

    // Error occurred
    payhere.onError = function onError(error) {
        // Note: show an error page
        console.log("Error:"  + error);
    }; 




// DOM elements
        const chatMessages = document.getElementById('chatMessages');
        const messageInput = document.getElementById('messageInput');
        const sendButton = document.getElementById('sendButton');
        const typingIndicator = document.getElementById('typingIndicator');

        // Sample data - replace with backend data
        const currentUser = 'customer'; // 'customer' or 'freelancer'
        const otherUser = currentUser === 'customer' ? 'freelancer' : 'customer';

        // Initialize chat
        function initChat() {
            // Hide payment section for freelancer
            if (currentUser === 'freelancer') {
                document.getElementById('paymentSection').style.display = 'none';
            }
            
            // Update header based on user type
            updateHeader();
        }

        function updateHeader() {
            const header = document.querySelector('.user-details h6');
            const status = document.querySelector('.user-details small');
            
            if (currentUser === 'customer') {
                header.textContent = 'John Smith (Freelancer)';
                status.textContent = 'Web Developer • Online';
            } else {
                header.textContent = 'Sarah Johnson (Customer)';
                status.textContent = 'Project Manager • Online';
            }
        }

        // Send message function
//        function sendMessage() {
//            const message = messageInput.value.trim();
//            if (!message) return;
//
//            const timestamp = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
//            
//            // Create message element
//            const messageElement = createMessageElement(message, timestamp, 'sent');
//            
//            // Add to chat
//            chatMessages.appendChild(messageElement);
//            
//            // Clear input
//            messageInput.value = '';
//            
//            // Scroll to bottom
//            scrollToBottom();
//            
//            // Simulate typing indicator (for demo)
//            setTimeout(() => {
//                showTypingIndicator();
//                setTimeout(() => {
//                    hideTypingIndicator();
//                    // Simulate response
//                    const responses = [
//                        "Thanks for the message! I'll get back to you soon.",
//                        "Got it! Let me check on that for you.",
//                        "Perfect! I'll update you on the progress.",
//                        "I understand. Let me work on that right away."
//                    ];
//                    const randomResponse = responses[Math.floor(Math.random() * responses.length)];
//                    const responseTimestamp = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
//                    const responseElement = createMessageElement(randomResponse, responseTimestamp, 'received');
//                    chatMessages.appendChild(responseElement);
//                    scrollToBottom();
//                }, 2000);
//            }, 1000);
//        }

        function createMessageElement(text, timestamp, type) {
            const messageDiv = document.createElement('div');
            messageDiv.className = `message ${type}`;
            
            messageDiv.innerHTML = `
                <div class="message-bubble">
                    ${text}
                </div>
                <div class="message-time">${timestamp}</div>
            `;
            
            return messageDiv;
        }

        function showTypingIndicator() {
            typingIndicator.style.display = 'block';
            scrollToBottom();
        }

        function hideTypingIndicator() {
            typingIndicator.style.display = 'none';
        }

        function scrollToBottom() {
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        

        // Event listeners
        sendButton.addEventListener('click', sendMessage);
        
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });

        // Auto-resize input (optional)
        messageInput.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 100) + 'px';
        });

        // Initialize chat
        initChat();
        
        // Scroll to bottom on load
        setTimeout(scrollToBottom, 100);
        
        
        async function loadData(){
            
            const parameter = new URLSearchParams(window.location.search);
            if(parameter.has("id")){
                let gigId = parameter.get("id");
                const response = await fetch("loadChatData?id="+gigId);
                
                if(response.ok){
                    const json = await response.json();
                    if(json.status){
                        console.log(json);
                        const userName = json.gigObject.freelancer.user.username;
                        const role = json.gigObject.category.name;
                        document.getElementById("frname").innerHTML=userName+"(Freelancer)";
                        document.getElementById("frCategory").innerHTML=role;
                        document.getElementById("frid").innerHTML=json.gigObject.freelancer.user.id;
                        loadMessages(json.gigObject.freelancer.user.id);
                        document.getElementById("payment-button").addEventListener(
                "click",(e)=>{
                    proceedToPayment(json.gigObject.price,json.gigObject.title);
            e.preventDefault();
                }
                        );
                        
                    }else{
                        console.log("error");
                    }
                }else{
                    console.log("Something went wrong");
                }
            }
        }
        
        async function sendMsg(){
            //console.log(id);
            let gid=0;
            const parameter = new URLSearchParams(window.location.search);
            if(parameter.has("id")){
                gid = parameter.get("id");
            }
            const text = document.getElementById("messageInput").value;
            const id = document.getElementById("frid").innerHTML;
            
//            console.log(text);
//            console.log(id);
            
            const textData={
              msgText:text,
              recieverID:id,
              GigId:gid
            };
            
            const jsonData = JSON.stringify(textData);
            
            const response = await fetch("saveMessage",{
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body:jsonData
            });
            
            if(response.ok){
                const json = await response.json();
                if(json.status){
                    console.log(json.message);
                    document.getElementById("messageInput").value="";
                    loadMessages(id);
                }else{
                    console.log(json.message);
                }
            }else{
                console.log("error");
            }
            
        }
        
        async function loadMessages(rid){
            const popup = Notification();
            let gid=0;
            const parameter = new URLSearchParams(window.location.search);
            if(parameter.has("id")){
                gid = parameter.get("id");
            }
            
            const data ={
              recieverId:rid,
              gigId:gid
            };
            
            const jsonData = JSON.stringify(data);
            
            const response = await fetch("loadMessage",{
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body:jsonData
            });
            
            if(response.ok){
                const json = await response.json();
                if (json.status) {
    const chat_message_main = document.getElementById("chatMessages");
    chat_message_main.innerHTML = "";

    // Combine both lists with sender type
    const allMessages = [];

    json.senderChatList.forEach(sender => {
        allMessages.push({
            message: sender.message,
            time: sender.time,
            type: "sent"
        });
    });

    json.recieverChatList.forEach(receiver => {
        allMessages.push({
            message: receiver.message,
            time: receiver.time,
            type: "received"
        });
    });

    // Sort by time (assuming ISO format or comparable)
    allMessages.sort((a, b) => new Date(a.time) - new Date(b.time));

    // Render sorted messages
    allMessages.forEach(msg => {
        let chat = `<div class="message ${msg.type}">
            <div class="message-bubble">${msg.message}</div>
            <div class="message-time">${msg.time}</div>
        </div>`;
        chat_message_main.innerHTML += chat;
    });
}
else{
                    console.log(json.message);
                    popup.error({
                message:json.message
            });
                }
            }else{
                console.log("error");
            }
        }
        
        
        async function proceedToPayment(price,title) {
            const popup = Notification();
            let gid=0;
            const parameter = new URLSearchParams(window.location.search);
            if(parameter.has("id")){
                gid = parameter.get("id");
            }
            
            const pData={
                gigprice:price,
                gigTitle:title,
                gigId:gid
            };
            
            const paymentData = JSON.stringify(pData);
            
            const response = await fetch("Checkout",{
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                    
                },
                body:paymentData
            });
            
            if(response.ok){
                const json = await response.json();
                if(json.status){
                    
                    payhere.startPayment(json.payhereJson);
                }else{
                    console.log(json.message);
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


