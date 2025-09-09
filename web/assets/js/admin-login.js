 // DOM elements
        

        // Form validation and submission
//        loginForm.addEventListener('submit', function(e) {
//            e.preventDefault();
//            
//            // Reset messages
//            hideMessages();
//            
//            // Get form values
//            const username = usernameInput.value.trim();
//            const password = passwordInput.value.trim();
//            
//            // Validate fields
//            if (!username || !password) {
//                showError('Please fill in all fields.');
//                return;
//            }
//            
//            // Show loading state
//            setLoadingState(true);
//            
//            // Simulate API call (replace with actual backend call)
//            setTimeout(() => {
//                setLoadingState(false);
//                
//                // Demo validation (replace with actual authentication)
//                if (username === 'admin' && password === 'admin123') {
//                    showSuccess('Login successful! Redirecting...');
//                    // Redirect to admin dashboard
//                    setTimeout(() => {
//                        window.location.href = '/admin/dashboard';
//                    }, 1500);
//                } else {
//                    showError('Invalid username or password.');
//                }
//            }, 1500);
//        });

        // Helper functions
//        function setLoadingState(loading) {
//            if (loading) {
//                loginBtn.disabled = true;
//                spinner.style.display = 'inline-block';
//                loginText.textContent = 'Logging in...';
//            } else {
//                loginBtn.disabled = false;
//                spinner.style.display = 'none';
//                loginText.textContent = 'Login';
//            }
//        }
//
//        function showError(message) {
//            errorMessage.textContent = message;
//            errorMessage.style.display = 'block';
//            errorMessage.scrollIntoView({ behavior: 'smooth', block: 'center' });
//        }
//
//        function showSuccess(message) {
//            successMessage.textContent = message;
//            successMessage.style.display = 'block';
//        }
//
//        function hideMessages() {
//            errorMessage.style.display = 'none';
//            successMessage.style.display = 'none';
//        }
//
//        function forgotPassword() {
//            alert('Password reset functionality would be implemented here.');
//            // Replace with actual password reset logic
//        }
//
//        // Real-time validation
//        usernameInput.addEventListener('input', function() {
//            if (this.value.trim()) {
//                this.classList.remove('is-invalid');
//            }
//        });
//
//        passwordInput.addEventListener('input', function() {
//            if (this.value.trim()) {
//                this.classList.remove('is-invalid');
//            }
//        });
//
//        // Enter key support
//        document.addEventListener('keypress', function(e) {
//            if (e.key === 'Enter' && !loginBtn.disabled) {
//                loginForm.dispatchEvent(new Event('submit'));
//            }
//        });
//
//        // Focus first input on load
//        window.addEventListener('load', function() {
//            usernameInput.focus();
//        });
        
   async function adminLogin(){
      //console.log("OK");
      const popup = new Notification();
      const uName = document.getElementById("username").value;
      const pWord = document.getElementById("password").value;
      
      const login ={
          username:uName,
          password:pWord
      };
      
      const loginData = JSON.stringify(login);
      
      const response = await fetch("adminLogin",{
          method:"POST",
          headers:{
              "Content-Type":"application/json"
          },
          body:loginData
      });
      
      if(response.ok){
          const json = await response.json();
          if(json.status){
               popup.success({
                message:json.message
            });
              window.location="admin-verification.html";
          }else{
              popup.error({
                message:json.message
            });
          }
      }else{
          popup.error({
                message:"Error"
            });
      }
   }


