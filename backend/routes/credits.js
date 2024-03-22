const express = require('express');
const { connectDatabase } = require('../db');

const router = express.Router();

// Create a new record
router.post('/', async (req, res) => {
    const db = await connectDatabase();
    const { date, invoice_no, supplier_id, site_id, cost, description } = req.body;
    console.log(supplier_id)
    const query = `INSERT INTO credits (date, invoice_no, supplier_id, site_id, cost, description) VALUES (?, ?, ?, ?, ?, ?);`;
    db.run(query, [date, invoice_no, supplier_id, site_id, cost, description], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }

        try {
            const credits = await getAllCredits();
            res.json(credits);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Get all records
router.get('/', async (req, res) => {
    const db = await connectDatabase();
    const query = `
    SELECT credits.*, sites.name AS site_name, suppliers.name AS supplier_name
    FROM credits
    INNER JOIN sites ON credits.site_id = sites.id
    INNER JOIN suppliers ON credits.supplier_id = suppliers.id;
`;

    db.all(query, [], async (err, rows) => {
        if (err) {
            return res.status(500).json({ error: err.message });
            console.log(err)
        }
        try {
            const credits = await getAllCredits();
            res.json(credits);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Update a record
router.put('/:id', async (req, res) => {
    const db = await connectDatabase();
    const { date, invoice_no, supplier_id, cost, description, site_id } = req.body;
    const { id } = req.params;
    const query = `
    UPDATE credits 
    SET 
        date = ?,
        invoice_no = ?,
        supplier_id = ?,
        site_id = ?,
        cost = ?,
        description = ?
    WHERE 
        id = ?;`;

    db.run(query, [date, invoice_no, supplier_id, site_id, cost, description, id], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        try {
            const credits = await getAllCredits();
            res.json(credits);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const db = await connectDatabase();
    const { id } = req.params;
    const query = `DELETE FROM credits WHERE id = ?`;
    db.run(query, id, async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        try {
            const credits = await getAllCredits();
            res.json(credits);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});


async function getAllCredits() {
    try {
        const db = await connectDatabase();
        const query = `
        SELECT credits.*, sites.name AS site_name, suppliers.name AS supplier_name
        FROM credits
        INNER JOIN sites ON credits.site_id = sites.id
        INNER JOIN suppliers ON credits.supplier_id = suppliers.id;
    `;

        return new Promise((resolve, reject) => {
            db.all(query, [], (err, rows) => {
                if (err) {
                    reject(err);
                    return;
                }
                resolve(rows);
            });
        });
    } catch (error) {
        throw new Error('Error fetching all sites from database: ' + error.message);
    }
}

module.exports = router;
