const express = require('express');
const { connectDatabase } = require('./db');
const cors = require('cors');
const session = require('express-session');

const app = express();
const PORT = 3000;
app.use(cors());


connectDatabase()
    .then(() => {
        // Middleware
        app.use(express.json());

        app.use(session({
            secret: 'your_secret_key', // Replace with your secret key
            resave: false,
            saveUninitialized: true,
            cookie: { secure: false } // Set secure to true if using https
        }));


        // Mount routes
        app.use('/api/sites', require('./routes/sites'));
        app.use('/api/suppliers', require('./routes/suppliers'));
        app.use('/api/credits', require('./routes/credits'));
        app.use('/api/cash', require('./routes/cash'));
        app.use('/api/accounts', require('./routes/accounts'));
        app.use('/api/auth', require('./routes/auth'));

        // Start the server
        app.listen(PORT, () => {
            console.log(`Server is running on http://localhost:${PORT}`);
        });
    })
    .catch(err => {
        console.error('Error connecting to database:', err);
    });
