SET FOREIGN_KEY_CHECKS = 0;

-- Tables principales d'achat
TRUNCATE TABLE `tabMaterial_Request_Item`;
TRUNCATE TABLE `tabMaterial_Request`;
TRUNCATE TABLE `tabRequest_for_Quotation_Supplier`;
TRUNCATE TABLE `tabRequest_for_Quotation_Item`;
TRUNCATE TABLE `tabRequest_for_Quotation`;
TRUNCATE TABLE `tabPurchase_Order_Item`;
TRUNCATE TABLE `tabPurchase_Order`;
TRUNCATE TABLE `tabPurchase_Receipt_Item`;
TRUNCATE TABLE `tabPurchase_Receipt`;
TRUNCATE TABLE `tabPurchase_Invoice_Item`;
TRUNCATE TABLE `tabPurchase_Invoice`;

-- Tables de stock liées aux achats
TRUNCATE TABLE `tabStock_Ledger_Entry`;
TRUNCATE TABLE `tabBin`;

-- Tables comptables liées aux achats
TRUNCATE TABLE `tabGL_Entry`;
TRUNCATE TABLE `tabPayment_Entry`;
TRUNCATE TABLE `tabPayment_Schedule`;
TRUNCATE TABLE `tabPayment_Ledger_Entry`;

SET FOREIGN_KEY_CHECKS = 1;

-- Réinitialiser TOUTES les séquences liées
UPDATE `tabSeries` SET current = 0 WHERE name IN (
    '',                   -- Séquence générale
    'ACC-GLE-2025-',     -- GL Entry
    'ACC-PAY-2025-',     -- Payment Entry
    'ACC-PDA-',          -- Payment Advance
    'ACC-PINV-2025-',    -- Purchase Invoice
    'ACC-PRQ-2025-',     -- Payment Request
    'MAT-DN-2025-',      -- Delivery Note
    'MAT-MR-2025-',      -- Material Request
    'MAT-PRE-2025-',     -- Purchase Receipt
    'MAT-SLE-2025-',     -- Stock Ledger Entry
    'MAT-UOM-CNV-',      -- UOM Conversion
    'PUR-ORD-2025-',     -- Purchase Order
    'PUR-RFQ-2025-',     -- Request for Quotation
    'PUR-SQTN-2025-'     -- Supplier Quotation
);