-- Crear tabla games si no existe
CREATE TABLE IF NOT EXISTS games (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    description TEXT,
    genre VARCHAR(100) NOT NULL,
    platform VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    stock INTEGER DEFAULT 0 CHECK (stock >= 0),
    image_url VARCHAR(500),
    developer VARCHAR(150) NOT NULL,
    release_year INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Crear índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_games_title ON games(title);
CREATE INDEX IF NOT EXISTS idx_games_genre ON games(genre);
CREATE INDEX IF NOT EXISTS idx_games_platform ON games(platform);
CREATE INDEX IF NOT EXISTS idx_games_developer ON games(developer);
CREATE INDEX IF NOT EXISTS idx_games_is_active ON games(is_active);

-- Insertar datos de ejemplo
INSERT INTO games (title, description, genre, platform, price, stock, image_url, developer, release_year)
VALUES
    ('The Legend of Zelda: Breath of the Wild', 'Aventura épica en mundo abierto', 'Aventura', 'Nintendo Switch', 59.99, 25, 'https://example.com/zelda.jpg', 'Nintendo', 2017),
    ('God of War', 'Aventura nórdica de Kratos y Atreus', 'Acción', 'PlayStation 5', 49.99, 15, 'https://example.com/gow.jpg', 'Santa Monica Studio', 2018),
    ('Cyberpunk 2077', 'RPG futurista en Night City', 'RPG', 'PC', 39.99, 30, 'https://example.com/cyberpunk.jpg', 'CD Projekt Red', 2020),
    ('Minecraft', 'Construcción y supervivencia en bloques', 'Sandbox', 'PC', 26.95, 100, 'https://example.com/minecraft.jpg', 'Mojang Studios', 2011),
    ('Spider-Man: Miles Morales', 'Aventura de superhéroes en Nueva York', 'Acción', 'PlayStation 5', 49.99, 20, 'https://example.com/spiderman.jpg', 'Insomniac Games', 2020)
    ON CONFLICT DO NOTHING;