document.addEventListener('alpine:init', () => {
    Alpine.data('odpApp', function () {
        return {
            loading: false,
            validationMessage: '',
            searchDTO: {
                client: null,
                distributionManagers: []
            },
            distributionManagers: [],
            results: [],
            tomSelect: null,

            init() {
                this.fetchDistributionManagers();

                const clientInput = document.getElementById('client');
                if (clientInput) {
                    clientInput.focus();
                }

            },

            // Tom Select initialization with plugins and event handling
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
                        autofocus: false, // Prevents auto-focus on dropdown
                        onInitialize: function () {
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

            // Load distribution managers from API
            async fetchDistributionManagers() {
                try {
                    this.loading = true;
                    const response = await fetch('/odp/api/distribution-managers?onlyMapped=true');

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
                            const clientInput = document.getElementById('client');
                            if (clientInput) {
                                clientInput.focus();
                            }
                        }, 100);
                    });

                } catch (error) {
                    console.error('Error fetching distribution managers:', error);
                    this.validationMessage = 'Failed to load distribution managers';
                } finally {
                    this.loading = false;
                }
            },

            // Refresh Tom Select options after data fetch
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

            // Perform search with given inputs
            async searchOdp() {
                if (!this.searchDTO.client && this.searchDTO.distributionManagers.length === 0) {
                    this.validationMessage = 'Please enter either a Client ID or one Distribution Manager';
                    return;
                }

                this.validationMessage = '';
                this.loading = true;

                try {
                    const response = await fetch('/odp/api/odp-result', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            client: this.searchDTO.client,
                            distributionManagers: this.searchDTO.distributionManagers
                        })
                    });

                    if (!response.ok) throw new Error('Network response was not ok');

                    this.results = await response.json();
                } catch (error) {
                    console.error('Error searching ODP:', error);
                    this.validationMessage = 'An error occurred during the search';
                } finally {
                    this.loading = false;
                }
            },
            clearForm() {
                // Reset search DTO while preserving the structure
                this.searchDTO = {
                    client: null,
                    distributionManagers: [] // Clear selected managers
                };

                // Clear results and validation message
                this.results = [];
                this.validationMessage = '';

                // Reset TomSelect if initialized (select all by default as per your init behavior)
                if (this.tomSelect && this.distributionManagers.length > 0) {
                    const allValues = this.distributionManagers.map(dm => dm.id.toString());
                    this.tomSelect.setValue(allValues);
                    this.searchDTO.distributionManagers = allValues;
                }

                // Focus on client field after clearing (matching your init behavior)
                this.$nextTick(() => {
                    const clientInput = document.getElementById('client');
                    if (clientInput) {
                        clientInput.focus();
                    }
                });
            }
        };
    });
});
