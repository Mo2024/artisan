const express = require('express');
const { getClient } = require('../db'); // Ensure you use getClient for PostgreSQL
const { format } = require('date-fns');

const router = express.Router();
const bypass_id = process.env.DEFAULT_USER_ID

// Create a new record
router.post('/', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    const { date, invoice_no, supplier_id, site_id, cost, description } = req.body;
    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    const query = `
        INSERT INTO credits (date, invoice_no, supplier_id, site_id, cost, description, date_recorded, user_id)
        VALUES ($1, $2, $3, $4, $5, $6, $7, $8) RETURNING *;
    `;

    try {
        await client.query(query, [date, invoice_no, supplier_id, site_id, cost, description, formattedDate, user_id]);
        const credits = await getAllCredits(client, user_id);
        res.json(credits);
    } catch (err) {
        console.error('Error in / POST route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Get all records
router.get('/', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    const query = `
        SELECT credits.*, sites.name AS site_name, suppliers.name AS supplier_name
        FROM credits
        INNER JOIN sites ON credits.site_id = sites.id
        INNER JOIN suppliers ON credits.supplier_id = suppliers.id
        WHERE credits.user_id = $1;
    `;

    try {
        const result = await client.query(query, [user_id]);
        res.json(result.rows);
    } catch (err) {
        console.error('Error in / GET route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Update a record
router.put('/:id', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    const { date, invoice_no, supplier_id, site_id, cost, description } = req.body;
    const { id } = req.params;
    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    const query = `
        UPDATE credits
        SET 
            date = $1,
            invoice_no = $2,
            supplier_id = $3,
            site_id = $4,
            cost = $5,
            description = $6,
            date_edited = $7
        WHERE 
            id = $8 AND user_id = $9;
    `;

    try {
        await client.query(query, [date, invoice_no, supplier_id, site_id, cost, description, formattedDate, id, user_id]);
        const credits = await getAllCredits(client, user_id);
        res.json(credits);
    } catch (err) {
        console.error('Error in / PUT route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    const { id } = req.params;
    const query = `DELETE FROM credits WHERE id = $1 AND user_id = $2;`;

    try {
        await client.query(query, [id, user_id]);
        const credits = await getAllCredits(client, user_id);
        res.json(credits);
    } catch (err) {
        console.error('Error in / DELETE route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Helper function to get all credits
async function getAllCredits(client, user_id) {
    const query = `
        SELECT credits.*, sites.name AS site_name, suppliers.name AS supplier_name
        FROM credits
        INNER JOIN sites ON credits.site_id = sites.id
        INNER JOIN suppliers ON credits.supplier_id = suppliers.id
        WHERE credits.user_id = $1;
    `;

    try {
        const result = await client.query(query, [user_id]);
        return result.rows;
    } catch (err) {
        throw new Error('Error fetching all credits from database: ' + err.message);
    }
}

module.exports = router;
