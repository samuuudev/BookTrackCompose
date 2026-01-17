#!/bin/bash

# Script para compilar y ejecutar BookTrackCompose
# Use este script para verificar que todo está compilando correctamente

echo "🔨 Compilando BookTrackCompose..."
./gradlew clean build

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa!"
    echo ""
    echo "📱 Opciones para ejecutar:"
    echo "1. Dispositivo físico: ./gradlew installDebug"
    echo "2. Emulador: Asegúrate de que esté ejecutándose y ejecuta ./gradlew installDebug"
    echo ""
    echo "💡 Nota: La app mostrará datos de demostración si no hay conexión a Supabase"
else
    echo "❌ Error de compilación"
    echo "Revisa los logs anteriores para más detalles"
    exit 1
fi

