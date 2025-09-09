// DOM elements
        const verificationForm = document.getElementById('verificationForm');
        const verificationCodeInput = document.getElementById('verificationCode');
        const verifyBtn = document.getElementById('verifyBtn');
        const verifyText = document.getElementById('verifyText');
        const spinner = document.getElementById('spinner');
        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');

        // Form validation and submission
//        verificationForm.addEventListener('submit', function(e) {
//            e.preventDefault();
//            
//            // Reset messages
//            hideMessages();
//            
//            // Get form value
//            const verificationCode = verificationCodeInput.value.trim();
//            
//            // Validate field
//            if (!verificationCode) {
//                showError('Please enter the verification code.');
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
//                // Demo validation (replace with actual verification)
//                if (verificationCode === 'ADMIN123' || verificationCode === 'admin123') {
//                    showSuccess('Verification successful! Redirecting to admin panel...');
//                    // Redirect to admin dashboard
//                    setTimeout(() => {
//                        window.location.href = '/admin/dashboard';
//                    }, 1500);
//                } else {
//                    showError('Invalid verification code. Please try again.');
//                    verificationCodeInput.focus();
//                }
//            }, 1500);
//        });

        // Helper functions
        function setLoadingState(loading) {
            if (loading) {
                verifyBtn.disabled = true;
                spinner.style.display = 'inline-block';
                verifyText.textContent = 'Verifying...';
            } else {
                verifyBtn.disabled = false;
                spinner.style.display = 'none';
                verifyText.textContent = 'Verify';
            }
        }

        function showError(message) {
            errorMessage.textContent = message;
            errorMessage.style.display = 'block';
            errorMessage.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }

        function showSuccess(message) {
            successMessage.textContent = message;
            successMessage.style.display = 'block';
        }

        function hideMessages() {
            errorMessage.style.display = 'none';
            successMessage.style.display = 'none';
        }

        function goBack() {
            // Replace with actual back navigation
            window.location.href = '/admin/login';
        }

        // Real-time validation
        verificationCodeInput.addEventListener('input', function() {
            if (this.value.trim()) {
                this.classList.remove('is-invalid');
            }
            
            // Auto-format code (optional)
            this.value = this.value.toUpperCase();
        });

        // Enter key support
        document.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && !verifyBtn.disabled) {
                verificationForm.dispatchEvent(new Event('submit'));
            }
        });

        // Focus input on load
        window.addEventListener('load', function() {
            verificationCodeInput.focus();
        });

        // Auto-select text on focus (optional)
        verificationCodeInput.addEventListener('focus', function() {
            this.select();
        });
        
        async function verifAdmin(){
            const popup = new Notification();
            let verification = document.getElementById("verificationCode").value;
            
            const response = await fetch("verifyAdmin?id="+verification);
            
            if(response.ok){
                const json = await response.json();
                if(json.status){
                   // console.log("now can redirect to dashboard");
                   window.location="admin-dashboard.html";
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


