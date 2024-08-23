const express = require('express');
const { getClient } = require('../db');

const router = express.Router();
const bypass_id = process.env.DEFAULT_USER_ID
// Create a new record
router.post('/', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session
    const { name, description } = req.body;

    try {
        // Check if the site name already exists for the user
        const checkQuery = `SELECT COUNT(*) FROM sites WHERE name = $1 AND user_id = $2`;
        const checkResult = await client.query(checkQuery, [name, user_id]);

        if (checkResult.rows[0].count > 0) {
            return res.status(400).json({ error: 'Site name already exists for this user' });
        }

        const query = `INSERT INTO sites (name, description, user_id) VALUES ($1, $2, $3) RETURNING *`;
        const result = await client.query(query, [name, description, user_id]);
        const sites = await getAllSites(client, user_id);
        res.json(sites);
    } catch (err) {
        console.error('Error in / POST route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Get all records
router.get('/', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id;

    try {
        const query = `SELECT * FROM sites WHERE user_id = $1`;
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
    const user_id = req.session.userId || bypass_id;
    const { name, description } = req.body;
    const { id } = req.params;

    try {
        const query = `UPDATE sites SET name = $1, description = $2 WHERE id = $3 AND user_id = $4 RETURNING *`;
        const result = await client.query(query, [name, description, id, user_id]);
        const sites = await getAllSites(client, user_id);
        res.json(sites);
    } catch (err) {
        console.error('Error in / PUT route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id;
    const { id } = req.params;

    try {
        const query = `DELETE FROM sites WHERE id = $1 AND user_id = $2 RETURNING *`;
        await client.query(query, [id, user_id]);
        const sites = await getAllSites(client, user_id);
        res.json(sites);
    } catch (err) {
        console.error('Error in / DELETE route:', err);
        return res.status(500).json({ error: err.message });
    }
});

async function getAllSites(client, user_id) {
    try {
        const query = `SELECT * FROM sites WHERE user_id = $1`;
        const result = await client.query(query, [user_id]);
        return result.rows;
    } catch (error) {
        throw new Error('Error fetching all sites from database: ' + error.message);
    }
}

module.exports = router;
