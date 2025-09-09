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

        // Simple Form Handling (front-end demo)
        document.getElementById('freelancerForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const title = document.getElementById('title');
            const skills = document.getElementById('skills');
            const country = document.getElementById('country');

            // Basic required validation
            [title, skills, country].forEach(el => {
                if (!el.value || !el.value.trim()) {
                    el.classList.add('is-invalid');
                } else {
                    el.classList.remove('is-invalid');
                }
            });
            if ([title, skills, country].some(el => el.classList.contains('is-invalid'))) {
                alert('Please complete required fields.');
                return;
            }

            // Collect data for demo
            const data = {
                title: title.value.trim(),
                skills: skills.value.trim(),
                available: document.getElementById('availability').checked,
                linkedin: document.getElementById('linkedin').value.trim(),
                portfolio: document.getElementById('portfolio').value.trim(),
                description: document.getElementById('description').value.trim(),
                country: country.value,
                education: Array.from(document.querySelectorAll('.edu-pair')).map(pair => ({
                    qualification: pair.querySelector('input')?.value?.trim() || '',
                    level: pair.querySelector('select')?.value || ''
                })),
                hasPdf: document.getElementById('educationPdf').files.length > 0
            };

            console.log('Submitting freelancer registration:', data);
            alert('Registration submitted! (Demo)');
            // Optionally reset: this.reset();
        });