const express = require('express');
const { getClient } = require('../db');

const router = express.Router();

// Create a new record
router.post('/', async (req, res) => {
    const client = await getClient();
    const { name, description } = req.body;

    try {
        const query = `INSERT INTO suppliers (name, description) VALUES ($1, $2) RETURNING *`;
        const result = await client.query(query, [name, description]);
        const suppliers = await getAllSuppliers(client);
        res.json(suppliers);
    } catch (err) {
        console.error('Error in / POST route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Get all records
router.get('/', async (req, res) => {
    const client = await getClient();
    const query = `SELECT * FROM suppliers`;

    try {
        const result = await client.query(query);
        res.json(result.rows);
    } catch (err) {
        console.error('Error in / GET route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Update a record
router.put('/:id', async (req, res) => {
    const client = await getClient();
    const { name, description } = req.body;
    const { id } = req.params;

    try {
        const query = `UPDATE suppliers SET name = $1, description = $2 WHERE id = $3 RETURNING *`;
        const result = await client.query(query, [name, description, id]);
        const suppliers = await getAllSuppliers(client);
        res.json(suppliers);
    } catch (err) {
        console.error('Error in / PUT route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const client = await getClient();
    const { id } = req.params;

    try {
        const query = `DELETE FROM suppliers WHERE id = $1 RETURNING *`;
        await client.query(query, [id]);
        const suppliers = await getAllSuppliers(client);
        res.json(suppliers);
    } catch (err) {
        console.error('Error in / DELETE route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Helper function to get all suppliers
async function getAllSuppliers(client) {
    try {
        const query = `SELECT * FROM suppliers`;
        const result = await client.query(query);
        return result.rows;
    } catch (err) {
        throw new Error('Error fetching all suppliers from database: ' + err.message);
    }
}

module.exports = router;
