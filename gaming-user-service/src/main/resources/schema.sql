-- Crear tabla users si no existe
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    address VARCHAR(200),
    date_of_birth TIMESTAMP,
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    is_active BOOLEAN DEFAULT true,
    email_verified BOOLEAN DEFAULT false,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Crear índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_users_email_verified ON users(email_verified);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insertar datos de ejemplo
INSERT INTO users (username, email, password, first_name, last_name, phone, address, role, email_verified)
VALUES
    ('admin', 'admin@gaming.com', 'admin123', 'Admin', 'System', '+1234567890', '123 Admin St', 'ADMIN', true),
    ('johndoe', 'john.doe@email.com', 'password123', 'John', 'Doe', '+1234567891', '456 User Ave', 'USER', true),
    ('janesmith', 'jane.smith@email.com', 'password123', 'Jane', 'Smith', '+1234567892', '789 Gamer Blvd', 'USER', false),
    ('mikegamer', 'mike.gaming@email.com', 'password123', 'Mike', 'Johnson', '+1234567893', '321 Play St', 'USER', true),
    ('sarahplayer', 'sarah.player@email.com', 'password123', 'Sarah', 'Wilson', '+1234567894', '654 Console Ave', 'USER', false)
    ON CONFLICT (username) DO NOTHING;