document.addEventListener('alpine:init', () => {
    Alpine.data('campaignMessageApp', function() {
        return {
            loading: false,
            searchDTO: {
                propertyId: null,
                campaignId: null,
                campaignName: '',
                sentFromDate: '',
                sentToDate: '',
                hasPermission: 1 // Default to "All" (1)
            },
            results: [],
            summary: {
                totalSent: 0,
                totalOpened: 0,
                totalPermissions: 0,
                openRate: 0.0,
                permissionRate: 0.0
            },
            fromDatePicker: null,
            toDatePicker: null,

            init() {
                this.setDefaultDates();
                this.$nextTick(() => {
                    setTimeout(() => {
                        this.initializeDatePickers();
                    }, 20);
                });
            },

            setDefaultDates() {
                const today = new Date();
                const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
                const yesterday = new Date(today);
                yesterday.setDate(today.getDate() - 1);

                this.searchDTO.sentFromDate = this.formatDate(firstDay);
                this.searchDTO.sentToDate = this.formatDate(yesterday);
            },

            initializeDatePickers() {
                this.fromDatePicker = flatpickr(this.$refs.fromDate, {
                    dateFormat: "d M Y",
                    defaultDate: this.searchDTO.sentFromDate,
                    onChange: (selectedDates) => {
                        if (selectedDates.length > 0) {
                            this.searchDTO.sentFromDate = this.formatDate(selectedDates[0]);
                            if (this.toDatePicker) {
                                this.toDatePicker.set('minDate', selectedDates[0]);
                            }
                        }
                    }
                });

                this.toDatePicker = flatpickr(this.$refs.toDate, {
                    dateFormat: "d M Y",
                    defaultDate: this.searchDTO.sentToDate,
                    minDate: this.searchDTO.sentFromDate,
                    onChange: (selectedDates) => {
                        if (selectedDates.length > 0) {
                            this.searchDTO.sentToDate = this.formatDate(selectedDates[0]);
                        }
                    }
                });
            },

            clearForm() {
                this.searchDTO = {
                    propertyId: null,
                    campaignId: null,
                    campaignName: '',
                    sentFromDate: this.formatDate(new Date(new Date().getFullYear(), new Date().getMonth(), 1)),
                    sentToDate: this.formatDate(new Date()),
                    hasPermission: 1 // Reset to "All"
                };
                this.results = [];
                this.summary = {
                    totalSent: 0,
                    totalOpened: 0,
                    totalPermissions: 0,
                    openRate: 0.0,
                    permissionRate: 0.0
                };
                this.fromDatePicker.setDate(this.searchDTO.sentFromDate);
                this.toDatePicker.setDate(this.searchDTO.sentToDate);
            },

            getFeedbackStatus(message) {
                if (!message.messageFeedbacks || message.messageFeedbacks.length === 0) {
                    return 'No feedback';
                }
                return message.messageFeedbacks[0].status || 'Feedback available';
            },

            async searchCampaignMessages() {
                if (!this.searchDTO.sentFromDate || !this.searchDTO.sentToDate) {
                    return;
                }

                this.loading = true;

                try {
                    const permission = Number(this.searchDTO.hasPermission);

                    const permissionValue = permission === 1 ? null :
                                            permission === 2 ? true :
                                            false;

                    const response = await fetch('/campaignmessage/api/campaignmessage-result', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            propertyId: this.searchDTO.propertyId ? parseInt(this.searchDTO.propertyId) : null,
                            campaignId: this.searchDTO.campaignId ? parseInt(this.searchDTO.campaignId) : null,
                            campaignName: this.searchDTO.campaignName,
                            sentFromDate: this.searchDTO.sentFromDate,
                            sentToDate: this.searchDTO.sentToDate,
                            hasPermission: permissionValue
                        })
                    });

                    if (!response.ok) throw new Error('Network response was not ok');

                    const data = await response.json();
                    this.results = data.messages || [];

                    // Update summary from response
                    if (data.summary) {
                        this.summary = {
                            totalSent: data.summary.totalSent || 0,
                            totalOpened: data.summary.totalOpened || 0,
                            totalPermissions: data.summary.totalPermissions || 0,
                            openRate: data.summary.openRate || 0.0,
                            permissionRate: data.summary.permissionRate || 0.0
                        };
                    } else {
                        // Fallback calculation if summary not provided
                        const totalSent = this.results.length;
                        const totalOpened = this.results.filter(m => m.status === 'OPENED').length;
                        const totalPermissions = this.results.filter(m => m.permission).length;

                        this.summary = {
                            totalSent: totalSent,
                            totalOpened: totalOpened,
                            totalPermissions: totalPermissions,
                            openRate: totalSent > 0 ? (totalOpened / totalSent * 100) : 0,
                            permissionRate: totalSent > 0 ? (totalPermissions / totalSent * 100) : 0
                        };
                    }
                } catch (error) {
                    console.error('Error searching campaign messages:', error);
                    this.results = [];
                    this.summary = {
                        totalSent: 0,
                        totalOpened: 0,
                        totalPermissions: 0,
                        openRate: 0.0,
                        permissionRate: 0.0
                    };
                } finally {
                    this.loading = false;
                }
            },
            // Formats Date object or date string → "DD MMM YYYY" (e.g., "2023-01-01" or new Date() → "01 Jan 2023")
            // Handle both Date objects and date strings
            formatDate(input) {

                const date = input instanceof Date ? input : new Date(input);
                if (!date || isNaN(date.getTime())) return '';

                const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                   "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                const day = String(date.getDate()).padStart(2, '0');
                const month = monthNames[date.getMonth()];
                const year = date.getFullYear();
                return `${day} ${month} ${year}`;
            }
        };
    });
});