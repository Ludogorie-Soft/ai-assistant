# IntelliJ IDEA Setup Guide

## Running Spring Boot Application from IntelliJ

When running the application from IntelliJ (not in Docker), you need to configure environment variables and activate the `local` profile.

### Option 1: Using the Local Profile (Recommended)

1. **Open Run Configuration:**
   - Go to `Run` → `Edit Configurations...`
   - Select your Spring Boot run configuration (or create a new one)

2. **Set Active Profile:**
   - In the "VM options" field, add:
     ```
     -Dspring.profiles.active=local
     ```
   - Or in the "Active profiles" field (if available), enter: `local`

3. **Set Environment Variables:**
   - In the "Environment variables" field, add all required variables:
     ```
     AUTHORIZATION_TOKEN=sk-imwvyhjx3qrbs7s0hfnmgdb9mu4vixmtss0fktwasr1xo25er4kvhbkyiso3ocms69
     TWILIO_ENCRYPTED_KEY=3eb4c5f5-8c5b-455a-81ee-0a6c8a2a5c57
     POSTGRES_USER=root
     POSTGRES_PASSWORD=root
     POSTGRES_DB=ai_database
     ELEVEN_LABS_ENCRYPTED_KEY=sk_925686489853647b3e5362313e0ef9f676360ddf792d765d
     ELEVENLABS_AGENT_ID=agent_01jxy9g3rbfsjsev7faxxd6vdv
     ELEVENLABS_AGENT_PHONE_ID=phnum_2501kgm3epxxfexacek5cyecvhke
     ```
   - **Note:** Replace with your actual values from `.env` file

4. **Alternative: Use EnvFile Plugin (Easier)**
   - Install the "EnvFile" plugin from IntelliJ Marketplace
   - In Run Configuration → "EnvFile" tab
   - Check "Enable EnvFile" 
   - Click "+" to add an entry
   - Set the path to your `.env` file: `C:\LS projects\ai-assistant\.env` (or use relative path: `.env`)
   - Make sure "Enable EnvFile" checkbox is checked
   - **Important:** The EnvFile plugin loads variables as environment variables, which Spring Boot will then use to resolve `${ELEVEN_LABS_ENCRYPTED_KEY}` etc.
   - This automatically loads all variables from `.env`

### Option 2: Without Local Profile (Using Environment Variables)

If you prefer not to use the local profile:

1. **Set Environment Variables in Run Configuration:**
   ```
   AUTHORIZATION_TOKEN=your_token
   TWILIO_ENCRYPTED_KEY=your_key
   POSTGRES_USER=root
   POSTGRES_PASSWORD=root
   POSTGRES_DB=ai_database
   DB_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ai_database
   ELEVEN_LABS_ENCRYPTED_KEY=your_key
   ELEVENLABS_AGENT_ID=your_agent_id
   ELEVENLABS_AGENT_PHONE_ID=your_phone_id
   ```

2. **Make sure PostgreSQL Docker container is running:**
   ```bash
   docker-compose up -d db
   ```

### Quick Setup Steps:

1. **Ensure PostgreSQL is running:**
   ```bash
   docker-compose up -d db
   ```

2. **Configure IntelliJ Run Configuration:**
   - VM options: `-Dspring.profiles.active=local`
   - Environment variables: Copy from `.env` file (or use EnvFile plugin)

3. **Run the application**

### Troubleshooting

**Error: "Driver claims to not accept jdbcUrl, ${DB_DATASOURCE_URL}"**
- This means environment variables are not being loaded
- Solution: Set environment variables in IntelliJ run configuration or use the `local` profile

**Error: "Connection refused"**
- Make sure PostgreSQL Docker container is running: `docker ps`
- Check port 5432 is not blocked
- Verify database credentials match `.env` file

**Error: "Environment variable not found" or "Injection of autowired dependencies failed"**
- Ensure all required environment variables are set in IntelliJ run configuration
- If using EnvFile plugin:
  - Verify the `.env` file path is correct in the EnvFile tab
  - Make sure "Enable EnvFile" checkbox is checked
  - The file path should be: `C:\LS projects\ai-assistant\.env` (absolute) or `.env` (relative to project root)
  - Restart IntelliJ if the plugin was just installed
- Check that all these variables exist in your `.env` file:
  - `ELEVEN_LABS_ENCRYPTED_KEY`
  - `AUTHORIZATION_TOKEN`
  - `TWILIO_ENCRYPTED_KEY`
  - `ELEVENLABS_AGENT_ID`
  - `ELEVENLABS_AGENT_PHONE_ID`
