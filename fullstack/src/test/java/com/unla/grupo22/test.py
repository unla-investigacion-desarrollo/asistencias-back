import mysql.connector
from datetime import datetime

# Configuración de conexión a la base de datos
db_config = {
    'host': 'localhost',
    "port": 3309,
    'user': 'root',
    'password': 'root12345678',
    'database': 'eventos'
}

# Parámetros del evento
unique_code_evento = '3cc36e16-ba65-4b25-86e3-08923b8164bd'

try:
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()

    # Buscar ID del evento
    cursor.execute("SELECT id FROM event WHERE unique_code = %s", (unique_code_evento,))
    result = cursor.fetchone()
    if not result:
        raise ValueError("Evento no encontrado")

    event_id = result[0]

    # Obtener días del evento
    cursor.execute("SELECT id FROM event_days WHERE event_id = %s", (event_id,))
    event_days = cursor.fetchall()

    if not event_days:
        raise ValueError("El evento no tiene días asociados")

    # Insertar 100 asistentes
    for i in range(100):
        nombre = f"Nombre{i}"
        apellido = f"Apellido{i}"
        email = f"julianstomczak+{i}@gmail.com"
        documento = f"100000{i:03d}"

        cursor.execute("""
    INSERT INTO assistance_response (
        document_number, email, name, last_name,
        is_present, is_assistance_certify_sent,
        welcome_mail_sent,
        created_at, updated_at, event_id
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                       """, (
    documento, email, nombre, apellido,
    True, False,
    False,
    datetime.now(), datetime.now(), event_id))


        asistencia_id = cursor.lastrowid

        # Marcar presente en todos los días
        for (event_day_id,) in event_days:
            cursor.execute("""
                INSERT INTO assistance_days (
                    assistance_response_id, event_days_id, is_present
                ) VALUES (%s, %s, %s)
            """, (asistencia_id, event_day_id, True))

    conn.commit()
    print("✔ Se insertaron 100 asistentes correctamente.")

except Exception as e:
    print("❌ Error al insertar asistentes:", e)
    conn.rollback()
finally:
    cursor.close()
    conn.close()
