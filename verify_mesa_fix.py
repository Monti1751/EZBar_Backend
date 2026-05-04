import requests
import json

BASE_URL = "http://localhost:8080/api/mesas"

def test_create_table():
    payload = {
        "numero_mesa": 999,
        "capacidad": 4,
        "ubicacion": "Terraza",
        "nombre": "Mesa de Prueba Antigravity",
        "estado": "libre"
    }
    
    try:
        print(f"Enviando POST a {BASE_URL}...")
        response = requests.post(BASE_URL, json=payload)
        print(f"Status Code: {response.statusCode}")
        print(f"Response: {response.text}")
        
        if response.status_code in [200, 201]:
            data = response.json()
            if data.get("nombre") == payload["nombre"]:
                print("✅ ÉXITO: El nombre se guardó correctamente.")
            else:
                print(f"❌ ERROR: El nombre devuelto ('{data.get('nombre')}') no coincide.")
        else:
            print("❌ ERROR: No se pudo crear la mesa.")
            
    except Exception as e:
        print(f"❌ ERROR al conectar con el backend: {e}")

if __name__ == "__main__":
    test_create_table()
