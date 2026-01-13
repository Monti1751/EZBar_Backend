import mysql from "mysql2/promise";

const pool = mysql.createPool({
  host: process.env.DB_HOST || "localhost",
  port: process.env.DB_PORT || 3306,
  user: process.env.DB_USER || "root",
  password: process.env.DB_PASSWORD || "m3f1",
  database: process.env.DB_NAME || "EZBarDB",
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// Verificar conexión al iniciar
pool.getConnection()
  .then(conn => {
    console.log("✅ Conexión exitosa a MariaDB");
    conn.release();
  })
  .catch(err => {
    console.error("❌ Error conectando a MariaDB:", err.message);
  });

export default pool;