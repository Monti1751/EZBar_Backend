import 'package:flutter/material.dart'; 

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'EZBar',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.green,
        scaffoldBackgroundColor: Colors.green.shade50,
        inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: Colors.white,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: Colors.green),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: Colors.green, width: 2),
          ),
        ),
      ),
      home: const LoginPage(),
    );
  }
}

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  void _login() {
    if (_formKey.currentState!.validate()) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: const Text('Conectando...'),
          backgroundColor: Colors.green.shade700,
        ),
      );
    }
  }

 @override
Widget build(BuildContext context) {
  return Scaffold(
    body: Center(
      child: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
        child: Stack(
          alignment: Alignment.topCenter,
          children: [
            // Contenedor del formulario (lo desplazamos hacia abajo)
            Container(
              margin: const EdgeInsets.only(top: 90), // deja espacio para el logo
              padding: const EdgeInsets.fromLTRB(24, 80, 24, 24), // añade espacio arriba para el logo superpuesto
              decoration: BoxDecoration(
                color: const Color(0xFFD2FFBD), // Color del recuadro (#D2FFBD)
                borderRadius: BorderRadius.circular(20),
                boxShadow: [
                  BoxShadow(
                    color: Colors.green.withOpacity(0.2),
                    blurRadius: 10,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: Form(
                key: _formKey,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const SizedBox(height: 6),
                    Text(
                      "Bienvenido",
                      style: TextStyle(
                        fontSize: 28,
                        fontWeight: FontWeight.bold,
                        color: Colors.green.shade800,
                      ),
                    ),
                    const SizedBox(height: 30),
                    TextFormField(
                      controller: _usernameController,
                      decoration: const InputDecoration(
                        labelText: 'Nombre de usuario...',
                        prefixIcon: Icon(Icons.person_outlined, color: Colors.green),
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Por favor, ingresa tu usuario';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      controller: _passwordController,
                      obscureText: true,
                      decoration: const InputDecoration(
                        labelText: 'Contraseña...',
                        prefixIcon: Icon(Icons.lock_outline, color: Colors.green),
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Por favor, ingresa la contraseña';
                        }
                        if (value.length < 8) {
                          return 'Debe tener al menos 8 caracteres';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 24),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _login,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.green,
                          padding: const EdgeInsets.symmetric(vertical: 14),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                        ),
                        child: const Text(
                          'Iniciar sesión',
                          style: TextStyle(fontSize: 18, color: Colors.white),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),

            // Logo superpuesto
            Container(
              decoration: BoxDecoration(
                color: Colors.green,
                shape: BoxShape.circle,
                border: Border.all(
                  color: const Color.fromARGB(255, 76, 175, 80),
                  width: 6,
                ),
              ),
              padding: const EdgeInsets.all(8),
              child: ClipOval(
                child: Image.asset(
                  'logo.png',
                  height: 130,
                  width: 130,
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ],
        ),
      ),
    ),
  );
}
}
