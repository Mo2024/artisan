const { Client } = require('pg');
require('dotenv').config();

let client;

function connectDatabase() {
    return new Promise((resolve, reject) => {
        if (client) {
            resolve(client);
        } else {
            client = new Client({
                host: process.env.DB_HOST,
                port: process.env.DB_PORT,
                user: process.env.DB_USER,
                password: process.env.DB_PASSWORD,
                database: process.env.DB_NAME
            });

            client.connect(err => {
                if (err) {
                    reject(err);
                } else {
                    console.log('Connected to PostgreSQL database');
                    resolve(client);
                }
            });
        }
    });
}

function getClient() {
    if (!client) {
        throw new Error('Database client has not been initialized. Please call connectDatabase first.');
    }
    return client;
}

module.exports = {
    connectDatabase,
    getClient
};
