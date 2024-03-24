const express = require('express');
const { connectDatabase } = require('../db');
const { format } = require('date-fns');

const router = express.Router();

// Create a new record
router.post('/', async (req, res) => {
    const db = await connectDatabase();
    const { name, balance } = req.body;
    const query = `INSERT INTO accounts (name, balance) VALUES (?, ?);`;
    db.run(query, [name, balance], async function (err) {
        if (err) {
            return res.status(500).json({ error: err.message });
        }

        try {
            const accounts = await getAllAccounts();
            res.json(accounts);

        } catch (error) {
            return res.status(500).json({ error: error.message });
        }
    });
});

// Get all records
router.get('/', async (req, res) => {
    const db = await connectDatabase();

    try {
        // Execute the SQL SELECT statement
        const query = `
            SELECT 
                a.id,
                a.name,
                a.balance - COALESCE(SUM(cash.cost), 0) + COALESCE(SUM(inflow.price), 0) AS balance
            FROM 
                Accounts a
            LEFT JOIN 
                cash ON a.id = cash.account_id
            LEFT JOIN 
                inflow ON a.id = inflow.account_id
            GROUP BY 
                a.id, a.name, a.balance;
        `;


        db.all(query, [], (err, rows) => {
            if (err) {
                return res.status(500).json({ error: err.message });
                console.log(err)
            }
            res.json(rows);
        });
        
    } catch (error) {
        // Handle errors
        return res.status(500).json({ error: error.message });
    }
});


async function getAllAccounts() {
    try {
        const db = await connectDatabase();
        const query = `
        SELECT 
            a.id,
            a.name,
            a.balance - COALESCE(SUM(cash.cost), 0) + COALESCE(SUM(inflow.price), 0) AS balance
        FROM 
            Accounts a
        LEFT JOIN 
            cash ON a.id = cash.account_id
        LEFT JOIN 
            inflow ON a.id = inflow.account_id
        GROUP BY 
            a.id, a.name, a.balance;
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
