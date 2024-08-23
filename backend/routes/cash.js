const express = require('express');
const { getClient } = require('../db');
const { format } = require('date-fns');

const router = express.Router();
const bypass_id = process.env.DEFAULT_USER_ID

// Create a new record
router.post('/', async (req, res) => {
    const { date, paid_by, payment_method, site_id, cost, description, account_id } = req.body;
    const user_id = req.session.userId || bypass_id; // Get the user_id from session
    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    const query = `
        INSERT INTO cash (date, paid_by, payment_method, site_id, cost, description, date_recorded, account_id, user_id) 
        VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9);
    `;
    const client = await getClient();

    try {
        await client.query(query, [date, paid_by, payment_method, site_id, cost, description, formattedDate, account_id, user_id]);
        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// Get all records
router.get('/', async (req, res) => {
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    try {
        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
});

// Update a record
router.put('/:id', async (req, res) => {
    const { date, paid_by, payment_method, site_id, cost, description, account_id } = req.body;
    const { id } = req.params;
    const user_id = req.session.userId || bypass_id; // Get the user_id from session
    const query = `
        UPDATE cash 
        SET 
            date = $1,
            paid_by = $2,
            payment_method = $3,
            site_id = $4,
            cost = $5,
            description = $6,
            date_edited = $7,
            account_id = $8
        WHERE 
            id = $9 AND user_id = $10;
    `;
    const client = await getClient();

    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    try {
        await client.query(query, [date, paid_by, payment_method, site_id, cost, description, formattedDate, account_id, id, user_id]);
        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const { id } = req.params;
    const user_id = req.session.userId || bypass_id; // Get the user_id from session
    const query = `DELETE FROM cash WHERE id = $1 AND user_id = $2;`;
    const client = await getClient();


    try {
        await client.query(query, [id, user_id]);
        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// Fetch all records with the user_id condition
async function getAllCash(user_id) {
    const query = `
        SELECT cash.*, sites.name AS site_name
        FROM cash
        INNER JOIN sites ON cash.site_id = sites.id
        WHERE cash.user_id = $1;
    `;
    const client = await getClient();

    try {
        const { rows } = await client.query(query, [user_id]);
        return rows;
    } catch (error) {
        throw new Error('Error fetching all cash records: ' + error.message);
    }
}

module.exports = router;
