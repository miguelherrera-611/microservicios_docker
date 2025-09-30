-- Crear tabla reviews si no existe
CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGSERIAL PRIMARY KEY,
                                       game_id BIGINT NOT NULL,
                                       user_id BIGINT NOT NULL,
                                       rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_verified_purchase BOOLEAN DEFAULT false,
    is_approved BOOLEAN DEFAULT true,
    helpful_count INTEGER DEFAULT 0 CHECK (helpful_count >= 0),
    unhelpful_count INTEGER DEFAULT 0 CHECK (unhelpful_count >= 0),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraint para evitar múltiples reseñas del mismo usuario para el mismo juego
    UNIQUE(game_id, user_id)
    );

-- Crear índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_reviews_game_id ON reviews(game_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user_id ON reviews(user_id);
CREATE INDEX IF NOT EXISTS idx_reviews_rating ON reviews(rating);
CREATE INDEX IF NOT EXISTS idx_reviews_is_approved ON reviews(is_approved);
CREATE INDEX IF NOT EXISTS idx_reviews_is_active ON reviews(is_active);
CREATE INDEX IF NOT EXISTS idx_reviews_created_at ON reviews(created_at);
CREATE INDEX IF NOT EXISTS idx_reviews_helpful_count ON reviews(helpful_count);
CREATE INDEX IF NOT EXISTS idx_reviews_game_user ON reviews(game_id, user_id);

-- Insertar datos de ejemplo
INSERT INTO reviews (game_id, user_id, rating, comment, is_verified_purchase, helpful_count, unhelpful_count)
VALUES
    (1, 1, 5, 'Increíble juego! La aventura es épica y los gráficos son impresionantes. Totalmente recomendado.', true, 15, 2),
    (1, 2, 4, 'Muy buen juego, aunque algo repetitivo en algunas partes. La historia está bien desarrollada.', false, 8, 1),
    (2, 1, 5, 'God of War es una obra maestra. La relación entre Kratos y Atreus es emotiva y el combate es brutal.', true, 22, 0),
    (2, 3, 4, 'Excelente juego con una narrativa sólida. Los puzzles son desafiantes pero justos.', true, 12, 3),
    (3, 2, 3, 'Cyberpunk tiene potencial pero aún tiene muchos bugs. Esperemos que mejore con actualizaciones.', false, 5, 8),
    (3, 4, 2, 'Muy decepcionante. Muchas promesas incumplidas y problemas técnicos constantes.', false, 18, 4),
    (4, 1, 5, 'Minecraft nunca pasa de moda. La creatividad no tiene límites en este juego.', true, 25, 1),
    (4, 3, 5, 'Perfecto para relajarse y construir. Las posibilidades son infinitas.', true, 14, 0),
    (5, 2, 4, 'Spider-Man Miles Morales es divertido pero corto. Los gráficos en PS5 son espectaculares.', true, 9, 2),
    (5, 4, 5, 'Me encanta balancearme por Nueva York. Los poderes de Miles son únicos y entretenidos.', false, 11, 1)
    ON CONFLICT (game_id, user_id) DO NOTHING;