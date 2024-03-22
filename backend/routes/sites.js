const express = require('express');
const { connectDatabase } = require('../db');

const router = express.Router();

// Create a new record
router.post('/', async (req, res) => {
    const db = await connectDatabase();
    const { name, description } = req.body;
    const query = `INSERT INTO sites (name, description) VALUES (?, ?)`;
    db.run(query, [name, description], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }

        try {
            const sites = await getAllSites();
            res.json(sites);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Get all records
router.get('/', async (req, res) => {
    const db = await connectDatabase();
    const query = `SELECT * FROM sites`;
    db.all(query, [], (err, rows) => {
        if (err) {
            return res.status(500).json({ error: err.message });
            console.log(err)
        }
        res.json(rows);
    });
});

// Update a record
router.put('/:id', async (req, res) => {
    const db = await connectDatabase();
    const { name, description } = req.body;
    const { id } = req.params;
    const query = `UPDATE sites SET name = ?, description = ? WHERE id = ?`;
    db.run(query, [name, description, id], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        try {
            const sites = await getAllSites();
            res.json(sites);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Delete a record
router.delete('/:id', async (req, res) => {
    const db = await connectDatabase();
    const { id } = req.params;
    const query = `DELETE FROM sites WHERE id = ?`;
    db.run(query, id, async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        try {
            const sites = await getAllSites();
            res.json(sites);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});


async function getAllSites() {
    try {
        const db = await connectDatabase();
        const allSitesQuery = `SELECT * FROM sites`;
        return new Promise((resolve, reject) => {
            db.all(allSitesQuery, [], (err, rows) => {
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
