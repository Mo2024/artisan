const express = require('express');
const { getClient } = require('../db');

const router = express.Router();
const bypass_id = process.env.DEFAULT_USER_ID

router.post('/', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    const { name, balance } = req.body;

    // Check if the account name already exists for the user
    try {
        const exists = await nameExists(client, name, user_id);
        if (exists) {
            return res.status(400).json({ error: 'Account name already exists for this user.' });
        }

        // Proceed with insertion if the name does not exist
        const query = `INSERT INTO accounts (name, balance, user_id) VALUES ($1, $2, $3) RETURNING *;`;
        await client.query(query, [name, balance, user_id]);
        const accounts = await getAllAccounts(client, user_id);
        res.json(accounts);
    } catch (err) {
        console.error('Error in / POST route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Get all records
router.get('/', async (req, res) => {
    const client = await getClient();
    const user_id = req.session.userId || bypass_id; // Get the user_id from session

    try {
        // Execute the SQL SELECT statement with user_id condition

        const accounts = await getAllAccounts(client, user_id);
        console.log(accounts)
        res.json(accounts);

    } catch (err) {
        console.error('Error in / GET route:', err);
        return res.status(500).json({ error: err.message });
    }
});

// Helper function to get all accounts
async function getAllAccounts(client, user_id) {
    try {
        const query = `
            SELECT 
                a.id,
                a.name,
                a.balance AS balance
            FROM 
                accounts a
            WHERE 
                a.user_id = $1
            GROUP BY 
                a.id, a.name, a.balance;
        `;

        const result = await client.query(query, [user_id]);
        return result.rows;
    } catch (err) {
        throw new Error('Error fetching all accounts from database: ' + err.message);
    }
}

async function nameExists(client, name, user_id) {
    const query = `SELECT 1 FROM accounts WHERE name = $1 AND user_id = $2 LIMIT 1;`;
    const result = await client.query(query, [name, user_id]);
    return result.rowCount > 0; // Returns true if a record exists
}

module.exports = router;
