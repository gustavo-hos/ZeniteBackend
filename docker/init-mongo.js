db = db.getSiblingDB('zenite');
db.createUser({
    user: process.env.MONGO_APP_USER || 'zenite',
    pwd:  process.env.MONGO_APP_PASS || 'zenite',
    roles: [{ role: 'readWrite', db: 'zenite' }]
});