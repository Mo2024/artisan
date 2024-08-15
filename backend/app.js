const express = require('express');
const { connectDatabase } = require('./db');
const cors = require('cors');
const session = require('express-session');
const { requiresAuth } = require('./middleware/auth');

const app = express();
const PORT = 3000;
// app.use(cors({ origin: true, credentials: true }));
app.use(cors({
    origin: 'http://localhost:4200',
    allowedHeaders: ['Content-Type', 'Authorization'],
    credentials: true
}));
// Middleware
app.use(express.json());

app.use(session({
    secret: 'your_secret_key', // Replace with your secret key
    resave: false,
    saveUninitialized: true,
    cookie: { secure: false } // Set secure to true if using https
}));


// app.use((req, res, next) => {
//     // The below 2 headers are for cookies
//     res.setHeader("Access-Control-Allow-Credentials", true);
//     res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
//     // res.setHeader("Access-Control-Allow-Headers",
//     //     "Origin, X-Requested-With, Content-Type, Accept");
//     // res.setHeader("Access-Control-Allow-Methods",
//     //     "GET, POST, PUT, PATCH, DELETE, OPTIONS");
//     next();
// });

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
