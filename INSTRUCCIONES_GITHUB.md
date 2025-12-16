# 📤 Instrucciones para Subir el Backend a GitHub

## ✅ Estado Actual

- ✅ Repositorio Git inicializado
- ✅ Commit inicial realizado (solo backend)
- ✅ Frontend excluido del repositorio (mantenido privado)
- ✅ README actualizado para solo mostrar backend

## 🚀 Pasos para Subir a GitHub

### Paso 1: Crear el Repositorio en GitHub

1. Ve a [GitHub.com](https://github.com) e inicia sesión
2. Haz clic en el botón **"+"** (arriba a la derecha) → **"New repository"**
3. Configura el repositorio:
   - **Repository name**: `gestor-proyectos-backend` (o el nombre que prefieras)
   - **Description**: `Sistema Gestor de Proyectos - Backend Spring Boot para el taller SENA`
   - **Visibility**: ✅ **Public** (para que tu instructor pueda verlo)
   - ⚠️ **NO marques** "Add a README file" (ya tenemos uno)
   - ⚠️ **NO marques** "Add .gitignore" (ya tenemos uno)
   - ⚠️ **NO marques** "Choose a license"
4. Haz clic en **"Create repository"**

### Paso 2: Conectar el Repositorio Local con GitHub

Después de crear el repositorio, GitHub te mostrará instrucciones. Ejecuta estos comandos en tu terminal:

```bash
cd /home/julioparada99/Escritorio/Proyectos/Ropero

# Agregar el repositorio remoto (reemplaza TU_USUARIO con tu usuario de GitHub)
git remote add origin https://github.com/TU_USUARIO/gestor-proyectos-backend.git

# Verificar que se agregó correctamente
git remote -v
```

### Paso 3: Subir el Código

```bash
# Subir el código a GitHub
git push -u origin main
```

Si te pide autenticación:
- Si usas HTTPS: GitHub te pedirá usuario y contraseña (o token personal)
- Si prefieres SSH: cambia la URL del remote a `git@github.com:TU_USUARIO/gestor-proyectos-backend.git`

### Paso 4: Verificar

1. Ve a tu repositorio en GitHub: `https://github.com/TU_USUARIO/gestor-proyectos-backend`
2. Verifica que todos los archivos del backend estén presentes
3. Verifica que **NO** aparezca la carpeta `frontend/`

## 📋 Archivos que DEBEN estar en GitHub

✅ `.gitignore`
✅ `README.md`
✅ `pom.xml`
✅ `mvnw` (Maven Wrapper)
✅ `.mvn/` (configuración Maven Wrapper)
✅ `src/` (todo el código Java)
✅ `database/schema.sql`
✅ `docs/` (DER.md, Pseudocodigo.md)

## ❌ Archivos que NO deben estar en GitHub

❌ `frontend/` (completamente excluido)
❌ `target/` (compilados, excluido por .gitignore)
❌ `.idea/`, `.vscode/` (configuración IDE, excluido)

## 🔗 Compartir con tu Instructor

Una vez subido, comparte este link:
```
https://github.com/TU_USUARIO/gestor-proyectos-backend
```

## ⚠️ Si tienes problemas

### Error: "remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/TU_USUARIO/gestor-proyectos-backend.git
```

### Error de autenticación
- Opción 1: Usar Personal Access Token (recomendado)
  - Ve a GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
  - Genera un nuevo token con permisos `repo`
  - Úsalo como contraseña cuando Git te la pida

- Opción 2: Configurar SSH
  ```bash
  # Generar clave SSH (si no tienes una)
  ssh-keygen -t ed25519 -C "tu-email@example.com"
  
  # Agregar la clave a GitHub (Settings → SSH and GPG keys)
  cat ~/.ssh/id_ed25519.pub
  
  # Cambiar el remote a SSH
  git remote set-url origin git@github.com:TU_USUARIO/gestor-proyectos-backend.git
  ```

## 📝 Notas Importantes

- El frontend permanece **privado** en tu máquina local
- Solo el backend es público en GitHub
- Tu instructor podrá clonar y ejecutar el backend sin problemas
- El README contiene todas las instrucciones necesarias para ejecutar el proyecto

