const sqlite3 = require('sqlite3');

let db;

function connectDatabase() {
    return new Promise((resolve, reject) => {
        if (db) {
            resolve(db);
        } else {
            db = new sqlite3.Database('artisan.db', err => {
                if (err) {
                    reject(err);
                } else {
                    console.log('Connected to SQLite database');
                    resolve(db);
                }
            });
        }
    });
}

module.exports = {
    connectDatabase
};
