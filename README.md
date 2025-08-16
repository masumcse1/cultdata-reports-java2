# Rules for CultData reports 

When defining **rules for reports** in your Extranet (especially if these reports are shared with hotels, distributors, or used for internal monitoring), it's important to standardize several **formatting, structural, and functional aspects** beyond just the date format.

Here’s a comprehensive checklist of elements you should consider:

## Date & Time Formatting

* Date format: e.g., `DD MMM YYYY` → `05 Jul 2025`
* Time format: 24-hour (`14:30`) 
* Datetime timezone: UTC (`GMT`)
* Week start day: Monday

## Number Formatting

* Decimal separator: comma (`1.000,50`)
* Thousand separator: period
* Currency format: 3 letter code ISO 4217 (`EUR`). Never use a currency symbol

## Language & Localization

* Default language for report labels and headers is English
* If translations are necessary they will be taken from Translation Tool

---

### 📊 **Content Rules**

* **Date range filter behavior:** e.g., show last 30 days by default, or allow custom selection
* **Default grouping:** by day, week, month, market segment, room, product, etc.
* **Data granularity:** e.g., daily totals vs. per-booking level
* **Handling of cancellations / modifications:**

  * Show as separate lines?
  * Strike-through with comment?
  * Net effect only?

---

### 📁 **File Format & Export Options**

* Available formats: PDF, XLSX, CSV
* Filename structure: e.g., `RoomDB_Report_2025-07-05.xlsx`
* Character encoding for CSV: UTF-8 recommended

---

### 📌 **Filtering and Sorting**

* Default filters: e.g., only confirmed bookings, only certain room types
* Sorting rules: e.g., by date, revenue, room
* Custom columns visibility: can users hide/show columns?

---

### 📐 **Units & Measurements**

* Currency: default and per-booking
* Lengths: meters vs. feet (if showing dimensions)
* Guest count format: `2 adults + 1 child` or total number (`3 guests`)

---

### 🔒 **Access & Permissions**

* Who can see what reports? (e.g., property owner vs. staff)
* Time-based access: can users see only past data or future forecasts?
* Data masking (e.g., anonymize guest names in downloadable reports)

---

### 🧮 **Calculation Rules**

* Gross vs. net pricing
* Inclusion/exclusion of taxes, commissions, and service charges
* Rounding rules (round up, down, nearest)

---

### ✅ **Visuals & Indicators**

* Icons or colors for statuses: cancelled, confirmed, pending
* Conditional formatting: e.g., red for negative revenue
* Legend or tooltips for abbreviations or metrics


