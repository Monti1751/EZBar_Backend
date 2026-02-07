import bcrypt from 'bcryptjs';

const password = 'admin123';
const rounds = 10;

bcrypt.hash(password, rounds, (err, hash) => {
  if (err) {
    console.error('Error:', err);
  } else {
    console.log('Hash para "admin123":');
    console.log(hash);
  }
  process.exit(0);
});
