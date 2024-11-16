const express = require('express');
const { connectDatabase, getClient } = require('./db');
const cors = require('cors');
const session = require('express-session');
const { requiresAuth } = require('./middleware/auth');

const app = express();
const PORT = 3000;

app.use(cors({
    origin: 'http://localhost:4200',
    allowedHeaders: ['Content-Type', 'Authorization'],
    credentials: true
}));

// Middleware
app.use(express.json());

app.use(session({
    secret: 'your_secret_key',
    resave: false,
    saveUninitialized: true,
    cookie: { secure: false }
}));

app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Credentials', true);
    res.header('Access-Control-Allow-Origin', req.headers.origin);
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Origin, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version, X-Response-Time, X-PINGOTHER, X-CSRF-Token,Authorization');
    if (req.method === "OPTIONS") {
        return res.status(200).end();
    } else {
        next();
    }
});
app.use((err, req, res, next) => {
    console.error('Global error handler:', err);
    res.status(500).json({ error: 'Internal Server Error' });
});
process.on('unhandledRejection', (reason, promise) => {
    console.error('Unhandled Rejection at:', promise, 'reason:', reason);
});


connectDatabase()
    .then(() => {


        // Mount routes
        app.use('/api/sites', requiresAuth, require('./routes/sites'));
        app.use('/api/suppliers', requiresAuth, require('./routes/suppliers'));
        app.use('/api/credits', requiresAuth, require('./routes/credits'));
        app.use('/api/cash', requiresAuth, require('./routes/cash'));
        app.use('/api/accounts', requiresAuth, require('./routes/accounts'));
        app.use('/api/auth', require('./routes/auth'));

        // Start the server
        app.listen(PORT, () => {
            console.log(`Server is running on http://localhost:${PORT}`);
        });
    })
    .catch(err => {
        console.error('Error connecting to database:', err);
    });
