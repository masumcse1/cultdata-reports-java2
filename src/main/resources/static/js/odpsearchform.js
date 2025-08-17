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
            allDMsOption: { id: 'all', name: 'All Distribution Managers' },

            init() {
                this.fetchDistributionManagers();

                const clientInput = document.getElementById('client');
                if (clientInput) {
                    clientInput.focus();
                }

            },

            initializeTomSelect() {
            const selectEl = document.getElementById('distributionManagers');
            if (selectEl) {
                const component = this;
                this.tomSelect = new TomSelect(selectEl, {
                    maxItems: 1,
                    onInitialize: function() {
                        this.setValue('all');
                        component.searchDTO.distributionManagers = [];
                    },
                    onChange: (value) => {
                        if (value === 'all') {
                            this.searchDTO.distributionManagers = [];
                        } else {
                            this.searchDTO.distributionManagers = value ? [parseInt(value, 10)] : [];
                        }
                    }
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
                    this.distributionManagers = [
                        this.allDMsOption,
                        ...data.map(dm => ({
                            id: dm.id,
                            name: `${dm.name} (${dm.id})`
                        }))
                    ];

                    this.$nextTick(() => {
                        this.initializeTomSelect();
                        document.getElementById('client')?.focus();
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

            async searchOdp() {
                const selectedValue = this.tomSelect?.getValue();
                const isAllDMsSelected = selectedValue === 'all';

                const hasClient = !!this.searchDTO.client;
                const hasSpecificDM = !isAllDMsSelected && selectedValue;

                if (!hasClient && !isAllDMsSelected && !hasSpecificDM) {
                    this.validationMessage = 'Please enter either a Client ID or select a Distribution Manager';
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
                            distributionManagers: isAllDMsSelected
                                ? this.distributionManagers
                                    .filter(dm => dm.id !== 'all')
                                    .map(dm => dm.id)
                                : [parseInt(selectedValue, 10)]
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
