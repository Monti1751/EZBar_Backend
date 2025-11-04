import 'package:flutter/material.dart';

class AjustesScreen extends StatelessWidget {
  const AjustesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Ajustes'),
        backgroundColor: const Color(0xFF7BA238),
      ),
      body: ListView(
        children: const [
          ListTile(
            leading: Icon(Icons.person, color: Color(0xFF4A4025)),
            title: Text('Cuenta'),
            subtitle: Text('Configura tu información personal'),
          ),
          ListTile(
            leading: Icon(Icons.lock, color: Color(0xFF4A4025)),
            title: Text('Privacidad'),
            subtitle: Text('Cambia tu contraseña o verifica permisos'),
          ),
          ListTile(
            leading: Icon(Icons.notifications, color: Color(0xFF4A4025)),
            title: Text('Notificaciones'),
            subtitle: Text('Administra alertas y sonidos'),
          ),
        ],
      ),
    );
  }
}