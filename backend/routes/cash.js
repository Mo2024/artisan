const express = require('express');
const { connectDatabase } = require('../db');
const { format } = require('date-fns');

const router = express.Router();

// Create a new record
router.post('/', async (req, res) => {
    const db = await connectDatabase();
    const { date, paid_by, payment_method, site_id, cost, description } = req.body;
    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    const query = `INSERT INTO cash (date, paid_by, payment_method, site_id, cost, description, date_recorded) VALUES (?, ?, ?, ?, ?, ?, ?);`;
    db.run(query, [date, paid_by, payment_method, site_id, cost, description, formattedDate], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }

        try {
            const cash = await getAllCash();
            res.json(cash);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Get all records
router.get('/', async (req, res) => {
    const db = await connectDatabase();

    try {
        const cash = await getAllCash();
        res.json(cash);

    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
});

// Update a record
router.put('/:id', async (req, res) => {
    const db = await connectDatabase();
    const { date, paid_by, payment_method, site_id, cost, description } = req.body;
    const { id } = req.params;
    const query = `
    UPDATE cash 
    SET 
        date = ?,
        paid_by = ?,
        payment_method = ?,
        site_id = ?,
        cost = ?,
        description = ?,
        date_edited = ?
    WHERE 
        id = ?;`;
    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    console.log(formattedDate)
    db.run(query, [date, paid_by, payment_method, site_id, cost, description, formattedDate, id], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        try {
            const cash = await getAllCash();
            res.json(cash);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const db = await connectDatabase();
    const { id } = req.params;
    const query = `DELETE FROM cash WHERE id = ?`;
    db.run(query, id, async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        try {
            const cash = await getAllCash();
            res.json(cash);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});


async function getAllCash() {
    try {
        const db = await connectDatabase();
        const query = `
        SELECT cash.*, sites.name AS site_name
        FROM cash
        INNER JOIN sites ON cash.site_id = sites.id;
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
