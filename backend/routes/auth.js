const express = require('express');
const { connectDatabase } = require('../db');
const { format } = require('date-fns');

const router = express.Router();

// Create a new record
router.post('/register', async (req, res) => {
    const { username } = req.body;
    const db = await connectDatabase();
    try {
        // Check if the username exists
        const user = await checkUsername(db, username);
        if (user) {
            return res.status(400).json({ error: 'Username already exists' });
        }

        // Insert the new user
        const userId = await insertUser(db, username);

        // Create an Express session with the user's ID
        req.session.userId = userId;

        // Send a success response
        res.status(201).json({ message: 'User registered successfully', userId: userId });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }

});

router.post('/login', async (req, res) => {
    const { username } = req.body;
    const db = await connectDatabase();
    try {
        // Check if the username exists
        const user = await checkUsername(db, username);
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

async function checkUsername(db, username) {
    return new Promise(async (resolve, reject) => {
        const query = 'SELECT * FROM users WHERE username = ?';
        db.get(query, [username], (err, row) => {
            if (err) {
                return reject(err);
            }
            resolve(row);
        });
    });
}

async function insertUser(db, username) {
    return new Promise(async (resolve, reject) => {
        const query = 'INSERT INTO users (username) VALUES (?)';
        db.run(query, [username], function (err) {
            if (err) {
                return reject(err);
            }
            resolve(this.lastID); // Resolve with the ID of the inserted user
        });
    });
}

module.exports = router;
