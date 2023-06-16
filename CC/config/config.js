module.exports = {
    multipleStatements  : true,
    host                : process.env.MYSQLHOST,
    user                : 'root',
    password            : process.env.MYSQLPASSWORD,
    database            : process.env.MYSQLDATABASE,
    port                : process.env.MYSQLPORT
};