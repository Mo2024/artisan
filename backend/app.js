const express = require('express');
const { connectDatabase } = require('./db');
const cors = require('cors');

const app = express();
const PORT = 3000;
app.use(cors());


connectDatabase()
    .then(() => {
        // Middleware
        app.use(express.json());

        // Mount routes
        app.use('/api/sites', require('./routes/sites'));
        app.use('/api/suppliers', require('./routes/suppliers'));
        app.use('/api/credits', require('./routes/credits'));

        // Start the server
        app.listen(PORT, () => {
            console.log(`Server is running on http://localhost:${PORT}`);
        });
    })
    .catch(err => {
        console.error('Error connecting to database:', err);
    });
