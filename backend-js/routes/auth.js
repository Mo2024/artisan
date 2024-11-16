const express = require('express');
const { getClient } = require('../db');
const { format } = require('date-fns');

const router = express.Router();
const client = getClient();
const bypass_id = process.env.DEFAULT_USER_ID

router.get('/isAuth', async (req, res, next) => {
    try {
        // if (req.session.userId) {
        const userId = req.session.userId;
        return res.status(200).json({ userId });
        // }
        return res.status(400).json({ message: "Not logged in" });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }

});


router.post('/register', async (req, res) => {
    const { username } = req.body;
    try {
        // Check if the username exists
        const user = await checkUsername(client, username);
        if (user) {
            return res.status(400).json({ error: 'Username already exists' });
        }

        // Insert the new user
        const userId = await insertUser(client, username);

        // Create an Express session with the user's ID
        req.session.userId = userId;

        // Send a success response
        res.status(201).json({ message: 'User registered successfully', userId: userId });
    } catch (error) {
        console.log(error)
        res.status(500).json({ error: error.message });
    }

});

router.post('/login', async (req, res) => {
    const { username } = req.body;
    try {
        // Check if the username exists
        const user = await checkUsername(client, username);
        if (user) {
            req.session.userId = user.id;
            res.status(201).json({ message: 'User logged in successfully', userId: user.id });
        } else {
            return res.status(400).json({ error: 'Username does not exists' });
        }

    } catch (error) {
        res.status(500).json({ error: error.message });
    }

});

router.post('/logout', (req, res) => {
    try {
        // Destroy the session
        req.session.destroy((err) => {
            if (err) {
                return res.status(500).json({ error: 'Failed to log out' });
            }
            // Send a success response
            res.status(200).json({ message: 'Logged out successfully' });
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});


async function checkUsername(client, username) {
    console.log(client)
    console.log(username)
    const query = 'SELECT * FROM users WHERE username = $1';
    try {
        const res = await client.query(query, [username]);
        return res.rows[0]; // Return the first row (or undefined if no rows)
    } catch (err) {
        throw err;
    }
}

async function insertUser(client, username) {
    const query = 'INSERT INTO users (username) VALUES ($1) RETURNING id';
    try {
        const res = await client.query(query, [username]);
        return res.rows[0].id; // Return the ID of the inserted user
    } catch (err) {
        throw err;
    }
}

module.exports = router;
