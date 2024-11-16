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
    const cashQuery = `
        INSERT INTO cash (date, paid_by, payment_method, site_id, cost, description, date_recorded, account_id, user_id) 
        VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9);
    `;
    const accountUpdateQuery = `
        UPDATE accounts 
        SET balance = balance - $1 
        WHERE id = $2 AND user_id = $3;
    `;
    const client = await getClient();

    try {
        await client.query('BEGIN');
        await client.query(cashQuery, [date, paid_by, payment_method, site_id, cost, description, formattedDate, account_id, user_id]);
        await client.query(accountUpdateQuery, [cost, account_id, user_id]);
        await client.query('COMMIT');

        // Fetch updated cash records
        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (err) {
        // Rollback the transaction in case of error
        await client.query('ROLLBACK');
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
    const { date: newDate, paid_by: newPaid_by, payment_method: newPayment_method, site_id: newSite_id, cost: newCost, description: newDescription, account_id: newAccount_id } = req.body;
    const { id } = req.params;
    const user_id = req.session.userId || bypass_id; // Get the user_id from session
    const updatePurchaseQuery = `
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
    const getCostQuery = `SELECT cost, account_id, payment_method FROM cash WHERE id = $1 AND user_id = $2`;
    const getAccountBalanceQuery = `SELECT balance FROM accounts WHERE id = $1 AND user_id = $2`;

    const client = await getClient();

    const currentDate = new Date();
    const formattedDate = format(currentDate, 'dd-MM-yyyy');
    try {
        await client.query('BEGIN');

        await client.query(updatePurchaseQuery, [newDate, newPaid_by, newPayment_method, newSite_id, newCost, newDescription, formattedDate, newAccount_id, id, user_id]);

        const result = await client.query(getCostQuery, [id, user_id]);
        const { cost: current_cost, account_id: current_account_id, payment_method: current_payment_method } = result.rows[0] || {};
        // console.log(result.rows[0])
        if (!current_cost) {
            throw new Error('Cash record not found');
        }

        if (newAccount_id) {
            // Fetch the current balance of the new account
            const balanceResult = await client.query(getAccountBalanceQuery, [newAccount_id, user_id]);
            const newAccountBalance = balanceResult.rows[0].balance;


            // console.log(payment_method === 'Cash' && newPayment_method === 'bank account')
            // console.log(payment_method)
            // console.log(newPayment_method)
            if (payment_method === 'Cash' && newPayment_method === 'bank account') {
                // Subtracting newCost from the account
                if (newAccountBalance - newCost < 0) {
                    throw new Error('Insufficient balance in the account for this transaction');
                }
                const updateCashToAccountQuery = `UPDATE accounts SET balance = balance - $1 WHERE id = $2 AND user_id = $3`;
                await client.query(updateCashToAccountQuery, [newCost, newAccount_id, user_id]);
            }
            else if (newPayment_method === 'Cash' && payment_method === 'bank account') {
                const updateAccountToCashQuery = `UPDATE accounts SET balance = balance + $1 WHERE id = $2 AND user_id = $3`;
                await client.query(updateAccountToCashQuery, [current_cost, current_account_id, user_id]);
            }
            else if (payment_method === 'bank account' && newPayment_method === 'bank account' && newAccount_id === current_account_id) {
                const balanceChange = newCost - current_cost;
                if (newAccountBalance - balanceChange < 0) {
                    throw new Error('Insufficient balance in the account for this transaction');
                }
                const updateSameAccountQuery = `UPDATE accounts SET balance = balance - $1 WHERE id = $2 AND user_id = $3`;
                await client.query(updateSameAccountQuery, [balanceChange, current_account_id, user_id]);
            }
            else if (payment_method === 'bank account' && newPayment_method === 'bank account' && newAccount_id !== current_account_id) {
                const oldAccountBalanceResult = await client.query(getAccountBalanceQuery, [current_account_id, user_id]);
                const oldAccountBalance = oldAccountBalanceResult.rows[0].balance;

                if (oldAccountBalance + current_cost < 0 || newAccountBalance - newCost < 0) {
                    throw new Error('Insufficient balance in the old or new account for this transaction');
                }

                const updateOldAccountQuery = `UPDATE accounts SET balance = balance + $1 WHERE id = $2 AND user_id = $3`;
                const updateNewAccountQuery = `UPDATE accounts SET balance = balance - $1 WHERE id = $2 AND user_id = $3`;
                await client.query(updateOldAccountQuery, [current_cost, current_account_id, user_id]);
                await client.query(updateNewAccountQuery, [newCost, newAccount_id, user_id]);
            }
        }

        await client.query('COMMIT');
        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (err) {
        await client.query('ROLLBACK');
        return res.status(500).json({ error: err.message });
    }
});


router.delete('/:id', async (req, res) => {
    const { id } = req.params;
    const user_id = req.session.userId || bypass_id; // Get the user_id from session
    const getCostQuery = `SELECT cost, account_id FROM cash WHERE id = $1 AND user_id = $2`;
    const deleteCashQuery = `DELETE FROM cash WHERE id = $1 AND user_id = $2`;
    const updateAccountQuery = `UPDATE accounts SET balance = balance + $1 WHERE id = $2 AND user_id = $3`;

    const client = await getClient();

    try {
        await client.query('BEGIN');
        const result = await client.query(getCostQuery, [id, user_id]);
        const { cost, account_id } = result.rows[0] || {};

        if (!cost) {
            throw new Error('Cash record not found');
        }
        await client.query(deleteCashQuery, [id, user_id]);
        if (account_id) {
            await client.query(updateAccountQuery, [cost, account_id, user_id]);
        }
        await client.query('COMMIT');

        const cash = await getAllCash(user_id);
        res.json(cash);
    } catch (err) {
        await client.query('ROLLBACK');
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
