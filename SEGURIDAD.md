# Configuración Segura de Credenciales

## ⚠️ Importante
Los archivos wallet y las contraseñas han sido eliminados del repositorio por razones de seguridad.

## Configuración de Variables de Entorno

Las aplicaciones utilizan variables de entorno para las credenciales. Configúralas de la siguiente manera:

### Linux / macOS
```bash
export DB_URL="jdbc:oracle:thin:@rpwbb3wdet77677f_high"
export DB_USERNAME="MICROS"
export DB_PASSWORD="tu_contraseña_real"
export WALLET_PASSWORD="tu_wallet_password"
export DB_WALLET_PATH="/path/to/your/wallet"
```

### Windows (PowerShell)
```powershell
$env:DB_URL="jdbc:oracle:thin:@rpwbb3wdet77677f_high"
$env:DB_USERNAME="MICROS"
$env:DB_PASSWORD="tu_contraseña_real"
$env:WALLET_PASSWORD="tu_wallet_password"
$env:DB_WALLET_PATH="C:\path\to\your\wallet"
```

### Docker
```dockerfile
ENV DB_URL="jdbc:oracle:thin:@rpwbb3wdet77677f_high"
ENV DB_USERNAME="MICROS"
ENV DB_PASSWORD="tu_contraseña_real"
ENV WALLET_PASSWORD="tu_wallet_password"
```

### application.properties (Ejemplo)
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.oracle.wallet.password=${WALLET_PASSWORD}
```

## Archivo Wallet

Coloca el archivo wallet en una ubicación segura **fuera del repositorio** y usa la variable de entorno:
- Consulta la documentación de Oracle para obtener el archivo wallet correcto
- Almacénalo en una ubicación segura del servidor

## Desarrollo Local

Para desarrollo local, crea un archivo `.env.local` (no versionado):
```
DB_URL=jdbc:oracle:thin:@rpwbb3wdet77677f_high
DB_USERNAME=MICROS
DB_PASSWORD=tu_password
WALLET_PASSWORD=tu_wallet_password
DB_WALLET_PATH=/ruta/local/wallet
```

## CI/CD

Usa los secrets configurados en tu plataforma CI/CD:
- GitHub Actions: Settings → Secrets
- GitLab CI: Settings → CI/CD → Variables
- Jenkins: Manage Jenkins → Manage Credentials

## Checklist de Seguridad
- ✅ No commit de credenciales en properties
- ✅ Wallet eliminado del repositorio
- ✅ .gitignore actualizado
- ✅ Variables de entorno configuradas
- ✅ Desarrolladores usan archivos .local o secretos del sistema
