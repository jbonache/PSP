export const config = {
    key: process.env.JWT_SECRET,
    user: process.env.DEMO_USER,
    pass: process.env.DEMO_PASSWORD
};

if (!config.key || !config.user || !config.pass) {
    throw new Error('Cal definir JWT_SECRET, DEMO_USER i DEMO_PASSWORD');
}
