// cultdatareportlib.js
window.CultDataReportLib = (function() {
    // Helper function to check if a date is valid
    function isValidDate(date) {
        return date instanceof Date && !isNaN(date.getTime());
    }

    // Parse date string (supports "DD MMM YYYY" and "DD.MM.YYYY")
    function parseDateString(dateStr) {
        if (!dateStr) return null;

        const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        const parts1 = dateStr.split(' ');
        if (parts1.length === 3 && months.includes(parts1[1])) {
            const day = parseInt(parts1[0]);
            const month = months.indexOf(parts1[1]);
            const year = parseInt(parts1[2]);
            if (!isNaN(day) && !isNaN(month) && !isNaN(year)) {
                return new Date(year, month, day);
            }
        }

        const parts2 = dateStr.split('.');
        if (parts2.length === 3) {
            const day = parseInt(parts2[0]);
            const month = parseInt(parts2[1]) - 1;
            const year = parseInt(parts2[2]);
            if (!isNaN(day) && !isNaN(month) && !isNaN(year)) {
                return new Date(year, month, day);
            }
        }

        return null;
    }

    // Format date as "DD MMM YYYY" (e.g., "01 Jan 2023")
    function formatDate(input) {
        const date = input instanceof Date ? input : new Date(input);
        if (!isValidDate(date)) return '';

        const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        const day = String(date.getDate()).padStart(2, '0');
        const month = monthNames[date.getMonth()];
        const year = date.getFullYear();
        return `${day} ${month} ${year}`;
    }

    // Format currency in European style (e.g., "1.234,56")
    function formatEuropeanCurrency(amount) {
        return amount?.toLocaleString('de-DE', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }) || '0,00';
    }

    return {
        parseDateString,
        formatDate,
        formatEuropeanCurrency
    };
})();