document.addEventListener('alpine:init', () => {
    Alpine.data('conversionRateApp', function() {
        return {
            loading: false,
            searchDTO: {
                clientId: '',
                distributionManagers: [],
                fromDate: '',
                toDate: '',
                excludeTestProperties: true,
                channelId: '58078'
            },
            distributionManagers: [],
            results: [],
            totalConversion: {},
            tomSelect: null,
            fromDatePicker: null,
            toDatePicker: null,
            searchQuery: '',
            sortField: null,
            sortDirection: 'asc',


            init() {
                this.setDefaultDates();
                this.fetchDistributionManagers();

            },

            setDefaultDates() {
                const today = new Date();
                const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
                const yesterday = new Date(today);
                yesterday.setDate(today.getDate() - 1);

                this.searchDTO.fromDate = this.formatDate(firstDay);
                this.searchDTO.toDate = this.formatDate(yesterday);
            },

            initializeDatePickers() {
                this.fromDatePicker = flatpickr(this.$refs.fromDate, {
                    dateFormat: "d M Y",
                    defaultDate: this.searchDTO.fromDate,
                    onChange: (selectedDates) => {
                        if (selectedDates.length > 0) {
                            this.searchDTO.fromDate = this.formatDate(selectedDates[0]);
                            if (this.toDatePicker) {
                                this.toDatePicker.set('minDate', selectedDates[0]);
                            }
                        }
                    }
                });

                this.toDatePicker = flatpickr(this.$refs.toDate, {
                    dateFormat: "d M Y",
                    defaultDate: this.searchDTO.toDate,
                    minDate: this.searchDTO.fromDate,
                    onChange: (selectedDates) => {
                        if (selectedDates.length > 0) {
                            this.searchDTO.toDate = this.formatDate(selectedDates[0]);
                        }
                    }
                });
            },

            initializeTomSelect() {
                const selectEl = document.getElementById('distributionManagers');
                if (selectEl) {
                    this.tomSelect = new TomSelect(selectEl, {
                        plugins: {
                            dropdown_header: {
                                title: '<div class="select-all-header" id="select-all-btn">✔ Select All</div>'
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
                        onInitialize: function() {
                            this.isSetup = true;
                        },
                        onDropdownOpen: () => {
                            const allValues = Object.keys(this.tomSelect.options);
                            document.getElementById('select-all-btn').onclick = () => {
                                this.tomSelect.setValue(allValues);
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

                    // Select all items after initialization
                    this.$nextTick(() => {
                        setTimeout(() => {
                            const allValues = this.distributionManagers.map(dm => dm.id.toString());
                            this.tomSelect.setValue(allValues);
                            this.searchDTO.distributionManagers = allValues;
                        }, 100);
                    });
                }
            },

            get filteredResults() {
                if (!this.searchQuery) return this.results || [];

                const fuse = new Fuse(this.results, {
                    keys: [
                        'client.id'
                    ],
                    threshold: 0.4,
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
                        row => {
                            if (this.sortField === 'conversionRate') {
                                return parseFloat(row.conversionRate.replace('%', ''));
                            }
                         return this.sortField === 'client.id' ? row.client.id :
                                this.sortField === 'noOfAvailabilitySearch' ? row.noOfAvailabilitySearch :
                                this.sortField === 'responseTime' ? row.responseTime :
                                this.sortField === 'noOfUniqueVisitors' ? row.noOfUniqueVisitors :
                                this.sortField === 'numberOfBookings' ? row.numberOfBookings :
                                this.sortField === 'uniqueVisitorRate' ? row.uniqueVisitorRate :
                                row.conversionRate;
                        }
                    ],
                    [this.sortDirection]
                );
            },

            clearForm() {
                // Reset search DTO while preserving the structure
                this.searchDTO = {
                    clientId: '',
                    distributionManagers: [], // Clear selected managers
                    fromDate: this.formatDate(new Date(new Date().getFullYear(), new Date().getMonth(), 1)),
                    toDate: this.formatDate(new Date()),
                    excludeTestProperties: true,
                    channelId: '58078'
                };

                // Clear results and totals
                this.results = [];
                this.totalConversion = {};

                // Reset TomSelect if initialized (select all by default as per your init behavior)
                if (this.tomSelect) {
                    const allValues = this.distributionManagers.map(dm => dm.id.toString());
                    this.tomSelect.setValue(allValues);
                    this.searchDTO.distributionManagers = allValues;
                }

                // Reset date pickers if initialized
                if (this.fromDatePicker) {
                    this.fromDatePicker.setDate(this.searchDTO.fromDate);
                }
                if (this.toDatePicker) {
                    this.toDatePicker.setDate(this.searchDTO.toDate);
                }

                // Focus on client ID field after clearing
                this.$nextTick(() => {
                    const clientInput = document.getElementById('clientId');
                    if (clientInput) {
                        clientInput.focus();
                    }
                });
            },

           async fetchDistributionManagers() {
               try {
                   this.loading = true;

                   const response = await fetch('/conversionrate/api/distribution-managers?onlyMapped=true');
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
                           this.initializeDatePickers();

                           const clientInput = document.getElementById('clientId');
                           if (clientInput) {
                               clientInput.focus();
                           }
                       }, 100); // Delay helps prevent focus-stealing by TomSelect
                   });

               } catch (error) {
                   console.error('Error fetching distribution managers:', error);
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

            formatOneDecimal(value) {
                return value.toFixed(1);
            },

            async searchConversionRates() {
                if (!this.searchDTO.fromDate || !this.searchDTO.toDate) {
                    return;
                }

                this.loading = true;

                try {
                    const response = await fetch('/conversionrate/api/conversionrate-result', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            clientId: this.searchDTO.clientId,
                            distributionManagers: this.searchDTO.distributionManagers,
                            fromDate: this.searchDTO.fromDate,
                            toDate: this.searchDTO.toDate,
                            excludeTestProperties: this.searchDTO.excludeTestProperties,
                            channelId: this.searchDTO.channelId
                        })
                    });

                    if (!response.ok) throw new Error('Network response was not ok');

                    const data = await response.json();
                    this.results = data.conversionRates || [];
                    this.totalConversion = data.totalConversion || {};
                } catch (error) {
                    console.error('Error searching conversion rates:', error);
                } finally {
                    this.loading = false;
                }
            },

            exportToCSV() {
                if (!this.results?.length) return;

                this.downloadCSV(
                    this.prepareCSVData(),
                    this.generateFilename()
                );
            },

            prepareCSVData() {
            const rows = this.results.map(row => ({
                'Client ID': row.client.id,
                'Channel ID': row.channel.id,
                'Date': row.period,
                'Searches': row.noOfAvailabilitySearch,
                'Average Response Time (ms)': this.formatOneDecimal(row.responseTime),
                'Unique Visitors': row.noOfUniqueVisitors,
                'Bookings': row.numberOfBookings,
                'Searches/Visitors': row.uniqueVisitorRate,
                'Conversion Rate': row.conversionRate
            }));

            if (this.totalConversion) {
                rows.push({
                    'Client ID': 'TOTAL',
                    'Channel ID': `Clients: (${this.totalConversion.totalNoClientCount})`,
                    'Date': '',
                    ...Object.fromEntries(Object.entries({
                        'Searches': 'totalNoOfAvailabilitySearch',
                        'Average Response Time (ms)': 'averageResponseTime',
                        'Unique Visitors': 'totalNoOfUniqueVisitors',
                        'Bookings': 'totalBooking',
                        'Searches/Visitors': 'totalUniqueVisitorRate',
                        'Conversion Rate': 'totalConversionRate'
                    }).map(([key, val]) => [key, this.totalConversion[val]]))
                });
            }

            return rows;
            },

            generateFilename() {
            const params = [
                this.searchDTO.fromDate.replace(/ /g, '-'),
                this.searchDTO.toDate.replace(/ /g, '-'),
                this.searchDTO.clientId && `Client-${this.searchDTO.clientId}`,
                this.searchDTO.distributionManagers.length > 0 && `DMs-${this.searchDTO.distributionManagers.length}`
            ].filter(Boolean).join('_');

            return `Conversion-Rate_${params}.csv`;
            },

            downloadCSV(data, filename) {
            const csv = Papa.unparse(data, {
                quotes: true,
                header: true,
                delimiter: ",",
                newline: "\r\n"
            });

            const link = document.createElement('a');
            link.href = URL.createObjectURL(new Blob([csv], { type: 'text/csv;charset=utf-8;' }));
            link.download = filename;
            link.click();
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