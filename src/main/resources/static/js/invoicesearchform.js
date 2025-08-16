// invoicesearchform.js
document.addEventListener('alpine:init', () => {
    Alpine.data('invoiceApp', function () {
        return {
            loading: false,
            validationMessage: '',
            searchDTO: {
                distributionManagers: [],
                startGeneratedDate: '',
                endGeneratedDate: ''
            },
            distributionManagers: [],
            results: [],
            totalRevenue: 0,
            tomSelect: null,
            startDatePicker: null,
            endDatePicker: null,
            sortField: null,
            sortDirection: 'asc',
            searchQuery: '',

            // Reference external library functions
            parseDateString: CultDataReportLib.parseDateString,
            formatDate: CultDataReportLib.formatDate,
            formatEuropeanCurrency: CultDataReportLib.formatEuropeanCurrency,

            init() {
                this.setDefaultDates();
                this.fetchDistributionManagers();
            },

            setDefaultDates() {
                const today = new Date();
                const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
                const yesterday = new Date(today);
                yesterday.setDate(today.getDate() - 1);

                this.searchDTO.startGeneratedDate = this.formatDate(firstDay);
                this.searchDTO.endGeneratedDate = this.formatDate(yesterday);
            },

            initializeDatePickers() {
                const dateConfig = {
                    dateFormat: "d M Y",
                    allowInput: true,
                    clickOpens: true,
                    parseDate: (dateStr) => this.parseDateString(dateStr)
                };

                this.startDatePicker = flatpickr(this.$refs.startDate, {
                    ...dateConfig,
                    defaultDate: this.searchDTO.startGeneratedDate,
                    onChange: (selectedDates) => {
                        if (selectedDates.length > 0) {
                            this.searchDTO.startGeneratedDate = this.formatDate(selectedDates[0]);
                        }
                    }
                });

                this.endDatePicker = flatpickr(this.$refs.endDate, {
                    ...dateConfig,
                    defaultDate: this.searchDTO.endGeneratedDate,
                    onChange: (selectedDates) => {
                        if (selectedDates.length > 0) {
                            this.searchDTO.endGeneratedDate = this.formatDate(selectedDates[0]);
                        }
                    }
                });
            },

            initializeTomSelect() {
                const selectEl = document.getElementById('distributionManagers');
                if (!selectEl) return;

                this.tomSelect = new TomSelect(selectEl, {
                    plugins: {
                        dropdown_header: {
                            title: '<div class="select-all-header" id="toggle-all-btn">✔ Select All</div>'
                        },
                        checkbox_options: {
                            checkedClassNames: ['ts-checked'],
                            uncheckedClassNames: ['ts-unchecked'],
                        },
                        remove_button: {},
                        clear_button: {
                            title: 'Remove all selected options'
                        }
                    },
                    maxItems: null,
                    autofocus: false,
                    onInitialize: function () {
                        this.isSetup = true;
                    },
                    onDropdownOpen: () => {
                        const allValues = Object.keys(this.tomSelect.options);
                        const btn = document.getElementById('toggle-all-btn');

                        if (this.tomSelect.items.length === allValues.length) {
                            btn.innerHTML = '✖ Unselect All';
                        } else {
                            btn.innerHTML = '✔ Select All';
                        }

                        btn.onclick = () => {
                            if (this.tomSelect.items.length === allValues.length) {
                                this.tomSelect.clear();
                            } else {
                                this.tomSelect.setValue(allValues);
                            }
                            this.tomSelect.close();
                        };
                    },
                    onItemAdd: () => {
                        this.searchDTO.distributionManagers = [...this.tomSelect.items];
                    },
                    onItemRemove: () => {
                        this.searchDTO.distributionManagers = [...this.tomSelect.items];
                    }
                });

                this.$nextTick(() => {
                    setTimeout(() => {
                        const allValues = this.distributionManagers.map(dm => dm.id.toString());
                        this.tomSelect.setValue(allValues);
                        this.searchDTO.distributionManagers = allValues;
                    }, 100);
                });
            },

            get filteredResults() {
                if (!this.searchQuery) return this.results || [];

                const fuse = new Fuse(this.results, {
                    keys: [
                        'client.id',
                        'client.name',
                        'client.distributionManagerName',
                        'invoiceName',
                        'totalNet',
                        'amountInEur'
                    ],
                    threshold: 0.3,
                    includeMatches: true
                });

                return fuse.search(this.searchQuery).map(result => result.item);
            },

            sort(field) {
                this.sortField = field;
                this.sortDirection = this.sortField === field && this.sortDirection === 'asc'
                    ? 'desc'
                    : 'asc';
            },

            get sortedResults() {
                const resultsToSort = this.searchQuery
                    ? this.filteredResults
                    : this.results;

                if (!resultsToSort || !this.sortField) return resultsToSort || [];

                return _.orderBy(
                    resultsToSort,
                    [
                        this.sortField === 'invoice.client.id' ? 'client.id' :
                        this.sortField === 'invoice.client.name' ? 'client.name' :
                        this.sortField === 'invoice.client.distributionManagerName' ? 'client.distributionManagerName' :
                        this.sortField === 'invoice.invoiceName' ? 'invoiceName' :
                        this.sortField === 'invoice.totalNet' ? 'totalNet' :
                        this.sortField === 'invoice.amountInEur' ? 'amountInEur' :
                        'generatedDate'
                    ],
                    [this.sortDirection]
                );
            },

            clearForm() {
                this.searchDTO = {
                    distributionManagers: [],
                    startGeneratedDate: this.formatDate(new Date(new Date().getFullYear(), new Date().getMonth(), 1)),
                    endGeneratedDate: this.formatDate(new Date())
                };

                this.results = [];
                this.totalRevenue = 0;
                this.validationMessage = '';

                if (this.tomSelect) {
                    const allValues = this.distributionManagers.map(dm => dm.id.toString());
                    this.tomSelect.setValue(allValues);
                    this.searchDTO.distributionManagers = allValues;
                }

                if (this.startDatePicker) {
                    this.startDatePicker.setDate(this.searchDTO.startGeneratedDate);
                }
                if (this.endDatePicker) {
                    this.endDatePicker.setDate(this.searchDTO.endGeneratedDate);
                }
            },

            async fetchDistributionManagers() {
                try {
                    this.loading = true;
                    const response = await fetch('/invoice/api/distribution-managers?onlyMapped=true');
                    if (!response.ok) throw new Error('Network response was not ok');

                    const data = await response.json();
                    this.distributionManagers = data.map(dm => ({
                        id: dm.id,
                        name: `${dm.name} (${dm.id})`
                    }));

                    this.$nextTick(() => {
                        setTimeout(() => {
                            this.refreshTomSelect();
                            this.initializeTomSelect();

                            const searchButton = document.getElementById('searchButton');
                            if (searchButton) {
                                searchButton.focus();
                            }

                            this.initializeDatePickers();
                        }, 100);
                    });

                } catch (error) {
                    console.error('Error fetching distribution managers:', error);
                    this.validationMessage = 'Failed to load distribution managers';
                } finally {
                    this.loading = false;
                }
            },

            refreshTomSelect() {
                if (!this.tomSelect) return;

                this.tomSelect.clearOptions();
                this.distributionManagers.forEach(dm => {
                    this.tomSelect.addOption({
                        value: dm.id,
                        text: dm.name
                    });
                });
                this.tomSelect.refreshOptions();
            },

            async searchInvoices() {
                const startDate = this.parseDateString(this.searchDTO.startGeneratedDate);
                const endDate = this.parseDateString(this.searchDTO.endGeneratedDate);

                if (this.searchDTO.distributionManagers.length === 0) {
                    this.validationMessage = 'Please select at least one Distribution Manager';
                    return;
                }

                if (!startDate || !endDate) {
                    this.validationMessage = 'Please enter valid dates in either "DD MMM YYYY" or "DD.MM.YYYY" format';
                    return;
                }

                if (startDate > endDate) {
                    this.validationMessage = 'Start date cannot be after end date';
                    return;
                }

                this.validationMessage = '';
                this.loading = true;

                try {
                    const dmIds = this.searchDTO.distributionManagers.map(id => parseInt(id));
                    const response = await fetch('/invoice/api/invoice-result', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            distributionManagers: dmIds,
                            startGeneratedDate: this.formatDate(startDate),
                            endGeneratedDate: this.formatDate(endDate)
                        })
                    });

                    if (!response.ok) {
                        const errorData = await response.json();
                        throw new Error(errorData.message || 'Network response was not ok');
                    }

                    const data = await response.json();
                    this.results = data.invoices || [];
                    this.totalRevenue = data.totalRevenue || 0;
                } catch (error) {
                    console.error('Error searching invoices:', error);
                    this.validationMessage = error.message || 'An error occurred during the search';
                } finally {
                    this.loading = false;
                }
            },

            async exportToExcel() {
                const startDate = this.parseDateString(this.searchDTO.startGeneratedDate);
                const endDate = this.parseDateString(this.searchDTO.endGeneratedDate);

                if (this.searchDTO.distributionManagers.length === 0) {
                    this.validationMessage = 'Please select at least one Distribution Manager';
                    return;
                }

                if (!startDate || !endDate) {
                    this.validationMessage = 'Please enter valid dates in either "DD MMM YYYY" or "DD.MM.YYYY" format';
                    return;
                }

                if (startDate > endDate) {
                    this.validationMessage = 'Start date cannot be after end date';
                    return;
                }

                this.validationMessage = '';
                this.loading = true;

                try {
                    const response = await fetch('/invoice/api/export', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            distributionManagers: this.searchDTO.distributionManagers,
                            startGeneratedDate: this.formatDate(startDate),
                            endGeneratedDate: this.formatDate(endDate)
                        })
                    });

                    if (!response.ok) throw new Error('Network response was not ok');

                    const blob = await response.blob();
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = 'Monthly_invoice_list.xlsx';
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                } catch (error) {
                    console.error('Error exporting invoices:', error);
                    this.validationMessage = 'An error occurred during export';
                } finally {
                    this.loading = false;
                }
            }
        };
    });
});