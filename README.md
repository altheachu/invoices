The following requirements is mimicking a coding test from a certain company. I did not pass the coding test and felt ashamed, so I asked AI to build similiar requirements for me to practice implementing.

The requirements are as follows in Chinese and English:

# 📌 題目：不同國家的發票格式輸出

🏢 背景：

你正在開發一套 SaaS 財會系統，服務對象涵蓋台灣、日本與美國的中小企業。
系統有一個「列印發票」的功能，每個國家的發票格式都略有不同：

🌐 各國發票格式需求如下：
## 🇹🇼 台灣發票

有統一編號、發票號碼、發票類別（收銀機／電子發票）
金額需顯示「含稅金額」與「營業稅 5%」

## 🇯🇵 日本發票（適格請求書）

顯示消費稅（10%）與課稅對象分類（課稅／免稅）
顯示供應商的「登録番号」

## 🇺🇸 美國發票
僅列出商品明細與總金額，稅由各州另計
發票下方要顯示適用的「State Tax Code」

# ✅ 任務說明：

1. 請設計一個InvoicePrinter，可根據輸入的國家別建立對應的發票格式處理物件，並產生對應的列印內容。
2. 若要新增「香港」地區，應用擴充class，不應修改現有邏輯。
3. 用TDD跟OOP實作
4. 時間限制：40-60分鐘

===

# 📌 Problem: Invoice Format Printing by Country

🏢 Background:
You are developing a SaaS accounting system targeting small and medium-sized businesses in Taiwan, Japan, and the United States. One of the system’s features is "Print Invoice", and the invoice format varies slightly by country:

🌐 Invoice format requirements for each country:

## 🇹🇼 Taiwan Invoice

Includes a Unified Business Number, invoice number, and invoice type (Cash Register / e-Invoice)

Must display the "Amount (Tax Included)" and the "Business Tax (5%)"

## 🇯🇵 Japan Invoice (Qualified Invoice)

Displays Consumption Tax (10%) and tax classification (Taxable / Exempt)

Must include the supplier’s Registered Number (登録番号)

## 🇺🇸 US Invoice

Lists only the product details and total amount; tax is calculated separately by state

Must display the applicable State Tax Code at the bottom

# ✅ Task Requirements:

1. Design an InvoicePrinter that can generate the appropriate invoice format handler based on the input country and produce the correct printed content.
2. The design should be open for extension: e.g., adding a new region such as "Hong Kong" should be done by adding new classes, without modifying existing logic.
3. Implement using TDD and OOP principles.
4. Time limit: 40 - 60 minutes
