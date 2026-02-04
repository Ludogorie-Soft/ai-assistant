# PostgreSQL 18+ Migration Guide

## Overview
PostgreSQL 18+ changed the data directory structure. The volume mount point needs to be updated from `/var/lib/postgresql/data` to `/var/lib/postgresql`.

## Local Development (No Data Preservation Needed)

Run these commands to clean up and start fresh:

```bash
# Stop and remove containers
docker-compose down

# Remove the old volume
docker volume rm ai-assistant_pg_data
# Or if the volume name is different, list volumes first:
# docker volume ls | grep pg_data

# Start with the new configuration
docker-compose up -d
```

## Server Migration (Data Preservation Required)

### Option 1: Using pg_upgrade (Recommended for Production)

This requires running both old and new PostgreSQL versions temporarily.

1. **Backup your data first:**
   ```bash
   docker exec ai_database pg_dumpall -U ${POSTGRES_USER} > backup.sql
   ```

2. **Stop the current containers:**
   ```bash
   docker-compose down
   ```

3. **Temporarily modify docker-compose.yaml to use PostgreSQL 17:**
   ```yaml
   db:
     image: postgres:17
     # ... rest of config
   ```

4. **Start PostgreSQL 17 and restore data:**
   ```bash
   docker-compose up -d db
   docker exec -i ai_database psql -U ${POSTGRES_USER} < backup.sql
   ```

5. **Update docker-compose.yaml back to latest and use new mount:**
   ```yaml
   db:
     image: postgres:latest
     volumes:
       - pg_data:/var/lib/postgresql  # Updated mount point
   ```

6. **Use pg_upgrade to migrate:**
   - This is complex and requires both versions running simultaneously
   - See: https://www.postgresql.org/docs/current/pgupgrade.html

### Option 2: Dump and Restore (Simpler, Recommended)

1. **Export all data:**
   ```bash
   docker exec ai_database pg_dumpall -U ${POSTGRES_USER} > full_backup.sql
   ```

2. **Stop containers and remove old volume:**
   ```bash
   docker-compose down
   docker volume rm <volume_name>
   ```

3. **Update docker-compose.yaml** (already done - uses `/var/lib/postgresql`)

4. **Start fresh container:**
   ```bash
   docker-compose up -d db
   ```

5. **Restore data:**
   ```bash
   docker exec -i ai_database psql -U ${POSTGRES_USER} < full_backup.sql
   ```

6. **Start the app:**
   ```bash
   docker-compose up -d
   ```

### Option 3: Manual Volume Migration

If you need to preserve the exact volume structure:

1. **Stop containers:**
   ```bash
   docker-compose down
   ```

2. **Create a temporary container to access the volume:**
   ```bash
   docker run --rm -v <old_volume_name>:/old_data -v <new_volume_name>:/new_data postgres:latest bash -c "cp -r /old_data/* /new_data/"
   ```

3. **Update docker-compose.yaml** (already done)

4. **Start containers:**
   ```bash
   docker-compose up -d
   ```

## Verification

After migration, verify the database is working:

```bash
# Check container logs
docker logs ai_database

# Connect to database
docker exec -it ai_database psql -U ${POSTGRES_USER} -d ${POSTGRES_DB}

# List tables
\dt
```

## Notes

- The new mount point `/var/lib/postgresql` allows PostgreSQL 18+ to manage version-specific directories internally
- This structure is compatible with `pg_ctlcluster` and future upgrades
- Always backup before migration in production!
